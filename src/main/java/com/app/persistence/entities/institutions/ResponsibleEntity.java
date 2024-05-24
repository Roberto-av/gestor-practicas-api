package com.app.persistence.entities.institutions;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "responsibles")
public class ResponsibleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String position;

    private String email;

    @Column(name = "cofirm_email")
    private String confirmEmail;

    private String phone;

    private String education;

    @OneToOne(mappedBy = "responsible")
    private InstitutionEntity institution;
}
