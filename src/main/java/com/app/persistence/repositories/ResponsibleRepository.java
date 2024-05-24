package com.app.persistence.repositories;

import com.app.persistence.entities.institutions.AddressEntity;
import com.app.persistence.entities.institutions.ResponsibleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsibleRepository extends JpaRepository<ResponsibleEntity, Long> {
}
