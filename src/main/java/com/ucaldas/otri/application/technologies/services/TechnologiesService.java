package com.ucaldas.otri.application.technologies.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ucaldas.otri.application.shared.exceptions.ApplicationException;
import com.ucaldas.otri.application.shared.exceptions.ErrorCodes;
import com.ucaldas.otri.application.shared.services.BuildPromptService;
import com.ucaldas.otri.application.shared.services.IAiService;
import com.ucaldas.otri.application.shared.services.IJSONService;
import com.ucaldas.otri.application.technologies.models.*;
import com.ucaldas.otri.domain.technologies.entities.*;
import com.ucaldas.otri.domain.technologies.enums.ReadinessType;
import com.ucaldas.otri.domain.technologies.enums.TechnologyStatus;
import com.ucaldas.otri.domain.technologies.repositories.AnswersRepository;
import com.ucaldas.otri.domain.technologies.repositories.QuestionsRepository;
import com.ucaldas.otri.domain.technologies.repositories.TechnologiesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TechnologiesService {
    private final TechnologiesRepository repository;
    private final QuestionsRepository questionsRepository;
    private final AnswersRepository answersRepository;
    private final IAiService aiService;

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

    public void updateTechnologyAnswers(List<ViewLevelAnswersResponse> answersResponseList){
        double percentageOfPositiveAnswersChecked = answersResponseList.stream()
                .mapToDouble(ans -> ans.isAnswer() && ans.isChecked() ? 1.0 : 0)
                .average()
                .orElse(0.0);

        if (percentageOfPositiveAnswersChecked >= 0.9){
            setReadinessLevel(answersResponseList);
        }
        for (ViewLevelAnswersResponse answersResponse : answersResponseList){
            Answer answer = answersRepository.findById(answersResponse.getId())
                    .orElseThrow(() -> new ApplicationException("No se encontraron resultados", ErrorCodes.VALIDATION_ERROR));
            answer.setChecked(answersResponse.isChecked());
            answer.setContent(answersResponse.isAnswer());
            answersRepository.save(answer);
        }
    }

    private void setReadinessLevel(List<ViewLevelAnswersResponse> answersResponseList) {
        ViewLevelAnswersResponse answer = answersResponseList.get(0);
        Technology technology = repository.findById(answer.getTechnologyId())
                .orElseThrow(() -> new ApplicationException("No se encontraron resultados", ErrorCodes.VALIDATION_ERROR));
        if (answer.getType() == ReadinessType.TRL){
            technology.getTechnicalDescription().setTrlLevel(answer.getLevel());
        } else{
            technology.getPatentabilityAnalysis().setCrlLevel(answer.getLevel());
        }
        technology.setLastUpdatedDate(new Date());
        repository.save(technology);
    }

    public List<ViewLevelAnswersResponse> getLevelAnswers(UUID technologyId, int level, ReadinessType type){
        Technology technology = repository.findById(technologyId).orElseThrow( () ->
                new ApplicationException(
                        "No se encontraron resultados",
                        ErrorCodes.VALIDATION_ERROR
                )
        );
        List<Question> levelQuestions = questionsRepository.findByLevelAndType(level, type);
        String evaluationPrompt = BuildPromptService.buildEvaluationPrompt(technology, level, type, levelQuestions);

        String evaluationResponse = aiService.chat(evaluationPrompt);

        return saveAndMapAnswers(technology, level, type, BuildPromptService.splitAnswersResponse(evaluationResponse));
    }

    public Map<Integer, List<ViewLevelAnswersResponse>> viewTechnologyAnswers(UUID technologyId, ReadinessType type){
        List<Answer> answers = answersRepository.findByTechnologyIdAndType(technologyId, type);

        return answers.stream()
                .map(this::mapAnswersToResponse)
                .collect(Collectors.groupingBy(ViewLevelAnswersResponse::getLevel));
    }

    public String getRecommendedActions(UUID technologyId){
        Technology technology = repository.findById(technologyId).orElseThrow( () ->
                new ApplicationException(
                        "No se encontraron resultados",
                        ErrorCodes.VALIDATION_ERROR
                )
        );

        String recommendedActions = aiService.chat(BuildPromptService.buildRecommendationsPrompt(technology));
        technology.setRecommendedActions(recommendedActions);
        repository.save(technology);
        return recommendedActions;
    }

    private ViewLevelAnswersResponse mapAnswersToResponse(Answer answer){
        return ViewLevelAnswersResponse.builder()
                .id(answer.getId())
                .technologyId(answer.getTechnology().getId())
                .question(answer.getQuestion())
                .answer(answer.isContent())
                .checked(answer.isChecked())
                .level(answer.getLevel())
                .type(answer.getType())
                .build();
    }

    private List<ViewLevelAnswersResponse> saveAndMapAnswers(Technology technology, int level, ReadinessType type, String[] answers){
        List<Answer> result = new ArrayList<>();
        answersRepository.deleteByTechnologyIdAndLevelAndType(technology.getId(), level, type);
        for (String answer : answers){
            String[] splittedAnswer = answer.split(";");
            result.add(Answer
                            .builder()
                            .technology(technology)
                            .content(splittedAnswer[1].trim().equalsIgnoreCase("Sí"))
                            .checked(false)
                            .question(splittedAnswer[0].trim())
                            .level(level)
                            .type(type)
                            .build());
        }
        return MapAnswersForResponse(answersRepository.saveAll(result));
    }

    private List<ViewLevelAnswersResponse> MapAnswersForResponse(List<Answer> answers){
        List<ViewLevelAnswersResponse> responseList = new ArrayList<>();
        for (Answer answer : answers){
            responseList.add(ViewLevelAnswersResponse
                    .builder()
                            .id(answer.getId())
                            .technologyId(answer.getTechnology().getId())
                            .question(answer.getQuestion())
                            .answer(answer.isContent())
                            .checked(answer.isChecked())
                            .level(answer.getLevel())
                            .type(answer.getType())
                    .build());
        }
        return  responseList;
    }

    public void deleteTechnology(UUID id) {
        Technology technology = repository.findById(id).orElseThrow(() ->
                new ApplicationException("Tecnología no encontrada", ErrorCodes.VALIDATION_ERROR));
        repository.delete(technology);
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
