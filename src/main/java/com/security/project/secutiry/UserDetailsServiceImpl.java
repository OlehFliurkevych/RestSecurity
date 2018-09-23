package com.security.project.secutiry;

import com.security.project.entity.UserEntity;
import com.security.project.enumeration.UserRole;
import com.security.project.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = Logger.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        LOGGER.info("Init parameters method loadUserByUsername: " + "email " + email);

        UserEntity user = userRepository.findByEmail(email);
        org.springframework.security.core.userdetails.User springUser;
        if (user == null) {
            throw new BadCredentialsException(email);
        }
        UserRole role = user.getRole();
        final Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(role.getParamName()));
        springUser = new User(user.getEmail(), user.getPassword(), true, true, true, true, authorities);
        return springUser;
    }
}
