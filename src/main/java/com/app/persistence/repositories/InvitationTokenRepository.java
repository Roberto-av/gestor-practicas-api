package com.app.persistence.repositories;

import com.app.persistence.entities.users.InvitationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationTokenRepository extends JpaRepository<InvitationTokenEntity, Long> {
    Optional<InvitationTokenEntity> findByToken(String token);
}

