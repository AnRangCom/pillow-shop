package com.exe201.pillow.service.dto;

import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

public class ChatMessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    private String message;

    private String sessionId;

    public ChatMessageDTO() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "ChatMessageDTO{" + "message='" + message + '\'' + ", sessionId='" + sessionId + '\'' + '}';
    }
}
