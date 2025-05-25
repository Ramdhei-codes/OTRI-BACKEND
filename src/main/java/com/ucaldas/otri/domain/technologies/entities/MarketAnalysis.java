package com.ucaldas.otri.domain.technologies.entities;

import com.ucaldas.otri.domain.technologies.enums.PreliminaryInterest;
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
public class MarketAnalysis {
    private String competitorsNational;
    private String competitorsInternational;
    private String substituteTechnologies;
    private String marketSize;
    private String applicationSectors;
    private PreliminaryInterest preliminaryInterest;
}
