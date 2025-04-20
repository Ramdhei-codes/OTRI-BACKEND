package com.ucaldas.otri.domain.users.repositories;

import com.ucaldas.otri.domain.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
