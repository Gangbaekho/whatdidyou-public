package com.nuitblanche.whatdidyou.controller.dto;


import com.nuitblanche.whatdidyou.domain.tag.Tag;
import com.nuitblanche.whatdidyou.domain.tag.TagType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TagSaveRequestDto {

    private TagType tagType;

    @Builder
    public TagSaveRequestDto(TagType tagType) {
        this.tagType = tagType;
    }

    public Tag toEntity(){
        return Tag.builder()
                .tagType(tagType)
                .build();
    }
}
