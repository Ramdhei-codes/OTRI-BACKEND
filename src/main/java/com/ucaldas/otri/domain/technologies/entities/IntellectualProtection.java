package com.ucaldas.otri.domain.technologies.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class IntellectualProtection {
    private boolean hasCurrentProtection;
    private String suggestedProtectionType;
    private boolean hasBeenDisclosed;
    private Date disclosedDate;
}
