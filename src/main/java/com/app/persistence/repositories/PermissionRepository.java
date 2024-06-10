package com.app.persistence.repositories;

import com.app.persistence.entities.users.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    PermissionEntity findByName(String name);
    List<PermissionEntity> findAllByNameIn(Set<String> permissionNames);
}
