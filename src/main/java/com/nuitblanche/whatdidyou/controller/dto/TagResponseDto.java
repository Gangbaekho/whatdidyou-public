package com.nuitblanche.whatdidyou.controller.dto;

import com.nuitblanche.whatdidyou.domain.tag.Tag;
import com.nuitblanche.whatdidyou.domain.tag.TagType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TagResponseDto {

    private Long id;
    private TagType tagType;

    public TagResponseDto(Tag tag){
        this.id = tag.getId();
        this.tagType = tag.getTagType();
    }
}
