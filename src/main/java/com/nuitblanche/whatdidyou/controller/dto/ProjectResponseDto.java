package com.nuitblanche.whatdidyou.controller.dto;

import com.nuitblanche.whatdidyou.domain.project.Project;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProjectResponseDto {

    private Long id;
    private String title;
    private int postCount;

    public ProjectResponseDto(Project project){
        this.id = project.getId();
        this.title = project.getTitle();
        this.postCount = project.getPostCount();
    }

}