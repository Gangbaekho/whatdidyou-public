package com.nuitblanche.whatdidyou.service;


import com.nuitblanche.whatdidyou.advice.exception.CUserNotFoundException;
import com.nuitblanche.whatdidyou.controller.dto.PostResponseDto;
import com.nuitblanche.whatdidyou.controller.dto.ProjectResponseDto;
import com.nuitblanche.whatdidyou.controller.dto.ProjectSaveRequestDto;
import com.nuitblanche.whatdidyou.controller.dto.ProjectUpdateRequestDto;
import com.nuitblanche.whatdidyou.domain.project.Project;
import com.nuitblanche.whatdidyou.domain.project.ProjectRepository;
import com.nuitblanche.whatdidyou.domain.user.User;
import com.nuitblanche.whatdidyou.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectResponseDto save(Long userId, ProjectSaveRequestDto requestDto){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CUserNotFoundException("해당 유저가 존재하지 않습니다. id=" +userId));

        Project project = requestDto.toEntity();
        project.updateUser(user);

        Project savedProject = projectRepository.save(project);

        return new ProjectResponseDto(savedProject);
    }

    @Transactional
    public ProjectResponseDto update(Long userId, Long projectId, ProjectUpdateRequestDto requestDto){

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다. id=" +projectId));

        if(!userId.equals(project.getUser().getId())){
            throw new IllegalArgumentException("유저정보가 일치하지 않아서 수정할 수 없습니다.");
        }

        project.update(requestDto.getTitle());

        return new ProjectResponseDto(project);
    }

    @Transactional
    public ProjectResponseDto findById(Long projectId){

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다. id=" +projectId));

        return new ProjectResponseDto(project);
    }

    @Transactional
    public ProjectResponseDto deleteById(Long userId, Long projectId){

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다. id=" +projectId));

        if(!userId.equals(project.getUser().getId())){
            throw new IllegalArgumentException("유저정보가 일치하지 않아서 삭제할 수 없습니다.");
        }

        projectRepository.deleteById(projectId);

        return new ProjectResponseDto(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> findAllByUserId(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CUserNotFoundException("해당 유저가 존재하지 않습니다. id=" +userId));

        List<ProjectResponseDto> projects = user.getProjects()
                .stream()
                .map(ProjectResponseDto::new)
                .collect(Collectors.toList());

        return projects;
    }
}
