package com.example.airesume.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .directory("./")
                .ignoreIfMissing()
                .load();
    }

    @Bean(name = "openaiApiKey")
    public String openaiApiKey(Dotenv dotenv) {
        String key = dotenv.get("OPENAI_API_KEY");
        if (key == null || key.isEmpty()) {
            throw new IllegalStateException("OPENAI_API_KEY is missing from .env file");
        }
        return key;
    }
}
