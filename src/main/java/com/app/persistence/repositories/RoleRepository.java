package com.app.persistence.repositories;

import com.app.persistence.entities.users.RoleEntity;
import com.app.persistence.entities.users.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    List<RoleEntity> findRoleEntitiesByRoleEnumIn(List<String> roleNames);
    RoleEntity findByRoleEnum(RoleEnum roleEnum);
    List<RoleEntity> findAll();
}
