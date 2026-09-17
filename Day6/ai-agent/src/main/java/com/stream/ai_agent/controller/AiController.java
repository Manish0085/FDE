package com.stream.ai_agent.controller;

import com.stream.ai_agent.dto.ChatRequest;
import com.stream.ai_agent.dto.ChatResponse;
import com.stream.ai_agent.service.AiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
public class AiController {

    public AiService aiService;

    public AiController(AiService aiService){
        this.aiService = aiService;
    }


    @PostMapping
    public ResponseEntity<ChatResponse> aiChat(@RequestBody ChatRequest request) {
        ChatResponse response = aiService.aiChat(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
