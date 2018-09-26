package com.security.project.service;

import com.security.project.dto.RestMessageDTO;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendHtmlEmail(final String to,final String subject,final String body);

    void sendVerificationEmail(final String name, final String email,final String token);
}
