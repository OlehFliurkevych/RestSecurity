package com.security.project.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendHtmlEmail(final String to,final String subject,final String body);
}
