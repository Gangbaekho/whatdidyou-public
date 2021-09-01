package com.nuitblanche.whatdidyou.controller;

import com.nuitblanche.whatdidyou.controller.dto.ProjectResponseDto;
import com.nuitblanche.whatdidyou.controller.dto.ProjectSaveRequestDto;
import com.nuitblanche.whatdidyou.controller.dto.ProjectUpdateRequestDto;
import com.nuitblanche.whatdidyou.response.ListResult;
import com.nuitblanche.whatdidyou.response.SingleResult;
import com.nuitblanche.whatdidyou.security.CurrentUser;
import com.nuitblanche.whatdidyou.security.UserPrincipal;
import com.nuitblanche.whatdidyou.service.ProjectService;
import com.nuitblanche.whatdidyou.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
public class ProjectController {

    private final ResponseService responseService;
    private final ProjectService projectService;

    @PostMapping("/api/v1/projects")
    public SingleResult<ProjectResponseDto> save(@CurrentUser UserPrincipal userPrincipal, @RequestBody ProjectSaveRequestDto requestDto){


        return responseService.getSingleResult(projectService.save(userPrincipal.getId(),requestDto));
    }

    @PutMapping("/api/v1/projects/{id}")
    public SingleResult<ProjectResponseDto> update(@CurrentUser UserPrincipal userPrincipal, @PathVariable(value = "id") Long projectId,
                         @RequestBody ProjectUpdateRequestDto requestDto){

        return responseService.getSingleResult(projectService.update(userPrincipal.getId(),projectId,requestDto));
    }

    @GetMapping("/api/v1/projects/{id}")
    public SingleResult<ProjectResponseDto> findById(@PathVariable(value = "id") Long projectId){

        return responseService.getSingleResult(projectService.findById(projectId));
    }

    @DeleteMapping("/api/v1/projects/{id}")
    public SingleResult<ProjectResponseDto> deleteById(@CurrentUser UserPrincipal userPrincipal
            ,@PathVariable(value = "id") Long projectId){

        return responseService.getSingleResult(projectService.deleteById(userPrincipal.getId(),projectId));
    }

    @GetMapping("/api/v1/projects")
    public ListResult<ProjectResponseDto> findAll(@CurrentUser UserPrincipal userPrincipal){

        return responseService.getListReulst(projectService.findAllByUserId(userPrincipal.getId()));
    }

}
