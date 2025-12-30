package com.example.airesume.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResumeRequest {

    private String resume;
    private String jobDescription;

}
