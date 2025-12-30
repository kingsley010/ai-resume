package com.example.airesume.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ResumeResponse {

    private List<Output> output;

    @Data
    public static class Output {
        private String id;
        private String type;
        private Content content;
    }

    @Data
    public static class Content {
        private String text;
    }
}
