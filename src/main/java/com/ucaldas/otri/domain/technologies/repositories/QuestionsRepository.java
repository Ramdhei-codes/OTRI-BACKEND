package com.ucaldas.otri.domain.technologies.repositories;

import com.ucaldas.otri.domain.technologies.entities.Question;
import com.ucaldas.otri.domain.technologies.enums.ReadinessType;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface QuestionsRepository extends ListCrudRepository<Question, String> {
    List<Question> findByLevelAndType(int level, ReadinessType type);
}
