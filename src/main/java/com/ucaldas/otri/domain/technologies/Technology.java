package com.ucaldas.otri.domain.technologies;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Technologies")
public class Technology {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String resultName;
    private String responsibleGroup;
    @Embedded
    private TechnicalDescription technicalDescription;
    @Embedded
    private IntellectualProtection intellectualProtection;
    @Embedded
    private PatentabilityAnalysis patentabilityAnalysis;
    @Embedded
    private MarketAnalysis marketAnalysis;
    private String transferMethod;
    private String recommendedActions;
    @OneToMany(mappedBy = "technology")
    private List<Answer> answers;
}
