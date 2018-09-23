package com.security.project.dto;

import com.security.project.validation.ValidEmail;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @NotEmpty
    @NotNull
    @ValidEmail
    private String email;
    @NotEmpty
    @NotNull
    private String password;
}
