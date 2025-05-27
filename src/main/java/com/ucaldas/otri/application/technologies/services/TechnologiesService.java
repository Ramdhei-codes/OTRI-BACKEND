package com.ucaldas.otri.application.technologies.services;

import com.ucaldas.otri.application.shared.exceptions.ApplicationException;
import com.ucaldas.otri.application.shared.exceptions.ErrorCodes;
import com.ucaldas.otri.application.technologies.models.RegisterTechnologyRequest;
import com.ucaldas.otri.application.technologies.models.TechnologySummaryResponse;
import com.ucaldas.otri.application.technologies.models.ViewTechnologyResponse;
import com.ucaldas.otri.domain.technologies.entities.*;
import com.ucaldas.otri.domain.technologies.enums.TechnologyStatus;
import com.ucaldas.otri.domain.technologies.repositories.TechnologiesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologiesService {
    private final TechnologiesRepository repository;

    public String register(RegisterTechnologyRequest request){
        TechnicalDescription technicalDescription = getTechnicalDescription(request);
        IntellectualProtection intellectualProtection = getIntellectualProtection(request);
        PatentabilityAnalysis patentabilityAnalysis = getPatentabilityAnalysis(request);
        MarketAnalysis marketAnalysis = getMarketAnalysis(request);
        Technology technology = Technology
                .builder()
                .resultName(request.getResultName())
                .responsibleGroup(request.getResponsibleGroup())
                .technicalDescription(technicalDescription)
                .intellectualProtection(intellectualProtection)
                .patentabilityAnalysis(patentabilityAnalysis)
                .marketAnalysis(marketAnalysis)
                .transferMethod(request.getTransferMethod())
                .createdDate(new Date())
                .status(TechnologyStatus.ACTIVE)
                .build();
        Technology savedTechnology = repository.save(technology);
        return savedTechnology.getId().toString();
    }

    public ViewTechnologyResponse view(UUID technologyId){
        Technology technology = repository.findById(technologyId).orElseThrow( () ->
                new ApplicationException(
                        "No se encontraron resultados",
                        ErrorCodes.VALIDATION_ERROR
                )
        );

        return ViewTechnologyResponse
                .builder()
                .technologyId(technology.getId())
                .resultName(technology.getResultName())
                .responsibleGroup(technology.getResponsibleGroup())
                .functionalSummary(technology.getTechnicalDescription().getFunctionalSummary())
                .problemSolved(technology.getTechnicalDescription().getProblemSolved())
                .trlLevel(technology.getTechnicalDescription().getTrlLevel())
                .economicSector(technology.getTechnicalDescription().getEconomicSector())
                .hasCurrentProtection(technology.getIntellectualProtection().isHasCurrentProtection())
                .suggestedProtectionType(technology.getIntellectualProtection().getSuggestedProtectionType())
                .hasBeenDisclosed(technology.getIntellectualProtection().isHasBeenDisclosed())
                .disclosedDate(technology.getIntellectualProtection().getDisclosedDate())
                .inventiveLevel(technology.getPatentabilityAnalysis().getInventiveLevel())
                .noveltyDescription(technology.getPatentabilityAnalysis().getNoveltyDescription())
                .hasIndustrialApplication(technology.getPatentabilityAnalysis().isHasIndustrialApplication())
                .teamAvailableForTransfer(technology.getPatentabilityAnalysis().isTeamAvailableForTransfer())
                .competitiveAdvantage(technology.getPatentabilityAnalysis().getCompetitiveAdvantage())
                .crlLevel(technology.getPatentabilityAnalysis().getCrlLevel())
                .teamAvailableToCollaborate(technology.getPatentabilityAnalysis().isTeamAvailableToCollaborate())
                .hasScalingCapability(technology.getPatentabilityAnalysis().isHasScalingCapability())
                .competitorsNational(technology.getMarketAnalysis().getCompetitorsNational())
                .competitorsInternational(technology.getMarketAnalysis().getCompetitorsInternational())
                .substituteTechnologies(technology.getMarketAnalysis().getSubstituteTechnologies())
                .marketSize(technology.getMarketAnalysis().getMarketSize())
                .applicationSectors(technology.getMarketAnalysis().getApplicationSectors())
                .preliminaryInterest(technology.getMarketAnalysis().getPreliminaryInterest())
                .transferMethod(technology.getTransferMethod())
                .lastUpdatedDate(technology.getLastUpdatedDate())
                .createdDate(technology.getCreatedDate())
                .status(technology.getStatus())
                .build();
    }

    public List<TechnologySummaryResponse> listAll() {
        return repository.findAll().stream()
                .map(tech -> TechnologySummaryResponse.builder()
                        .technologyId(tech.getId())
                        .resultName(tech.getResultName())
                        .responsibleGroup(tech.getResponsibleGroup())
                        .lastUpdatedDate(tech.getLastUpdatedDate())
                        .build())
                .collect(Collectors.toList()
                );
    }

    private static IntellectualProtection getIntellectualProtection(RegisterTechnologyRequest request) {
        return IntellectualProtection
                .builder()
                .hasCurrentProtection(request.isHasCurrentProtection())
                .suggestedProtectionType(request.getSuggestedProtectionType())
                .hasBeenDisclosed(request.isHasBeenDisclosed())
                .disclosedDate(request.getDisclosedDate())
                .build();
    }

    private static TechnicalDescription getTechnicalDescription(RegisterTechnologyRequest request) {
        return TechnicalDescription
                .builder()
                .functionalSummary(request.getFunctionalSummary())
                .problemSolved(request.getProblemSolved())
                .economicSector(request.getEconomicSector())
                .build();
    }

    private static PatentabilityAnalysis getPatentabilityAnalysis(RegisterTechnologyRequest request){
        return PatentabilityAnalysis
                .builder()
                .inventiveLevel(request.getInventiveLevel())
                .noveltyDescription(request.getNoveltyDescription())
                .hasIndustrialApplication(request.isHasIndustrialApplication())
                .teamAvailableForTransfer(request.isTeamAvailableForTransfer())
                .competitiveAdvantage(request.getCompetitiveAdvantage())
                .teamAvailableToCollaborate(request.isTeamAvailableToCollaborate())
                .hasScalingCapability(request.isHasScalingCapability())
                .build();
    }

    private static MarketAnalysis getMarketAnalysis(RegisterTechnologyRequest request){
        return MarketAnalysis
                .builder()
                .competitorsNational(request.getCompetitorsNational())
                .competitorsInternational(request.getCompetitorsInternational())
                .substituteTechnologies(request.getSubstituteTechnologies())
                .marketSize(request.getMarketSize())
                .applicationSectors(request.getApplicationSectors())
                .preliminaryInterest(request.getPreliminaryInterest())
                .build();
    }
}
