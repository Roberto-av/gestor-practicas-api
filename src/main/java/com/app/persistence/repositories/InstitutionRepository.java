package com.app.persistence.repositories;


import com.app.persistence.entities.institutions.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<InstitutionEntity, Long> {

}
