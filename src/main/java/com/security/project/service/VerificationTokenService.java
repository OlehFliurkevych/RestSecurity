package com.security.project.service;

import com.security.project.entity.VerificationTokenEntity;
import com.security.project.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface VerificationTokenService {

    VerificationTokenEntity getVerificationTokenByToken(String token);

}
