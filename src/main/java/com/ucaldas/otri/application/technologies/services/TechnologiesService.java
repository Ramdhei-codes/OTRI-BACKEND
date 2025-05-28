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

    public ViewTechnologyResponse view(UUID technologyId) {
        Technology technology = repository.findById(technologyId)
                .orElseThrow(() -> new ApplicationException("No se encontraron resultados", ErrorCodes.VALIDATION_ERROR));

        TechnicalDescription td = technology.getTechnicalDescription();
        IntellectualProtection ip = technology.getIntellectualProtection();
        PatentabilityAnalysis pa = technology.getPatentabilityAnalysis();
        MarketAnalysis ma = technology.getMarketAnalysis();

        return ViewTechnologyResponse.builder()
                .technologyId(technology.getId())
                .resultName(technology.getResultName())
                .responsibleGroup(technology.getResponsibleGroup())
                .functionalSummary(td != null ? td.getFunctionalSummary() : null)
                .problemSolved(td != null ? td.getProblemSolved() : null)
                .trlLevel(td != null ? td.getTrlLevel() : 0)
                .economicSector(td != null ? td.getEconomicSector() : null)
                .hasCurrentProtection(ip != null && ip.isHasCurrentProtection())
                .suggestedProtectionType(ip != null ? ip.getSuggestedProtectionType() : null)
                .hasBeenDisclosed(ip != null && ip.isHasBeenDisclosed())
                .disclosedDate(ip != null ? ip.getDisclosedDate() : null)
                .inventiveLevel(pa != null && pa.getInventiveLevel() != null ? pa.getInventiveLevel().ordinal() : null)
                .noveltyDescription(pa != null ? pa.getNoveltyDescription() : null)
                .hasIndustrialApplication(pa != null && pa.isHasIndustrialApplication())
                .teamAvailableForTransfer(pa != null && pa.isTeamAvailableForTransfer())
                .competitiveAdvantage(pa != null ? pa.getCompetitiveAdvantage() : null)
                .crlLevel(pa != null ? pa.getCrlLevel() : 0)
                .teamAvailableToCollaborate(pa != null && pa.isTeamAvailableToCollaborate())
                .hasScalingCapability(pa != null && pa.isHasScalingCapability())
                .competitorsNational(ma != null ? ma.getCompetitorsNational() : null)
                .competitorsInternational(ma != null ? ma.getCompetitorsInternational() : null)
                .substituteTechnologies(ma != null ? ma.getSubstituteTechnologies() : null)
                .marketSize(ma != null ? ma.getMarketSize() : null)
                .applicationSectors(ma != null ? ma.getApplicationSectors() : null)
                .preliminaryInterest(ma != null && ma.getPreliminaryInterest() != null ? ma.getPreliminaryInterest().ordinal() : null)
                .transferMethod(technology.getTransferMethod())
                .recommendedActions(technology.getRecommendedActions())
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
                        .status(tech.getStatus())
                        .build())
                .collect(Collectors.toList()
                );
    }

    public void updateTechnology(UUID id, RegisterTechnologyRequest request) {
        Technology technology = repository.findById(id).orElseThrow(() ->
                new ApplicationException("Tecnología no encontrada", ErrorCodes.VALIDATION_ERROR));

        technology.setResultName(request.getResultName());
        technology.setResponsibleGroup(request.getResponsibleGroup());
        technology.setTransferMethod(request.getTransferMethod());
        technology.setRecommendedActions(request.getRecommendedActions());
        technology.setLastUpdatedDate(new Date());

        technology.setTechnicalDescription(getTechnicalDescription(request));
        technology.setIntellectualProtection(getIntellectualProtection(request));
        technology.setPatentabilityAnalysis(getPatentabilityAnalysis(request));
        technology.setMarketAnalysis(getMarketAnalysis(request));

        repository.save(technology);
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
