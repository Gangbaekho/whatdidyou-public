package com.nuitblanche.whatdidyou.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectUpdateRequestDto {

    private String title;

    @Builder
    public ProjectUpdateRequestDto(String title) {
        this.title = title;
    }
}