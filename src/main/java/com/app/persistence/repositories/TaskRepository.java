package com.app.persistence.repositories;

import com.app.persistence.entities.groups.GroupEntity;
import com.app.persistence.entities.groups.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    TaskEntity findFirstByTittleAndDescriptionAndGroup(String title, String description, GroupEntity group);
}
