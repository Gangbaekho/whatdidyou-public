package com.nuitblanche.whatdidyou.controller.dto;

import com.nuitblanche.whatdidyou.domain.post.Post;
import com.nuitblanche.whatdidyou.domain.tag.Tag;
import com.nuitblanche.whatdidyou.domain.tag.TagType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostSaveRequestDto {

    private String title;
    private String content;
    private String pureText;
    private Long projectId;
    private List<TagType> tagTypes;

    @Builder
    public PostSaveRequestDto(String title, String content,Long projectId, String pureText,List<TagType> tagTypes) {
        this.title = title;
        this.content = content;
        this.projectId = projectId;
        this.pureText = pureText;
        this.tagTypes = tagTypes;
    }

    public Post toEntity(){

        return Post.builder()
                .title(title)
                .content(content)
                .pureText(pureText)
                .build();
    }


}
