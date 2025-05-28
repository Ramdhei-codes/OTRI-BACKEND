package com.ucaldas.otri.infrastructure.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucaldas.otri.application.shared.services.IJSONService;
import org.springframework.stereotype.Service;

@Service
public class JSONService implements IJSONService {
    private final ObjectMapper objectMapper;

    public JSONService(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> T fromJson(String json, TypeReference<T> typeReference) throws JsonProcessingException {
        return objectMapper.readValue(json, typeReference);
    }
}
