package com.ucaldas.otri.application.technologies.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromptAnswer {
    private String question;
    private boolean answer;
}
