package com.nuitblanche.whatdidyou.controller.dto;

import lombok.Getter;

@Getter
public class MonthPostResponseDto {

    private Long projectId;
    private String projectTitle;
    private Long postCount;

    public MonthPostResponseDto(Long projectId, String projectTitle, Long postCount) {
        this.projectId = projectId;
        this.projectTitle = projectTitle;
        this.postCount = postCount;
    }
}
