package com.example.airesume.controllers;

import com.example.airesume.dtos.ResumeRequest;
import com.example.airesume.dtos.ResumeResponse;
import com.example.airesume.services.AiResumeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/resume")
public class ResumeController {

    private final AiResumeService aiResumeService;

    public ResumeController(AiResumeService aiResumeService) {
        this.aiResumeService = aiResumeService;
    }

    @PostMapping("/generate")
    public ResumeResponse generate(@RequestBody ResumeRequest request) {
        return aiResumeService.generateResume(request);
    }

}
