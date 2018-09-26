package com.security.project.repository;

import com.security.project.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity,String> {

    List<VerificationTokenEntity> findByUserEmail(String email);
    VerificationTokenEntity findByToken(String token);
}
