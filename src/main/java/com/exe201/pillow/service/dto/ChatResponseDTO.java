package com.exe201.pillow.service.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class ChatResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String reply;
    private List<PillowDTO> suggestions;
    private String sessionId;

    public ChatResponseDTO() {}

    public ChatResponseDTO(String reply, List<PillowDTO> suggestions, String sessionId) {
        this.reply = reply;
        this.suggestions = suggestions;
        this.sessionId = sessionId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public List<PillowDTO> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<PillowDTO> suggestions) {
        this.suggestions = suggestions;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "ChatResponseDTO{" + "reply='" + reply + '\'' + ", sessionId='" + sessionId + '\'' + '}';
    }
}
