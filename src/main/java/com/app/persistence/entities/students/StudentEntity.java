package com.app.persistence.entities.students;

import com.app.persistence.entities.groups.GroupEntity;
import com.app.persistence.entities.institutions.InstitutionEntity;
import com.app.persistence.entities.users.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email", name = "_email"),
        @UniqueConstraint(columnNames = "control_number", name = "_control_number")
})
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "control_number", unique = true)
    private int controlNumber;

    private String name;

    @Column(unique = true)
    private String email;

    private String program;

    private int semester;

    @Column(name = "shift")
    @Enumerated(EnumType.STRING)
    private ShiftEnum shift;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "create_at" , updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "update_at")
    private Date updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @JsonIgnore
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private InstitutionEntity institution;

    @JsonProperty("groupId")
    public Long getGroupId() {
        return group != null ? group.getId() : null;
    }

    @JsonProperty("username")
    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    @JsonProperty("institutionId")
    public String getInstitutionName() {
        return institution != null ? institution.getName() : null;
    }

}
