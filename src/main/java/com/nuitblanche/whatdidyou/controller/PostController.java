package com.nuitblanche.whatdidyou.controller;

import com.nuitblanche.whatdidyou.controller.dto.*;
import com.nuitblanche.whatdidyou.response.ListResult;
import com.nuitblanche.whatdidyou.response.SingleResult;
import com.nuitblanche.whatdidyou.security.CurrentUser;
import com.nuitblanche.whatdidyou.security.UserPrincipal;
import com.nuitblanche.whatdidyou.service.PostService;
import com.nuitblanche.whatdidyou.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final ResponseService responseService;
    private final PostService postService;

    @GetMapping("/api/v1/posts")
    public ListResult<PostResponseDto> findAll(){
        return responseService.getListReulst(postService.findAllDesc());
    }

    @GetMapping("/api/v1/posts/{id}")
    public SingleResult<PostResponseDto> findById (@PathVariable(value = "id") Long postId){

        return responseService.getSingleResult(postService.findById(postId));
    }

    @PostMapping("/api/v1/posts")
    public SingleResult<PostResponseDto> save(@CurrentUser UserPrincipal userPrincipal,
                                   @RequestBody PostSaveRequestDto requestDto){

        return responseService.getSingleResult(postService.save(userPrincipal.getId(),requestDto));
    }

    @PutMapping("/api/v1/posts/{id}")
    public SingleResult<PostResponseDto> update(@CurrentUser UserPrincipal userPrincipal,
                                     @PathVariable(value = "id") Long postId,
                                     @RequestBody PostUpdateRequestDto requestDto){

        return responseService.getSingleResult(postService.update(userPrincipal.getId(),postId,requestDto));
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public SingleResult<PostResponseDto> deleteById(@CurrentUser UserPrincipal userPrincipal,
                                         @PathVariable(value = "id") Long postId){

        return responseService.getSingleResult(postService.deleteById(userPrincipal.getId(),postId));
    }

    @GetMapping("/api/v1/posts/projects/{projectId}")
    public ListResult<PostResponseDto> findAllByProjectId(@CurrentUser UserPrincipal userPrincipal,
                                                          @PathVariable(value = "projectId") Long projectId){
        return responseService.getListReulst(postService.findAllByProjectId(userPrincipal.getId(),projectId));
    }

    @GetMapping("/api/v1/posts/in-year")
    public ListResult<YearPostResponseDto> findAllPostsInYear(@CurrentUser UserPrincipal userPrincipal){

        return responseService.getListReulst(postService.findYearlyInformation(userPrincipal.getId()));
    }

    @GetMapping("/api/v1/posts/in-month")
    public ListResult<MonthPostResponseDto> findMonthlyInformation(@CurrentUser UserPrincipal userPrincipal){

        return responseService.getListReulst(postService.findMonthlyInformation(userPrincipal.getId()));
    }

    @PostMapping("/api/v1/posts/in-day")
    public ListResult<DayPostResponseDto> findAllPostsInDay(@RequestBody DayPostRequestDto requestDto){

        return responseService.getListReulst(postService.findDailyInformation(requestDto));
    }

    @GetMapping("/api/v1/posts/projects/{projectId}/pages/{currentPage}")
    public SingleResult<PagePostResponseDto> findCurrentPagePostsInProject(
            @PathVariable(value = "projectId") Long projectId,
            @PathVariable(value = "currentPage") int currentPage
    ){
        return responseService.getSingleResult(postService.findCurrentPagePostsInProject(projectId,currentPage));
    }

    @GetMapping("/api/v1/posts/pages/{currentPage}")
    public SingleResult<PagePostResponseDto> findCurrentPagePosts(
            @PathVariable(value = "currentPage") int currentPage
    ){
        return responseService.getSingleResult(postService.findCurrentPagePosts(currentPage));
    }

}
