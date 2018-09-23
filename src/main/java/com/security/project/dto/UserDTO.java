package com.security.project.dto;

import com.security.project.entity.UserEntity;
import com.security.project.enumeration.UserRole;
import com.security.project.validation.ValidEmail;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
//@Data
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {


    private Long id;

    @NotEmpty
    @NotNull
    @ValidEmail(message="Invalid email")
    private String email;

    @NotEmpty
    @NotNull
    private String login;

    @NotEmpty
    @NotNull
    private String password;

    @NotEmpty
    @NotNull
    private String passwordConfirm;

    @NotNull
    private UserRole userRole;

    public static UserEntity toUser(String email,String login, String hashedPassword) {
        return new UserEntity(email,login, hashedPassword, UserRole.ROLE_ADMIN);
    }
}
