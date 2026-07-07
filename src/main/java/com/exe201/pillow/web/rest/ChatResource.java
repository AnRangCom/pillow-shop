package com.exe201.pillow.web.rest;

import com.exe201.pillow.service.AIChatService;
import com.exe201.pillow.service.dto.ChatMessageDTO;
import com.exe201.pillow.service.dto.ChatResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatResource {

    private static final Logger LOG = LoggerFactory.getLogger(ChatResource.class);

    private final AIChatService aiChatService;

    public ChatResource(AIChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("")
    public ResponseEntity<ChatResponseDTO> chat(@Valid @RequestBody ChatMessageDTO chatMessage) {
        LOG.debug("REST request to chat : {}", chatMessage);
        ChatResponseDTO response = aiChatService.chat(chatMessage.getMessage(), chatMessage.getSessionId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/compare")
    public ResponseEntity<ChatResponseDTO> compareProducts(
        @RequestBody List<Long> pillowIds,
        @RequestParam(required = false) String sessionId
    ) {
        LOG.debug("REST request to compare products : {}", pillowIds);
        ChatResponseDTO response = aiChatService.compareProducts(pillowIds, sessionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<ChatResponseDTO> getInitialSuggestions(@RequestParam(required = false) String sessionId) {
        LOG.debug("REST request to get initial suggestions");
        ChatResponseDTO response = aiChatService.getInitialSuggestions(sessionId);
        return ResponseEntity.ok(response);
    }
}
