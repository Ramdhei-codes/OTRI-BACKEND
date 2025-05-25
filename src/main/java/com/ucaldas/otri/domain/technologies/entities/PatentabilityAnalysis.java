package com.ucaldas.otri.domain.technologies.entities;

import com.ucaldas.otri.domain.technologies.enums.InventiveLevel;
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
public class PatentabilityAnalysis {
    private InventiveLevel inventiveLevel;
    private String noveltyDescription;
    private boolean hasIndustrialApplication;
    private boolean teamAvailableForTransfer;
    private String competitiveAdvantage;
    private int crlLevel;
    private boolean teamAvailableToCollaborate;
    private boolean hasScalingCapability;
}
