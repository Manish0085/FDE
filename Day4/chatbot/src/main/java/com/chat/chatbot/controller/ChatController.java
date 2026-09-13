package com.chat.chatbot.controller;

import com.chat.chatbot.dto.ChatRequest;
import com.chat.chatbot.dto.ChatResponse;
import com.chat.chatbot.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private ChatService chatService;
    private ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
        ChatResponse response = chatService.chat(chatRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
