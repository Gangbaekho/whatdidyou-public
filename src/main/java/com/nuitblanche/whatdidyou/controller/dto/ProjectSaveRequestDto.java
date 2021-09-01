package com.nuitblanche.whatdidyou.controller.dto;

import com.nuitblanche.whatdidyou.domain.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectSaveRequestDto {

    private String title;

    @Builder
    public ProjectSaveRequestDto(String title) {
        this.title = title;
    }

    public Project toEntity(){
        return Project.builder()
                .title(title)
                .postCount(0)
                .build();
    }

}
