package com.security.project.service.impl;

import com.security.project.entity.VerificationTokenEntity;
import com.security.project.repository.VerificationTokenRepository;
import com.security.project.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public VerificationTokenEntity getVerificationTokenByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}
