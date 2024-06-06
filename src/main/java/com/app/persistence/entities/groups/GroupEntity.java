package com.app.persistence.entities.groups;

import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.entities.users.UserEntity;
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
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "group_entities")
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "create_at" , updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "update_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<StudentEntity> students;


    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TaskEntity> tasks;

    @ManyToMany(mappedBy = "groups", cascade = CascadeType.ALL)
    private List<UserEntity> users;

}
