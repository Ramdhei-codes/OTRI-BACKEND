package com.ucaldas.otri.domain.technologies.repositories;

import com.ucaldas.otri.domain.technologies.entities.Answer;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface AnswersRepository extends ListCrudRepository<Answer, UUID> {
}
