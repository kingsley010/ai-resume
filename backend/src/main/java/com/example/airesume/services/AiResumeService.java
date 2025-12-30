package com.example.airesume.services;

import com.example.airesume.dtos.ResumeRequest;
import com.example.airesume.dtos.ResumeResponse;
import com.example.airesume.exceptions.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AiResumeService {

    private final String apiKey;
    private final HttpClient client = HttpClient.newHttpClient();

    public AiResumeService(String openaiApiKey) {
        this.apiKey = openaiApiKey;
    }

    public ResumeResponse generateResume(ResumeRequest request) {
        try {
            String prompt = "Rewrite resume and generate cover letter:\n" +
                    "Resume:\n" + request.getResume() +
                    "\nJob Description:\n" + request.getJobDescription();

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/responses"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{ \"model\": \"gpt-5.1-mini\", \"input\": \"" + prompt + "\" }"
                    ))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return new ObjectMapper().readValue(response.body(), ResumeResponse.class);

        } catch (Exception e) {
            throw new ApiException("OpenAI request failed", e);
        }
    }
}
