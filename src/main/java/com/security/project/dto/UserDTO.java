package com.security.project.dto;

import com.security.project.validation.ValidEmail;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
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

}
