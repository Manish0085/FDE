package com.summarize.summarizer.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SummarizeService
{

    private ChatClient chatClient;

    public SummarizeService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }
    public String summarize(String message) {
        String output = chatClient.prompt()
                .user(message)
                .call()
                .content();

        return output;
    }
}
