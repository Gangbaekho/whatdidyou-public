package com.nuitblanche.whatdidyou.controller;

import com.nuitblanche.whatdidyou.controller.dto.TagResponseDto;
import com.nuitblanche.whatdidyou.controller.dto.TagSaveRequestDto;
import com.nuitblanche.whatdidyou.domain.tag.TagType;
import com.nuitblanche.whatdidyou.response.SingleResult;
import com.nuitblanche.whatdidyou.service.ResponseService;
import com.nuitblanche.whatdidyou.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class TagController {

    private final TagService tagService;
    private final ResponseService responseService;

    @PostMapping("/api/v1/tags")
    public SingleResult<TagResponseDto> save(@RequestBody TagSaveRequestDto requestDto){

        return responseService.getSingleResult(tagService.save(requestDto));
    }

    @GetMapping("/api/v1/tags/{tagType}")
    public SingleResult<TagResponseDto> findByTagType(@PathVariable(value = "tagType")TagType tagType){

        return responseService.getSingleResult(tagService.findByTagType(tagType));
    }

}
