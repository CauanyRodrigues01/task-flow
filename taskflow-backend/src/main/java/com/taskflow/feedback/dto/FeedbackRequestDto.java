package com.taskflow.feedback.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FeedbackRequestDto {
    @NotBlank(message = "Feedback message cannot be empty")
    private String message;
    private String context;
}
