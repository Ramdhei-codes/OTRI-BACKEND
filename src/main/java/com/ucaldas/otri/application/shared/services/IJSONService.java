package com.ucaldas.otri.application.shared.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

public interface IJSONService {
    <T> T fromJson(String json, TypeReference<T> typeReference) throws JsonProcessingException;
}
