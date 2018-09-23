//package com.security.project.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//import java.util.Properties;
//
//@Configuration
//public class MailConfig {
//
//    @Value("${spring.mail.host}")
//    private String host;
//
//    @Value("${spring.mail.port}")
//    private int port;
//
//    @Value("${spring.mail.username}")
//    private String userName;
//
//    @Value("${spring.mail.password}")
//    private String password;
//
//    @Value("${spring.mail.protocol}")
//    private String protocol;
//
//    @Value("${mail.debug}")
//    private String debug;
//
//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost(host);
//        mailSender.setPort(port);
//        mailSender.setUsername(userName);
//        mailSender.setPassword(password);
////        mailSender.setHost("smtp.gmail.com");
////        mailSender.setPort(587);
////
////        mailSender.setUsername("oleh0809@gmail.com");
////        mailSender.setPassword("meizum3note");
////
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
//}
