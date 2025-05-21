package com.ucaldas.otri.domain.technologies;

import jakarta.persistence.Embeddable;

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
