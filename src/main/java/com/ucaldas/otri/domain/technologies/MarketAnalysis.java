package com.ucaldas.otri.domain.technologies;

import jakarta.persistence.Embeddable;

@Embeddable
public class MarketAnalysis {
    private String competitorsNational; 
    private String competitorsInternational; 
    private String substituteTechnologies; 
    private String marketSize; 
    private String applicationSectors; 
    private PreliminaryInterest preliminaryInterest;
}
