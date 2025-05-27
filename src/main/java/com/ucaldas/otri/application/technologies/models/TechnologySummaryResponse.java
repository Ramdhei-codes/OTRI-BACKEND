package com.ucaldas.otri.application.technologies.models;

import com.ucaldas.otri.domain.technologies.enums.TechnologyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnologySummaryResponse {
    private UUID technologyId;
    private String resultName;
    private String responsibleGroup;
    private Date lastUpdatedDate;
    private TechnologyStatus status;
}
