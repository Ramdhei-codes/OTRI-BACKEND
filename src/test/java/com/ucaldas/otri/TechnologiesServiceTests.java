package com.ucaldas.otri;

import com.ucaldas.otri.application.shared.exceptions.ApplicationException;
import com.ucaldas.otri.application.shared.services.IAiService;
import com.ucaldas.otri.application.shared.services.IJSONService;
import com.ucaldas.otri.application.technologies.models.RegisterTechnologyRequest;
import com.ucaldas.otri.application.technologies.models.TechnologySummaryResponse;
import com.ucaldas.otri.application.technologies.models.ViewTechnologyResponse;
import com.ucaldas.otri.application.technologies.services.TechnologiesService;
import com.ucaldas.otri.domain.technologies.entities.*;
import com.ucaldas.otri.domain.technologies.enums.InventiveLevel;
import com.ucaldas.otri.domain.technologies.enums.PreliminaryInterest;
import com.ucaldas.otri.domain.technologies.enums.TechnologyStatus;
import com.ucaldas.otri.domain.technologies.repositories.AnswersRepository;
import com.ucaldas.otri.domain.technologies.repositories.QuestionsRepository;
import com.ucaldas.otri.domain.technologies.repositories.TechnologiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TechnologiesServiceTests {
    @Mock
    private TechnologiesRepository repository;

    @Mock
    private QuestionsRepository questionsRepository;
    @Mock
    private AnswersRepository answersRepository;

    @Captor
    private ArgumentCaptor<Technology> technologyArgumentCaptor;

    @Mock
    private IAiService aiService;

    @InjectMocks
    private TechnologiesService service;

    @BeforeEach
    void setUp(){
        service = new TechnologiesService(repository, questionsRepository, answersRepository, aiService);
    }

    @Test
    void register_ShouldCreateNewTechnology(){

        RegisterTechnologyRequest request = RegisterTechnologyRequest
                .builder()
                .resultName("Rizoar")
                .build();

        Technology savedTechnologyMock = Technology.builder()
                .id(UUID.randomUUID()) // simulate what would happen in a real DB save
                .resultName("Rizoar")
                .build();

        when(repository.save(Mockito.any(Technology.class))).thenReturn(savedTechnologyMock);

        String result = service.register(request);

        verify(repository).save(technologyArgumentCaptor.capture());
        Technology savedTechnology = technologyArgumentCaptor.getValue();

        assertEquals("Rizoar", savedTechnology.getResultName());
    }

    @Test
    void view_ShouldReturnResponse_WhenTechnologyExists() {
        UUID techId = UUID.randomUUID();

        TechnicalDescription technicalDescription = TechnicalDescription.builder()
                .functionalSummary("Summary")
                .problemSolved("Problem")
                .trlLevel(6)
                .economicSector("Sector")
                .build();

        IntellectualProtection intellectualProtection = IntellectualProtection.builder()
                .hasCurrentProtection(true)
                .suggestedProtectionType("Patent")
                .hasBeenDisclosed(false)
                .disclosedDate(null)
                .build();

        PatentabilityAnalysis patentabilityAnalysis = PatentabilityAnalysis.builder()
                .inventiveLevel(InventiveLevel.HIGH)
                .noveltyDescription("Unique tech")
                .hasIndustrialApplication(true)
                .teamAvailableForTransfer(true)
                .competitiveAdvantage("Strong")
                .crlLevel(3)
                .teamAvailableToCollaborate(true)
                .hasScalingCapability(true)
                .build();

        MarketAnalysis marketAnalysis = MarketAnalysis.builder()
                .competitorsNational("None")
                .competitorsInternational("Few")
                .substituteTechnologies("None known")
                .marketSize("Large")
                .applicationSectors("Healthcare")
                .preliminaryInterest(PreliminaryInterest.YES)
                .build();

        Technology mockTechnology = Technology.builder()
                .id(techId)
                .resultName("TechX")
                .responsibleGroup("Group A")
                .technicalDescription(technicalDescription)
                .intellectualProtection(intellectualProtection)
                .patentabilityAnalysis(patentabilityAnalysis)
                .marketAnalysis(marketAnalysis)
                .transferMethod("Licensing")
                .createdDate(new Date())
                .status(TechnologyStatus.ACTIVE)
                .build();

        when(repository.findById(techId)).thenReturn(Optional.of(mockTechnology));

        ViewTechnologyResponse response = service.view(techId);

        assertEquals("TechX", response.getResultName());
        assertEquals("Summary", response.getFunctionalSummary());
    }

    @Test
    void update_ShouldModifyExistingTechnology() {
        UUID technologyId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        Technology existingTechnology = Technology.builder()
                .id(technologyId)
                .resultName("Nombre original")
                .build();

        RegisterTechnologyRequest request = new RegisterTechnologyRequest();
        request.setResultName("Nombre actualizado");
        request.setResponsibleGroup("Grupo X");

        when(repository.findById(technologyId)).thenReturn(Optional.of(existingTechnology));

        service.updateTechnology(technologyId, request);

        verify(repository).save(technologyArgumentCaptor.capture());
        Technology updatedTechnology = technologyArgumentCaptor.getValue();

        assertEquals("Nombre actualizado", updatedTechnology.getResultName());
        assertEquals("Grupo X", updatedTechnology.getResponsibleGroup());
    }

    @Test
    void update_ShouldThrow_WhenTechnologyNotFound() {
        UUID technologyId = UUID.randomUUID();
        RegisterTechnologyRequest request = new RegisterTechnologyRequest();
        when(repository.findById(technologyId)).thenReturn(Optional.empty());

        ApplicationException exception = org.junit.jupiter.api.Assertions.assertThrows(
                ApplicationException.class,
                () -> service.updateTechnology(technologyId, request)
        );

        assertEquals("Tecnología no encontrada", exception.getMessage());
    }

    @Test
    void listAll_ShouldReturnSummaryList() {
        UUID techId = UUID.randomUUID();
        Technology tech = Technology.builder()
                .id(techId)
                .resultName("Tech 1")
                .responsibleGroup("Grupo A")
                .lastUpdatedDate(new Date())
                .status(TechnologyStatus.ACTIVE)
                .build();

        when(repository.findAll()).thenReturn(List.of(tech));

        List<TechnologySummaryResponse> result = service.listAll();
        assertEquals(1, result.size());
        TechnologySummaryResponse summary = result.get(0);
        assertEquals(techId, summary.getTechnologyId());
        assertEquals("Tech 1", summary.getResultName());
        assertEquals("Grupo A", summary.getResponsibleGroup());
        assertEquals(TechnologyStatus.ACTIVE, summary.getStatus());
    }
}
