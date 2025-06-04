package com.ucaldas.otri.application.technologies.models;

import com.ucaldas.otri.domain.technologies.enums.ReadinessType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewLevelAnswersResponse {
    private UUID id;
    private UUID technologyId;
    private String question;
    private boolean answer;
    private boolean checked;
    private int level;
    private ReadinessType type;
    private String reason;
}
