package com.security.project.service.impl;

import com.mysql.jdbc.StringUtils;
import com.security.project.dto.AuthUserDTO;
import com.security.project.dto.LoginDTO;
import com.security.project.dto.RestMessageDTO;
import com.security.project.dto.UserDTO;
import com.security.project.entity.UserEntity;
import com.security.project.repository.UserRepository;
import com.security.project.service.EmailService;
import com.security.project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authManager;






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
        UserEntity user = UserDTO.toUser(registrationDTO.getEmail(),registrationDTO.getLogin(), hashedPassword);
        userRepository.save(user);

        return new RestMessageDTO("Success", true);
    }



    @Override
    public AuthUserDTO getLoginUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return transformAuthenticationToAuthUserDTO( securityContext.getAuthentication());
    }

    @Override
    public RestMessageDTO sendMessageToUser(String mail) {
        if(StringUtils.isNullOrEmpty(mail)){
            throw new RuntimeException("No request param email");
        }
        UserEntity user=userRepository.findByEmail(mail);
        if (user==null)throw new RuntimeException("Doesn't exist user with this email");
        emailService.sendHtmlEmail(
                user.getEmail(),
                "Welcome "+user.getLogin(),
                "Hello "+user.getLogin()+" ! Thank you for registration! Have a nice day!");
        return RestMessageDTO.createCorrectMessage("Succes email sending");
    }


    private boolean isNotFilledFieldsExist(UserDTO registrationDTO) {
        return registrationDTO.getEmail() == null ||
                registrationDTO.getEmail().isEmpty() ||
                registrationDTO.getLogin().isEmpty()||
                registrationDTO.getPassword() == null ||
                registrationDTO.getPassword().isEmpty() ||
                registrationDTO.getPasswordConfirm() == null ||
                registrationDTO.getPasswordConfirm().isEmpty();
    }

    @Override
    public AuthUserDTO authenticateUser(LoginDTO loginUserDTO) {
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
