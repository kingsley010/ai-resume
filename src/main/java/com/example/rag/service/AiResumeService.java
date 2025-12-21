package com.example.rag.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiResumeService {

    private final WebClient webClient;

    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("OPENAI_API_KEY");

    public AiResumeService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization",
                        "Bearer " + apiKey)
                .build();
    }

    public Map<String, Object> generate(String resume, String jobDescription) {

        String prompt =
                "You are a professional technical recruiter.\n\n" +
                        "TASKS:\n" +
                        "1. Rewrite resume bullet points to match the job description\n" +
                        "2. Generate a concise, professional cover letter\n\n" +
                        "RULES:\n" +
                        "- Do NOT invent experience\n" +
                        "- Use strong action verbs\n" +
                        "- Output valid JSON ONLY in this format:\n\n" +
                        "{\n" +
                        "  \"resumeHighlights\": [ \"...\", \"...\" ],\n" +
                        "  \"coverLetter\": \"...\"\n" +
                        "}\n\n" +
                        "RESUME:\n" +
                        resume + "\n\n" +
                        "JOB DESCRIPTION:\n" +
                        jobDescription;

        Map<String, Object> message = new HashMap<String, Object>();
        message.put("role", "user");
        message.put("content", prompt);

        List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
        messages.add(message);

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("model", "gpt-4.1-mini");
        body.put("messages", messages);

        Map response = webClient.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map firstChoice = (Map) ((List) response.get("choices")).get(0);
        Map messageMap = (Map) firstChoice.get("message");
        String content = (String) messageMap.get("content");

        return parseJson(content);
    }

    private Map<String, Object> parseJson(String json) {
        try {
            return new ObjectMapper().readValue(json, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid AI JSON output", e);
        }
    }

}
