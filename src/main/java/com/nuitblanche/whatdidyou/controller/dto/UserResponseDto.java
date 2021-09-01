package com.nuitblanche.whatdidyou.controller.dto;


import com.nuitblanche.whatdidyou.security.UserPrincipal;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;
    private String userName;
    private String imageUrl;
    private String email;

    public UserResponseDto(UserPrincipal userPrincipal){
        this.id = userPrincipal.getId();;
        this.userName = userPrincipal.getUsername();
        this.imageUrl = userPrincipal.getImageUrl();
        this.email = userPrincipal.getEmail();
    }

}
