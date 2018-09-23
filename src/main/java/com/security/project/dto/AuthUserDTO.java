package com.security.project.dto;

import lombok.*;

import java.util.Map;

//@Data
@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class AuthUserDTO {
    private String email;
    private boolean enabled;
    private boolean expired;
    private boolean anonymous;
    private String message;
    private Map<String, Boolean> roles;
    public AuthUserDTO(String userName, Map<String, Boolean> roles, String message, boolean enabled, boolean expired, boolean anonymous) {
        this.email = userName;
        this.roles = roles;
        this.enabled = enabled;
        this.message = message;
        this.expired = expired;
        this.anonymous = anonymous;

    }

    public AuthUserDTO(String email, boolean enabled, boolean expired, boolean anonymous, String message, Map<String, Boolean> roles) {
        this.email = email;
        this.enabled = enabled;
        this.expired = expired;
        this.anonymous = anonymous;
        this.message = message;
        this.roles = roles;
    }
}
