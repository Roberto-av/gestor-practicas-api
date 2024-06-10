package com.app.persistence.repositories;

import com.app.persistence.entities.groups.SubmissionEntity;
import com.app.persistence.entities.groups.TaskEntity;
import com.app.persistence.entities.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<SubmissionEntity, Long> {
    List<SubmissionEntity> findByTaskAndUser(TaskEntity task, UserEntity user);
    List<SubmissionEntity> findByTask(TaskEntity task);
}