package com.ucaldas.otri.domain.technologies.repositories;

import com.ucaldas.otri.domain.technologies.entities.Answer;
import com.ucaldas.otri.domain.technologies.enums.ReadinessType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface AnswersRepository extends ListCrudRepository<Answer, UUID> {

    @Transactional
    @Modifying
    void deleteByTechnologyIdAndLevelAndType(UUID technologyId, int level, ReadinessType type);

    List<Answer> findByTechnologyIdAndType(UUID technologyId, ReadinessType type);
}
