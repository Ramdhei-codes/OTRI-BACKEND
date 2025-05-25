package com.ucaldas.otri.application.technologies.models;

import com.ucaldas.otri.domain.technologies.enums.InventiveLevel;
import com.ucaldas.otri.domain.technologies.enums.PreliminaryInterest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterTechnologyRequest {
    private String resultName;
    private String responsibleGroup;
    private String functionalSummary;
    private String problemSolved;
    private int trlLevel;
    private String economicSector;
    private boolean hasCurrentProtection;
    private String suggestedProtectionType;
    private boolean hasBeenDisclosed;
    private Date disclosedDate;
    private InventiveLevel inventiveLevel;
    private String noveltyDescription;
    private boolean hasIndustrialApplication;
    private boolean teamAvailableForTransfer;
    private String competitiveAdvantage;
    private int crlLevel;
    private boolean teamAvailableToCollaborate;
    private boolean hasScalingCapability;
    private String competitorsNational;
    private String competitorsInternational;
    private String substituteTechnologies;
    private String marketSize;
    private String applicationSectors;
    private PreliminaryInterest preliminaryInterest;
    private String transferMethod;
    private String recommendedActions;
}
