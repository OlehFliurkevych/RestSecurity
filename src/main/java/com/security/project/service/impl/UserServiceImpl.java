package com.security.project.service.impl;

import com.mysql.jdbc.StringUtils;
import com.security.project.dto.RestMessageDTO;
import com.security.project.dto.UserDTO;
import com.security.project.entity.UserEntity;
import com.security.project.repository.UserRepository;
import com.security.project.service.UserService;
import com.security.project.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ObjectMapperUtils modelMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ObjectMapperUtils modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RestMessageDTO createAccountForUser(UserDTO userDto) {
        if (userDto.getId() <= 0 ||
                StringUtils.isNullOrEmpty(userDto.getLogin()) ||
                StringUtils.isNullOrEmpty(userDto.getEmail()) ||
                StringUtils.isNullOrEmpty(userDto.getPassword()) ||
                StringUtils.isNullOrEmpty(userDto.getPasswordConfirm())) {
            throw new RuntimeException("Invalid data");
        }
        if (!userDto.getPassword().equals(userDto.getPasswordConfirm())
        ) {
            throw new RuntimeException("Incorrect passwordConfirm");
        }
        if (emailExist(userDto.getEmail())) {
            throw new RuntimeException("Exist user with " + userDto.getEmail() + " email exist");
        }
        UserEntity user = new UserEntity();
        user.setEmail(userDto.getEmail());
        user.setLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return RestMessageDTO.createCorrectMessage("Create new user");
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public RestMessageDTO<UserDTO> findUserByEmail(String email) {
        if (StringUtils.isNullOrEmpty(email)) {
            throw new RuntimeException("Email is null or empty");
        }
        UserEntity entity = userRepository.findByEmail(email);
        if (entity == null) {
            throw new RuntimeException("Doesn't exist user");
        }
        UserDTO userDTO=modelMapper.map(entity,UserDTO.class);
        return new RestMessageDTO<>(userDTO,true);
    }
}
