package com.ucaldas.otri;

import com.ucaldas.otri.application.shared.exceptions.ApplicationException;
import com.ucaldas.otri.application.shared.exceptions.ErrorCodes;
import com.ucaldas.otri.application.shared.services.BuildPromptService;
import com.ucaldas.otri.application.shared.services.IAiService;
import com.ucaldas.otri.application.shared.services.IJSONService;
import com.ucaldas.otri.application.technologies.models.RegisterTechnologyRequest;
import com.ucaldas.otri.application.technologies.models.TechnologySummaryResponse;
import com.ucaldas.otri.application.technologies.models.ViewLevelAnswersResponse;
import com.ucaldas.otri.application.technologies.models.ViewTechnologyResponse;
import com.ucaldas.otri.application.technologies.services.TechnologiesService;
import com.ucaldas.otri.domain.technologies.entities.*;
import com.ucaldas.otri.domain.technologies.enums.InventiveLevel;
import com.ucaldas.otri.domain.technologies.enums.PreliminaryInterest;
import com.ucaldas.otri.domain.technologies.enums.ReadinessType;
import com.ucaldas.otri.domain.technologies.enums.TechnologyStatus;
import com.ucaldas.otri.domain.technologies.repositories.AnswersRepository;
import com.ucaldas.otri.domain.technologies.repositories.QuestionsRepository;
import com.ucaldas.otri.domain.technologies.repositories.TechnologiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TechnologiesServiceTests {

    @Mock
    private TechnologiesRepository repository;

    @Mock
    private QuestionsRepository questionsRepository;

    @Mock
    private AnswersRepository answersRepository;

    @Mock
    private IAiService aiService;

    @InjectMocks
    private TechnologiesService technologiesService;

    private UUID technologyId;
    private Technology technology;
    private RegisterTechnologyRequest request;

    @BeforeEach
    void setUp() {
        technologyId = UUID.randomUUID();

        request = RegisterTechnologyRequest.builder()
                .resultName("Test Technology")
                .responsibleGroup("Test Group")
                .functionalSummary("Test Summary")
                .problemSolved("Test Problem")
                .economicSector("Test Sector")
                .hasCurrentProtection(true)
                .suggestedProtectionType("Patent")
                .hasBeenDisclosed(false)
                .disclosedDate(new Date())
                .inventiveLevel(InventiveLevel.HIGH)
                .noveltyDescription("Novel technology")
                .hasIndustrialApplication(true)
                .teamAvailableForTransfer(true)
                .competitiveAdvantage("Competitive edge")
                .teamAvailableToCollaborate(true)
                .hasScalingCapability(true)
                .competitorsNational("National competitors")
                .competitorsInternational("International competitors")
                .substituteTechnologies("Substitute tech")
                .marketSize("Large market")
                .applicationSectors("Multiple sectors")
                .preliminaryInterest(PreliminaryInterest.YES)
                .transferMethod("Licensing")
                .recommendedActions("Test actions")
                .build();

        technology = Technology.builder()
                .id(technologyId)
                .resultName("Test Technology")
                .responsibleGroup("Test Group")
                .technicalDescription(TechnicalDescription.builder()
                        .functionalSummary("Test Summary")
                        .problemSolved("Test Problem")
                        .economicSector("Test Sector")
                        .trlLevel(5)
                        .build())
                .intellectualProtection(IntellectualProtection.builder()
                        .hasCurrentProtection(true)
                        .suggestedProtectionType("Patent")
                        .hasBeenDisclosed(false)
                        .disclosedDate(new Date())
                        .build())
                .patentabilityAnalysis(PatentabilityAnalysis.builder()
                        .inventiveLevel(InventiveLevel.HIGH)
                        .noveltyDescription("Novel technology")
                        .hasIndustrialApplication(true)
                        .teamAvailableForTransfer(true)
                        .competitiveAdvantage("Competitive edge")
                        .crlLevel(3)
                        .teamAvailableToCollaborate(true)
                        .hasScalingCapability(true)
                        .build())
                .marketAnalysis(MarketAnalysis.builder()
                        .competitorsNational("National competitors")
                        .competitorsInternational("International competitors")
                        .substituteTechnologies("Substitute tech")
                        .marketSize("Large market")
                        .applicationSectors("Multiple sectors")
                        .preliminaryInterest(PreliminaryInterest.YES)
                        .build())
                .transferMethod("Licensing")
                .recommendedActions("Test actions")
                .createdDate(new Date())
                .lastUpdatedDate(new Date())
                .status(TechnologyStatus.ACTIVE)
                .build();
    }

    @Test
    void register_ShouldCreateTechnologySuccessfully() {
        // Arrange
        when(repository.save(any(Technology.class))).thenReturn(technology);

        // Act
        String result = technologiesService.register(request);

        // Assert
        assertNotNull(result);
        assertEquals(technologyId.toString(), result);
        verify(repository).save(any(Technology.class));
    }

    @Test
    void view_ShouldReturnTechnologyDetails_WhenTechnologyExists() {
        // Arrange
        when(repository.findById(technologyId)).thenReturn(Optional.of(technology));

        // Act
        ViewTechnologyResponse result = technologiesService.view(technologyId);

        // Assert
        assertNotNull(result);
        assertEquals(technologyId, result.getTechnologyId());
        assertEquals("Test Technology", result.getResultName());
        assertEquals("Test Group", result.getResponsibleGroup());
        assertEquals("Test Summary", result.getFunctionalSummary());
        assertEquals("Test Problem", result.getProblemSolved());
        assertEquals(5, result.getTrlLevel());
        assertEquals("Test Sector", result.getEconomicSector());
        assertTrue(result.isHasCurrentProtection());
        assertEquals("Patent", result.getSuggestedProtectionType());
        assertFalse(result.isHasBeenDisclosed());
        assertEquals(InventiveLevel.HIGH.ordinal(), result.getInventiveLevel());
        assertEquals("Novel technology", result.getNoveltyDescription());
        assertTrue(result.isHasIndustrialApplication());
        assertTrue(result.isTeamAvailableForTransfer());
        assertEquals("Competitive edge", result.getCompetitiveAdvantage());
        assertEquals(3, result.getCrlLevel());
        assertTrue(result.isTeamAvailableToCollaborate());
        assertTrue(result.isHasScalingCapability());
        assertEquals("National competitors", result.getCompetitorsNational());
        assertEquals("International competitors", result.getCompetitorsInternational());
        assertEquals("Substitute tech", result.getSubstituteTechnologies());
        assertEquals("Large market", result.getMarketSize());
        assertEquals("Multiple sectors", result.getApplicationSectors());
        assertEquals(PreliminaryInterest.YES.ordinal(), result.getPreliminaryInterest());
        assertEquals("Licensing", result.getTransferMethod());
        assertEquals("Test actions", result.getRecommendedActions());
        assertEquals(TechnologyStatus.ACTIVE, result.getStatus());

        verify(repository).findById(technologyId);
    }

    @Test
    void view_ShouldThrowException_WhenTechnologyNotFound() {
        // Arrange
        when(repository.findById(technologyId)).thenReturn(Optional.empty());

        // Act & Assert
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> technologiesService.view(technologyId));

        assertEquals("No se encontraron resultados", exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_ERROR, exception.getErrorCode());
        verify(repository).findById(technologyId);
    }

    @Test
    void view_ShouldHandleNullNestedObjects() {
        // Arrange
        Technology technologyWithNulls = Technology.builder()
                .id(technologyId)
                .resultName("Test Technology")
                .responsibleGroup("Test Group")
                .transferMethod("Licensing")
                .createdDate(new Date())
                .status(TechnologyStatus.ACTIVE)
                .build();

        when(repository.findById(technologyId)).thenReturn(Optional.of(technologyWithNulls));

        // Act
        ViewTechnologyResponse result = technologiesService.view(technologyId);

        // Assert
        assertNotNull(result);
        assertNull(result.getFunctionalSummary());
        assertNull(result.getProblemSolved());
        assertEquals(0, result.getTrlLevel());
        assertNull(result.getEconomicSector());
        assertFalse(result.isHasCurrentProtection());
        assertNull(result.getSuggestedProtectionType());
        assertFalse(result.isHasBeenDisclosed());
        assertNull(result.getDisclosedDate());
        assertNull(result.getInventiveLevel());
        assertNull(result.getNoveltyDescription());
        assertFalse(result.isHasIndustrialApplication());
        assertFalse(result.isTeamAvailableForTransfer());
        assertNull(result.getCompetitiveAdvantage());
        assertEquals(0, result.getCrlLevel());
        assertFalse(result.isTeamAvailableToCollaborate());
        assertFalse(result.isHasScalingCapability());
        assertNull(result.getCompetitorsNational());
        assertNull(result.getCompetitorsInternational());
        assertNull(result.getSubstituteTechnologies());
        assertNull(result.getMarketSize());
        assertNull(result.getApplicationSectors());
        assertNull(result.getPreliminaryInterest());
    }

    @Test
    void listAll_ShouldReturnAllTechnologies() {
        // Arrange
        List<Technology> technologies = Arrays.asList(technology);
        when(repository.findAll()).thenReturn(technologies);

        // Act
        List<TechnologySummaryResponse> result = technologiesService.listAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        TechnologySummaryResponse summary = result.get(0);
        assertEquals(technologyId, summary.getTechnologyId());
        assertEquals("Test Technology", summary.getResultName());
        assertEquals("Test Group", summary.getResponsibleGroup());
        assertEquals(TechnologyStatus.ACTIVE, summary.getStatus());

        verify(repository).findAll();
    }

    @Test
    void updateTechnology_ShouldUpdateTechnologySuccessfully() {
        // Arrange
        when(repository.findById(technologyId)).thenReturn(Optional.of(technology));
        when(repository.save(any(Technology.class))).thenReturn(technology);

        // Act
        technologiesService.updateTechnology(technologyId, request);

        // Assert
        verify(repository).findById(technologyId);
        verify(repository).save(any(Technology.class));
    }

    @Test
    void updateTechnology_ShouldThrowException_WhenTechnologyNotFound() {
        // Arrange
        when(repository.findById(technologyId)).thenReturn(Optional.empty());

        // Act & Assert
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> technologiesService.updateTechnology(technologyId, request));

        assertEquals("Tecnología no encontrada", exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_ERROR, exception.getErrorCode());
        verify(repository).findById(technologyId);
        verify(repository, never()).save(any(Technology.class));
    }

    @Test
    void updateTechnologyAnswers_ShouldUpdateAnswersWithoutLevelUpdate_WhenPercentageBelow90() {
        // Arrange
        List<ViewLevelAnswersResponse> answersResponseList = Arrays.asList(
                ViewLevelAnswersResponse.builder()
                        .id(UUID.randomUUID())
                        .technologyId(technologyId)
                        .answer(true)
                        .checked(true)
                        .level(1)
                        .type(ReadinessType.TRL)
                        .build(),
                ViewLevelAnswersResponse.builder()
                        .id(UUID.randomUUID())
                        .technologyId(technologyId)
                        .answer(false)
                        .checked(true)
                        .level(1)
                        .type(ReadinessType.TRL)
                        .build()
        );

        Answer answer1 = Answer.builder().id(answersResponseList.get(0).getId()).build();
        Answer answer2 = Answer.builder().id(answersResponseList.get(1).getId()).build();

        when(answersRepository.findById(answersResponseList.get(0).getId())).thenReturn(Optional.of(answer1));
        when(answersRepository.findById(answersResponseList.get(1).getId())).thenReturn(Optional.of(answer2));
        when(answersRepository.save(any(Answer.class))).thenReturn(answer1);

        // Act
        technologiesService.updateTechnologyAnswers(answersResponseList);

        // Assert
        verify(answersRepository, times(2)).findById(any(UUID.class));
        verify(answersRepository, times(2)).save(any(Answer.class));
        verify(repository, never()).findById(technologyId);
    }

    @Test
    void updateTechnologyAnswers_ShouldUpdateLevelAndAnswers_WhenPercentage90OrAbove() {
        // Arrange
        List<ViewLevelAnswersResponse> answersResponseList = Arrays.asList(
                ViewLevelAnswersResponse.builder()
                        .id(UUID.randomUUID())
                        .technologyId(technologyId)
                        .answer(true)
                        .checked(true)
                        .level(5)
                        .type(ReadinessType.TRL)
                        .build()
        );

        Answer answer = Answer.builder().id(answersResponseList.get(0).getId()).build();

        when(answersRepository.findById(answersResponseList.get(0).getId())).thenReturn(Optional.of(answer));
        when(answersRepository.save(any(Answer.class))).thenReturn(answer);
        when(repository.findById(technologyId)).thenReturn(Optional.of(technology));
        when(repository.save(any(Technology.class))).thenReturn(technology);

        // Act
        technologiesService.updateTechnologyAnswers(answersResponseList);

        // Assert
        verify(answersRepository).findById(any(UUID.class));
        verify(answersRepository).save(any(Answer.class));
        verify(repository).findById(technologyId);
        verify(repository).save(any(Technology.class));
    }

    @Test
    void updateTechnologyAnswers_ShouldThrowException_WhenAnswerNotFound() {
        // Arrange
        List<ViewLevelAnswersResponse> answersResponseList = Arrays.asList(
                ViewLevelAnswersResponse.builder()
                        .id(UUID.randomUUID())
                        .technologyId(technologyId)
                        .answer(true)
                        .checked(true)
                        .build()
        );

        // Act & Assert
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> technologiesService.updateTechnologyAnswers(answersResponseList));

        assertEquals("No se encontraron resultados", exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_ERROR, exception.getErrorCode());
    }

    @Test
    void getLevelAnswers_ShouldReturnAnswers_WhenTechnologyExists() {
        // Arrange
        int level = 1;
        ReadinessType type = ReadinessType.TRL;
        List<Question> questions = Arrays.asList(
                Question.builder().id(String.valueOf(UUID.randomUUID())).content("Question 1").level(level).type(type).build()
        );

        when(repository.findById(technologyId)).thenReturn(Optional.of(technology));
        when(questionsRepository.findByLevelAndType(level, type)).thenReturn(questions);
        when(aiService.chat(anyString())).thenReturn("Question 1; Sí; Entorno probado");

        try (MockedStatic<BuildPromptService> mockBuildPromptService = mockStatic(BuildPromptService.class)) {
            mockBuildPromptService.when(() -> BuildPromptService.buildEvaluationPrompt(any(), anyInt(), any(), any()))
                    .thenReturn("evaluation prompt");
            mockBuildPromptService.when(() -> BuildPromptService.splitAnswersResponse(anyString()))
                    .thenReturn(new String[]{"Question 1; Sí; Entorno probado"});

            when(answersRepository.saveAll(anyList())).thenReturn(Arrays.asList(
                    Answer.builder()
                            .id(UUID.randomUUID())
                            .technology(technology)
                            .question("Question 1")
                            .content(true)
                            .checked(false)
                            .level(level)
                            .type(type)
                            .build()
            ));

            // Act
            List<ViewLevelAnswersResponse> result = technologiesService.getLevelAnswers(technologyId, level, type);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Question 1", result.get(0).getQuestion());
            assertTrue(result.get(0).isAnswer());
            assertFalse(result.get(0).isChecked());
            assertEquals(level, result.get(0).getLevel());
            assertEquals(type, result.get(0).getType());

            verify(repository).findById(technologyId);
            verify(questionsRepository).findByLevelAndType(level, type);
            verify(aiService).chat(anyString());
            verify(answersRepository).deleteByTechnologyIdAndLevelAndType(technologyId, level, type);
            verify(answersRepository).saveAll(anyList());
        }
    }

    @Test
    void getLevelAnswers_ShouldThrowException_WhenTechnologyNotFound() {
        // Arrange
        when(repository.findById(technologyId)).thenReturn(Optional.empty());

        // Act & Assert
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> technologiesService.getLevelAnswers(technologyId, 1, ReadinessType.TRL));

        assertEquals("No se encontraron resultados", exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_ERROR, exception.getErrorCode());
    }

    @Test
    void viewTechnologyAnswers_ShouldReturnGroupedAnswers() {
        // Arrange
        ReadinessType type = ReadinessType.TRL;
        List<Answer> answers = Arrays.asList(
                Answer.builder()
                        .id(UUID.randomUUID())
                        .technology(technology)
                        .question("Question 1")
                        .content(true)
                        .checked(true)
                        .level(1)
                        .type(type)
                        .build(),
                Answer.builder()
                        .id(UUID.randomUUID())
                        .technology(technology)
                        .question("Question 2")
                        .content(false)
                        .checked(false)
                        .level(2)
                        .type(type)
                        .build()
        );

        when(answersRepository.findByTechnologyIdAndType(technologyId, type)).thenReturn(answers);

        // Act
        Map<Integer, List<ViewLevelAnswersResponse>> result = technologiesService.viewTechnologyAnswers(technologyId, type);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(1));
        assertTrue(result.containsKey(2));
        assertEquals(1, result.get(1).size());
        assertEquals(1, result.get(2).size());

        verify(answersRepository).findByTechnologyIdAndType(technologyId, type);
    }

    @Test
    void getRecommendedActions_ShouldReturnRecommendations_WhenTechnologyExists() {
        // Arrange
        String expectedRecommendations = "Recommended actions from AI";
        when(repository.findById(technologyId)).thenReturn(Optional.of(technology));
        when(aiService.chat(anyString())).thenReturn(expectedRecommendations);
        when(repository.save(any(Technology.class))).thenReturn(technology);

        try (MockedStatic<BuildPromptService> mockBuildPromptService = mockStatic(BuildPromptService.class)) {
            mockBuildPromptService.when(() -> BuildPromptService.buildRecommendationsPrompt(any()))
                    .thenReturn("recommendations prompt");

            // Act
            String result = technologiesService.getRecommendedActions(technologyId);

            // Assert
            assertEquals(expectedRecommendations, result);
            verify(repository).findById(technologyId);
            verify(aiService).chat(anyString());
            verify(repository).save(any(Technology.class));
        }
    }

    @Test
    void getRecommendedActions_ShouldThrowException_WhenTechnologyNotFound() {
        // Arrange
        when(repository.findById(technologyId)).thenReturn(Optional.empty());

        // Act & Assert
        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> technologiesService.getRecommendedActions(technologyId));

        assertEquals("No se encontraron resultados", exception.getMessage());
        assertEquals(ErrorCodes.VALIDATION_ERROR, exception.getErrorCode());
    }
}
