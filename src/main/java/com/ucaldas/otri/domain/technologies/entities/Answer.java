package com.ucaldas.otri.domain.technologies.entities;

import com.ucaldas.otri.domain.technologies.enums.ReadinessType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private boolean content;
    private boolean checked;
    private String question;
    private int level;
    private ReadinessType type;
    @ManyToOne
    @JoinColumn(name = "technologyId")
    private Technology technology;
}
