package com.app.persistence.entities.groups;

import com.app.persistence.entities.users.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
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

    @Column(name = "initial_date")
    private Date initialDate;

    @Column(name = "end_date")
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<FileEntity> files;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupEntity group;
}
