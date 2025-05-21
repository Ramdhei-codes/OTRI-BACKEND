package com.ucaldas.otri.domain.technologies;

import jakarta.persistence.Embeddable;

@Embeddable
public class TechnicalDescription {
    private String functionalSummary;
    private String problemSolved;
    private int trlLevel;
    private String economicSector;
}
