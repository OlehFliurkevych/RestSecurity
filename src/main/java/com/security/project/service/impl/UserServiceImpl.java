package com.security.project.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.mysql.jdbc.StringUtils;
import com.security.project.dto.AuthUserDTO;
import com.security.project.dto.LoginDTO;
import com.security.project.dto.RestMessageDTO;
import com.security.project.dto.UserDTO;
import com.security.project.entity.UserEntity;
import com.security.project.entity.VerificationTokenEntity;
import com.security.project.repository.UserRepository;
import com.security.project.repository.VerificationTokenRepository;
import com.security.project.service.EmailService;
import com.security.project.service.UserService;
import com.security.project.service.VerificationTokenService;
import com.security.project.utils.EmailGenerator;
import com.security.project.utils.ObjectMapperUtils;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.ws.Response;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Value("${succes.verification}")
    private String SUCCES_VERIFICATION;

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final VerificationTokenService tokenService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final ObjectMapperUtils modelMapper;

    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(VerificationTokenService tokenService, UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder, AuthenticationManager authManager, ObjectMapperUtils modelMapper, VerificationTokenRepository tokenRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.modelMapper = modelMapper;
        this.tokenRepository = tokenRepository;
    }



    @Override
    public RestMessageDTO<UserDTO> getVerification(String token) {
        VerificationTokenEntity tokenEntity = tokenService.getVerificationTokenByToken(token);
        if (tokenEntity == null) return RestMessageDTO.createFailureMessage("Doesn't exist this token");
//        if (tokenEntity == null) return ResponseEntity.badRequest().body("Doesn't exist '" + token + "' token!");
        if (!tokenEntity.getExpiryDate().isAfter(LocalDateTime.now())) {
            return RestMessageDTO.createFailureMessage("ExpiryDate was left");
//            return ResponseEntity.badRequest().body("ExpiryDate was left");
        }
        UserEntity user = userRepository.getOne(tokenEntity.getUser().getId());
        if (user == null) return RestMessageDTO.createFailureMessage("Doesn't exist user with current token");
//        if (user == null) return ResponseEntity.badRequest().body("Doesn't exist user with current token!");
        user.setActivated(true);
        UserDTO userDTO=modelMapper.map(user,UserDTO.class);
        userRepository.save(user);
//        final String body = EmailGenerator.sendHtml(user.getLogin(), SUCCES_VERIFICATION);
//        return ResponseEntity.ok().body(body);
        return new RestMessageDTO<>(userDTO,true);
    }

    @Transactional
    public RestMessageDTO createNotEnabledUser(UserDTO registrationDTO) {
        if (isNotFilledFieldsExist(registrationDTO)) {
            return RestMessageDTO.createFailureMessage("Failed to create user, fill all required fields");
        }
        if (!registrationDTO.getPassword().equals(registrationDTO.getPasswordConfirm())) {
            return RestMessageDTO.createFailureMessage("Failed to create user, passwords do not match");
        }
        UserEntity existingUser = userRepository.findByEmail(registrationDTO.getEmail());
        if (existingUser != null) {
            return RestMessageDTO.createFailureMessage("User already registered");
        }

        String hashedPassword = passwordEncoder.encode(registrationDTO.getPassword());
        UserEntity user = UserDTO.toUser(
                registrationDTO.getEmail(),
                registrationDTO.getLogin(),
                hashedPassword);

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        addTokenToUser(token, user);

        emailService.sendVerificationEmail(user.getLogin(), user.getEmail(), token);

        return RestMessageDTO.createCorrectMessage("Success");
    }

    private void addTokenToUser(String token, UserEntity user) {
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity();
        verificationTokenEntity.setToken(token);
        verificationTokenEntity.setUser(user);
        LocalDateTime local = LocalDateTime.now();
        verificationTokenEntity.setExpiryDate(local.plusDays(1));
        tokenRepository.save(verificationTokenEntity);
    }


    @Override
    public AuthUserDTO getLoginUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return transformAuthenticationToAuthUserDTO(securityContext.getAuthentication());
    }

    private boolean isNotFilledFieldsExist(UserDTO registrationDTO) {
        return registrationDTO.getEmail() == null ||
                registrationDTO.getEmail().isEmpty() ||
                registrationDTO.getLogin().isEmpty() ||
                registrationDTO.getPassword() == null ||
                registrationDTO.getPassword().isEmpty() ||
                registrationDTO.getPasswordConfirm() == null ||
                registrationDTO.getPasswordConfirm().isEmpty();
    }

    @Override
    public AuthUserDTO authenticateUser(LoginDTO loginUserDTO) {
        UserEntity user = userRepository.findByEmail(loginUserDTO.getEmail());
        if (user == null) return new AuthUserDTO(null,
                null, "Doesn't exist user!",
                true, false, true);
        if (!user.getActivated()) return new AuthUserDTO(null,
                null, "User don't verificate account!",
                true, false, true);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail().trim(),
                        loginUserDTO.getPassword());
        Authentication authentication;
        try {
            authentication = this.authManager.authenticate(authenticationToken);
        } catch (DisabledException e) {
            LOGGER.error("Failed to authenticate : " + loginUserDTO.getEmail(), e);
            return new AuthUserDTO(null,
                    null, "Please confirm your sign up using link in your email",
                    true, false, true);
        } catch (BadCredentialsException e) {
            LOGGER.error("Failed to authenticate : " + loginUserDTO.getEmail(), e);
            return new AuthUserDTO(null,
                    null, "Invalid credentials",
                    true, false, true);
        } catch (AccountExpiredException e) {
            LOGGER.error("Failed to authenticate : " + loginUserDTO.getEmail(), e);
            return new AuthUserDTO(null,
                    null, "Your account has expired",
                    true, true, true);
        } catch (AuthenticationException e) {
            LOGGER.error("Failed to authenticate : " + loginUserDTO.getEmail(), e);
            return new AuthUserDTO(null,
                    null, "Failed to authenticate, please check your credentials",
                    true, false, true);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return transformAuthenticationToAuthUserDTO(authentication);
    }

    private AuthUserDTO transformAuthenticationToAuthUserDTO(Authentication authentication) {
        if (authentication == null) {
            return new AuthUserDTO(
                    null,
                    null,
                    "Failed to obtain authentication, please check your credentials",
                    false,
                    false,
                    false);
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && (principal).equals("anonymousUser")) {
            return new AuthUserDTO(null, null, "Anonymous", false, false, true);
        }
        UserDetails userDetails = (UserDetails) principal;
        UserEntity user = userRepository.findByEmail(userDetails.getUsername());
        AuthUserDTO userDTO = new AuthUserDTO(user.getEmail(), true, false, false, "Success", createRoleMap(userDetails));
        return userDTO;
    }

    private static Map<String, Boolean> createRoleMap(UserDetails userDetails) {
        Map<String, Boolean> roles = new HashMap<>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.put(authority.getAuthority(), Boolean.TRUE);
        }
        return roles;
    }


}
