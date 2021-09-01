package com.nuitblanche.whatdidyou.controller;

import com.nuitblanche.whatdidyou.advice.exception.CUserNotFoundException;
import com.nuitblanche.whatdidyou.controller.dto.TagResponseDto;
import com.nuitblanche.whatdidyou.controller.dto.TagSaveRequestDto;
import com.nuitblanche.whatdidyou.domain.tag.Tag;
import com.nuitblanche.whatdidyou.domain.tag.TagRepository;
import com.nuitblanche.whatdidyou.domain.tag.TagType;
import com.nuitblanche.whatdidyou.response.SingleResult;
import com.nuitblanche.whatdidyou.security.CurrentUser;
import com.nuitblanche.whatdidyou.security.UserPrincipal;
import com.nuitblanche.whatdidyou.service.ResponseService;
import com.nuitblanche.whatdidyou.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

    private final TagService tagService;
    private final ResponseService responseService;

    @GetMapping("/whoami")
    public UserPrincipal whoami(@CurrentUser UserPrincipal userPrincipal){

        return userPrincipal;
    }

    @PostMapping("/api/v1/tags")
    public SingleResult<TagResponseDto> save(@RequestBody TagSaveRequestDto requestDto){

        return responseService.getSingleResult(tagService.save(requestDto));
    }

    @GetMapping("/throw")
    public void throwException(){
        throw new CUserNotFoundException("해당 유저는 찾을 수 없다.");
    }

    @GetMapping("/api/v1/tags/{tagType}")
    public TagType testTagType(@PathVariable(value = "tagType") TagType tagType){

        return tagType;
    }
}
