package com.security.project.entity;

import com.security.project.enumeration.UserRole;
import lombok.*;

import javax.persistence.*;

//@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private Boolean activated = false;
    @OneToOne(mappedBy = "user")
    private VerificationTokenEntity verificationToken;

    public UserEntity(String email, String login, String password, UserRole role) {
        this.email = email;
        this.login = login;
        this.password = password;
        this.role = role;
    }
}
