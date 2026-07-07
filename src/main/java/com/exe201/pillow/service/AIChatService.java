package com.exe201.pillow.service;

import com.exe201.pillow.domain.Pillow;
import com.exe201.pillow.service.dto.ChatResponseDTO;
import com.exe201.pillow.service.dto.PillowDTO;
import com.exe201.pillow.service.mapper.PillowMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AIChatService {

    private static final Logger LOG = LoggerFactory.getLogger(AIChatService.class);

    private static final String SYSTEM_PROMPT = """
        Bạn là trợ lý tư vấn gối của Pillow Shop - cửa hàng gối chất lượng cao.

        Nhiệm vụ của bạn:
        1. Hỏi rõ nhu cầu khách hàng (ngủ nghiêng, nằm ngửa, đau cổ, hay đọc sách, v.v.)
        2. Dựa vào catalog sản phẩm để gợi ý gối phù hợp nhất
        3. So sánh các loại gối khi khách hàng yêu cầu
        4. Giải thích tại sao sản phẩm phù hợp với nhu cầu của họ
        5. Luôn trả lời bằng tiếng Việt, thân thiện và chuyên nghiệp

        Khi gợi ý sản phẩm, hãy trả về JSON array chứa ID sản phẩm trong tag [SUGGESTIONS]:
        [SUGGESTIONS: id1, id2, id3]

        Nếu không gợi ý sản phẩm cụ thể, không cần tag này.
        """;

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.model-name:gemini-2.0-flash}")
    private String modelName;

    private final PillowService pillowService;
    private final PillowMapper pillowMapper;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private final Map<String, List<Map<String, Object>>> chatHistories = new ConcurrentHashMap<>();

    public AIChatService(PillowService pillowService, PillowMapper pillowMapper) {
        this.pillowService = pillowService;
        this.pillowMapper = pillowMapper;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public ChatResponseDTO chat(String message, String sessionId) {
        LOG.debug("Chat request: message={}, sessionId={}", message, sessionId);

        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }

        try {
            String catalogContext = buildCatalogContext();
            String fullSystemPrompt = SYSTEM_PROMPT + "\n\n--- CATALOG SẢN PHẨM ---\n" + catalogContext;

            String response = callGeminiAPI(message, fullSystemPrompt, sessionId);

            List<Long> suggestedIds = extractSuggestionIds(response);
            String cleanReply = removeSuggestionTags(response);

            List<PillowDTO> suggestions = new ArrayList<>();
            if (!suggestedIds.isEmpty()) {
                suggestions = pillowMapper.toDto(pillowService.findByIds(suggestedIds));
            }

            return new ChatResponseDTO(cleanReply, suggestions, sessionId);
        } catch (Exception e) {
            LOG.error("Chat error: {}", e.getMessage(), e);
            return new ChatResponseDTO("Xin lỗi, tôi đang gặp sự cố kỹ thuật. Vui lòng thử lại sau.", new ArrayList<>(), sessionId);
        }
    }

    public ChatResponseDTO compareProducts(List<Long> pillowIds, String sessionId) {
        LOG.debug("Compare products: pillowIds={}, sessionId={}", pillowIds, sessionId);

        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }

        try {
            List<Pillow> pillows = pillowService.findByIds(pillowIds);
            if (pillows.isEmpty()) {
                return new ChatResponseDTO("Không tìm thấy sản phẩm để so sánh.", new ArrayList<>(), sessionId);
            }

            String comparePrompt = buildComparePrompt(pillows);
            String fullPrompt = SYSTEM_PROMPT + "\n\n--- YÊU CẦU SO SÁNH ---\n" + comparePrompt;

            String response = callGeminiAPI("So sánh các sản phẩm được liệt kê", fullPrompt, sessionId);

            List<PillowDTO> suggestions = pillowMapper.toDto(pillows);
            return new ChatResponseDTO(response, suggestions, sessionId);
        } catch (Exception e) {
            LOG.error("Compare error: {}", e.getMessage(), e);
            return new ChatResponseDTO(
                "Xin lỗi, tôi đang gặp sự cố khi so sánh sản phẩm. Vui lòng thử lại sau.",
                new ArrayList<>(),
                sessionId
            );
        }
    }

    public ChatResponseDTO getInitialSuggestions(String sessionId) {
        LOG.debug("Get initial suggestions: sessionId={}", sessionId);

        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }

        try {
            List<Pillow> allPillows = pillowService.findAllEntities();
            List<PillowDTO> suggestions = pillowMapper.toDto(allPillows.stream().limit(3).toList());

            String welcomeMessage =
                "Chào mừng bạn đến với Pillow Shop! " +
                "Tôi là trợ lý tư vấn gối của bạn. " +
                "Hãy cho tôi biết nhu cầu của bạn (ví dụ: ngủ nghiêng, đau cổ, đọc sách...) " +
                "để tôi gợi ý sản phẩm phù hợp nhất nhé!";

            return new ChatResponseDTO(welcomeMessage, suggestions, sessionId);
        } catch (Exception e) {
            LOG.error("Initial suggestions error: {}", e.getMessage(), e);
            return new ChatResponseDTO(
                "Chào mừng bạn đến với Pillow Shop! Hãy cho tôi biết bạn cần tư vấn gì về gối nhé!",
                new ArrayList<>(),
                sessionId
            );
        }
    }

    private String callGeminiAPI(String userMessage, String systemPrompt, String sessionId) throws Exception {
        List<Map<String, Object>> history = chatHistories.computeIfAbsent(sessionId, k -> new ArrayList<>());

        if (history.isEmpty()) {
            history.add(Map.of("role", "user", "parts", List.of(Map.of("text", systemPrompt))));
            history.add(Map.of("role", "model", "parts", List.of(Map.of("text", "Tôi hiểu. Tôi sẽ tư vấn gối cho khách hàng."))));
        }

        history.add(Map.of("role", "user", "parts", List.of(Map.of("text", userMessage))));

        Map<String, Object> requestBody = Map.of("contents", history);

        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + modelName + ":generateContent?key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode root = objectMapper.readTree(response.body());
        String text = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

        history.add(Map.of("role", "model", "parts", List.of(Map.of("text", text))));

        return text;
    }

    private String buildCatalogContext() {
        List<Pillow> pillows = pillowService.findAllEntities();
        StringBuilder sb = new StringBuilder();

        for (Pillow pillow : pillows) {
            sb.append("- ID: ").append(pillow.getId());
            sb.append(", Tên: ").append(pillow.getName());
            sb.append(", Chất liệu: ").append(pillow.getMaterial() != null ? pillow.getMaterial() : "N/A");
            sb.append(", Giá: ").append(pillow.getBasePrice()).append(" VNĐ");
            sb.append(", Mô tả: ").append(pillow.getDescription() != null ? pillow.getDescription() : "N/A");
            sb.append("\n");

            if (pillow.getDefaultSizes() != null && !pillow.getDefaultSizes().isEmpty()) {
                pillow
                    .getDefaultSizes()
                    .forEach(size ->
                        sb
                            .append("  + Size: ")
                            .append(size.getName())
                            .append(" (")
                            .append(size.getLength())
                            .append("x")
                            .append(size.getWidth())
                            .append("), Phụ phí: ")
                            .append(size.getExtraPrice())
                            .append(" VNĐ\n")
                    );
            }
        }

        return sb.toString();
    }

    private String buildComparePrompt(List<Pillow> pillows) {
        StringBuilder sb = new StringBuilder("So sánh chi tiết các sản phẩm sau:\n\n");

        for (Pillow pillow : pillows) {
            sb.append("Sản phẩm: ").append(pillow.getName()).append("\n");
            sb.append("- Giá: ").append(pillow.getBasePrice()).append(" VNĐ\n");
            sb.append("- Chất liệu: ")
                .append(pillow.getMaterial() != null ? pillow.getMaterial() : "N/A")
                .append("\n");
            sb.append("- Mô tả: ")
                .append(pillow.getDescription() != null ? pillow.getDescription() : "N/A")
                .append("\n");

            if (pillow.getDefaultSizes() != null && !pillow.getDefaultSizes().isEmpty()) {
                sb.append("- Kích thước:\n");
                pillow
                    .getDefaultSizes()
                    .forEach(size ->
                        sb
                            .append("  + ")
                            .append(size.getName())
                            .append(": ")
                            .append(size.getLength())
                            .append("x")
                            .append(size.getWidth())
                            .append(", Phụ phí: ")
                            .append(size.getExtraPrice())
                            .append(" VNĐ\n")
                    );
            }
            sb.append("\n");
        }

        sb.append("Hãy so sánh ưu nhược điểm, đưa ra bảng so sánh, và gợi ý sản phẩm phù hợp cho từng nhu cầu.");
        return sb.toString();
    }

    private List<Long> extractSuggestionIds(String response) {
        List<Long> ids = new ArrayList<>();

        int start = response.indexOf("[SUGGESTIONS:");
        if (start == -1) {
            return ids;
        }

        int end = response.indexOf("]", start);
        if (end == -1) {
            return ids;
        }

        String idsStr = response.substring(start + 13, end).trim();
        for (String idStr : idsStr.split(",")) {
            try {
                ids.add(Long.parseLong(idStr.trim()));
            } catch (NumberFormatException e) {
                LOG.warn("Invalid suggestion ID: {}", idStr);
            }
        }

        return ids;
    }

    private String removeSuggestionTags(String response) {
        return response.replaceAll("\\[SUGGESTIONS:.*?\\]", "").trim();
    }
}
