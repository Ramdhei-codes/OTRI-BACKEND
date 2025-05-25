package com.ucaldas.otri.domain.technologies.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TechnicalDescription {
    private String functionalSummary;
    private String problemSolved;
    private int trlLevel;
    private String economicSector;
}
