package com.summarize.summarizer.controller;

import com.summarize.summarizer.service.SummarizeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/summarize")
public class SummarizeController
{

    private SummarizeService summarizeService;

    public SummarizeController(SummarizeService service) {
        this.summarizeService = service;
    }

    @PostMapping
    public String summarize(@RequestBody String message) {
        return summarizeService.summarize(message);
    }

}
