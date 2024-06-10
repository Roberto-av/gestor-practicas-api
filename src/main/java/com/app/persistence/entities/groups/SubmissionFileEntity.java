package com.app.persistence.entities.groups;

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
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "submission_files")
public class SubmissionFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "submission_id")
    private SubmissionEntity submission;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "submitted_at", updatable = false)
    private Date submittedAt;
}
