package com.app.persistence.entities.institutions;

import com.app.persistence.entities.students.StudentEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "institutions", uniqueConstraints = {
        @UniqueConstraint(columnNames = "rfc", name = "_rfc"),
        @UniqueConstraint(columnNames = "company_name", name = "_company_name")
})
public class InstitutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String rfc;

    @Column(name = "company_name", unique = true)
    private String companyName;

    private String giro;

    private String web;

    private boolean support;

    @Column(name = "sector")
    @Enumerated(EnumType.STRING)
    private SectorEnum sector;

    @Column(name = "modality")
    @Enumerated(EnumType.STRING)
    private ModalityEnum modality;

    @Column(name = "telefone_number")
    private String telephoneNumber;

    @Enumerated(EnumType.STRING)
    private StatusEnum status = StatusEnum.PENDING;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "create_at" , updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "update_at")
    private Date updatedAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressEntity address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "responsible_id", referencedColumnName = "id")
    private ResponsibleEntity responsible;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectEntity> projects;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StudentEntity> students = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = StatusEnum.PENDING;
        }
    }


}
