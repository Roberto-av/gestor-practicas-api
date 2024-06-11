package com.app.persistence.entities.groups;

import com.app.persistence.entities.institutions.StatusEnum;
import com.app.persistence.entities.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tittle;

    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusTaskEnum statusTask = StatusTaskEnum.DISABLE;

    @Column(name = "initial_date")
    private LocalDateTime initialDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @Builder.Default
    private List<FileEntity> files = new ArrayList<>();;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<SubmissionEntity> submissions;

    @PrePersist
    protected void onCreate() {
        if (statusTask == null) {
            statusTask = StatusTaskEnum.DISABLE;
        }
    }
}
