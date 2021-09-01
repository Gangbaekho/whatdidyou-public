package com.nuitblanche.whatdidyou.service;

import com.nuitblanche.whatdidyou.controller.dto.TagResponseDto;
import com.nuitblanche.whatdidyou.controller.dto.TagSaveRequestDto;
import com.nuitblanche.whatdidyou.domain.tag.Tag;
import com.nuitblanche.whatdidyou.domain.tag.TagRepository;
import com.nuitblanche.whatdidyou.domain.tag.TagType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagResponseDto save(TagSaveRequestDto requestDto){

        Tag tag = requestDto.toEntity();
        tagRepository.save(tag);

        return new TagResponseDto(tag);
    }

    public TagResponseDto findByTagType(TagType tagType){

        Tag tag = tagRepository.findByTagType(tagType);
        if(Objects.isNull(tag)){
            throw new IllegalArgumentException("해당하는 태그가 존재하지 않습니다. :" +tagType);
        }

        return new TagResponseDto(tag);
    }

}
