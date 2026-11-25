package com.upm.notes.controller;

import com.upm.notes.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Value("${app.message}")
    private String message;

    @Value("${app.version}")
    private String version;

    @GetMapping
    public MessageResponse getMessage() {
        return new MessageResponse(message, version);
    }
}
