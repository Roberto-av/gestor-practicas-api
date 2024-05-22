package com.app.persistence.entities.students;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

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

    private String semester;

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
}
