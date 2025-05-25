package com.ucaldas.otri.domain.technologies.repositories;

import com.ucaldas.otri.domain.technologies.entities.Technology;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface TechnologiesRepository  extends ListCrudRepository<Technology, UUID> {

}
