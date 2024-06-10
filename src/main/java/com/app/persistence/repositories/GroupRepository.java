package com.app.persistence.repositories;

import com.app.persistence.entities.groups.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    GroupEntity findByName(String name);
    List<GroupEntity> findByNameAndDescription(String name, String description);

}
