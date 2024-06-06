package com.app.persistence.repositories;


import com.app.persistence.entities.institutions.InstitutionEntity;
import com.app.persistence.entities.students.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<InstitutionEntity, Long> {
    Optional<InstitutionEntity> findInstitutionByRfc(String rfc);
    Optional<InstitutionEntity> findInstitutionByCompanyName(String companyName);
    Optional<InstitutionEntity> findByName(String name);

}
