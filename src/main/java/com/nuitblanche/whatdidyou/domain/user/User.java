package com.nuitblanche.whatdidyou.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nuitblanche.whatdidyou.domain.BaseTimeEntity;
import com.nuitblanche.whatdidyou.domain.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String providerId;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

    @Builder
    public User(String name, String email, String imageUrl,
                Boolean emailVerified,String password, AuthProvider provider,
                Role role,String providerId, List<Project> projects) {
        this.name = name;
        this.email = email;
        this.emailVerified = emailVerified;
        this.password = password;
        this.provider = provider;
        this.imageUrl = imageUrl;
        this.role = role;
        this.providerId = providerId;
        this.projects = projects;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }


}