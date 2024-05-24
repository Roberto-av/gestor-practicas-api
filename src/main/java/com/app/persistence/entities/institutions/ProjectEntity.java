package com.app.persistence.entities.institutions;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "institution_project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String schedule;

    @Size(max = 255)
    private String justification;

    @Size(max = 255)
    private String objectives;

    @Size(max = 255)
    private String functions;

    @Column(name = "num_hours")
    private int numHours;

    private int requested;

    private int registered;

    @ManyToOne
    @JoinColumn(name = "institution_id", referencedColumnName = "id")
    private InstitutionEntity institution;
}
