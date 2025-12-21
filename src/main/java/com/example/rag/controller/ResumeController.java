package com.example.rag.controller;

import com.example.rag.service.AiResumeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/resume")
public class ResumeController {
    private final AiResumeService aiResumeService;

    public ResumeController(AiResumeService aiResumeService) {
        this.aiResumeService = aiResumeService;
    }

    @PostMapping("/generate")
    public Map<String, Object> generate(@RequestBody Map<String, String> body) {
        return aiResumeService.generate(
                body.get("resume"),
                body.get("jobDescription")
        );
    }

}
