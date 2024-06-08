package com.app.persistence.repositories;

import com.app.persistence.entities.groups.SubmissionFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionFileRepository extends JpaRepository<SubmissionFileEntity, Long> {
}
