package com.ucaldas.otri.domain.technologies.entities;

import com.ucaldas.otri.domain.technologies.enums.ReadinessType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Questions")
public class Question {
    @Id
    private String id;
    private String content;
    private int level;
    private ReadinessType type;
}
