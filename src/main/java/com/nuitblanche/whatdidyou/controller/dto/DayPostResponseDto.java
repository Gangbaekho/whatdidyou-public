package com.nuitblanche.whatdidyou.controller.dto;

import lombok.Getter;

@Getter
public class DayPostResponseDto {
    private Long projectId;
    private String projectTitle;
    private Long postCount;

    public DayPostResponseDto(Long projectId, String projectTitle, Long postCount) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.postCount = postCount;
    }
}
