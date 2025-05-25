package com.ucaldas.otri;

import com.ucaldas.otri.application.technologies.models.RegisterTechnologyRequest;
import com.ucaldas.otri.application.technologies.models.ViewTechnologyResponse;
import com.ucaldas.otri.application.technologies.services.TechnologiesService;
import com.ucaldas.otri.domain.technologies.entities.Technology;
import com.ucaldas.otri.domain.technologies.repositories.TechnologiesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TechnologiesServiceTests {
    @Mock
    private TechnologiesRepository repository;

    @InjectMocks
    private TechnologiesService service;

    @Captor
    private ArgumentCaptor<Technology> technologyArgumentCaptor;

    @BeforeEach
    void setUp(){
        service = new TechnologiesService(repository);
    }

    @Test
    void register_ShouldCreateNewTechnology(){
        RegisterTechnologyRequest request = new RegisterTechnologyRequest();
        request.setResultName("Rizoar");

        String result = service.register(request);

        verify(repository).save(technologyArgumentCaptor.capture());
        Technology savedTechnology = technologyArgumentCaptor.getValue();

        assertEquals("Rizoar", savedTechnology.getResultName());
    }

    @Test
    void view_ShouldReturnResponse_WhenTechnologyExists(){
        UUID technologyId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        Technology technology = Technology.builder()
                .id(technologyId)
                .resultName("Rizoar")
                .build();
        when(repository.findById(technologyId)).thenReturn(
                Optional.of(technology)
        );

        ViewTechnologyResponse response = service.view(technologyId);

        assertEquals(technologyId, response.getTechnologyId());
        assertEquals("Rizoar", response.getResultName());
    }
}
