package com.ucaldas.otri.domain.technologies.entities;

import com.ucaldas.otri.domain.technologies.enums.TechnologyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
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
    @Column(columnDefinition = "TEXT")
    private String recommendedActions;
    private Date createdDate;
    private Date lastUpdatedDate;
    private TechnologyStatus status;
    @OneToMany(mappedBy = "technology")
    private List<Answer> answers;
}
