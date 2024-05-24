package com.app.persistence.repositories;

import com.app.persistence.entities.users.InvitationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationTokenRepository extends JpaRepository<InvitationTokenEntity, Long> {
    Optional<InvitationTokenEntity> findByToken(String token);
}

