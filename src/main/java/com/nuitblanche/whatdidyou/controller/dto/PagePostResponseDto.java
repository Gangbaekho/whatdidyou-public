package com.nuitblanche.whatdidyou.controller.dto;
import lombok.Getter;

import java.util.List;

@Getter
public class PagePostResponseDto {

    private int currentPage;
    private int lastPage;
    private List<PostResponseDto> posts;

    public PagePostResponseDto(int currentPage, int lastPage, List<PostResponseDto> posts) {
        this.currentPage = currentPage;
        this.lastPage = lastPage;
        this.posts = posts;
    }
}
