package com.ucaldas.otri.domain.technologies;

import jakarta.persistence.Embeddable;

import java.util.Date;

@Embeddable
public class IntellectualProtection {
    private boolean hasCurrentProtection;
    private String suggestedProtectionType;
    private boolean hasBeenDisclosed;
    private Date disclosedDate;
}
