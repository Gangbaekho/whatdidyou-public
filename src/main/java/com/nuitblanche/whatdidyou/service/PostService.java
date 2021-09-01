package com.nuitblanche.whatdidyou.service;

import com.nuitblanche.whatdidyou.advice.exception.CUserNotFoundException;
import com.nuitblanche.whatdidyou.controller.dto.*;
import com.nuitblanche.whatdidyou.domain.post.Post;
import com.nuitblanche.whatdidyou.domain.post.PostRepository;
import com.nuitblanche.whatdidyou.domain.posttag.PostTag;
import com.nuitblanche.whatdidyou.domain.posttag.PostTagRepository;
import com.nuitblanche.whatdidyou.domain.project.Project;
import com.nuitblanche.whatdidyou.domain.project.ProjectRepository;
import com.nuitblanche.whatdidyou.domain.tag.Tag;
import com.nuitblanche.whatdidyou.domain.tag.TagRepository;
import com.nuitblanche.whatdidyou.domain.tag.TagType;
import com.nuitblanche.whatdidyou.domain.user.User;
import com.nuitblanche.whatdidyou.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final int POSTS_PER_PAGE = 10;

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    @Transactional
    public PostResponseDto save(Long userId, PostSaveRequestDto requestDto){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CUserNotFoundException("해당 유저가 존재하지 않습니다. id=" +userId));

        Project project = projectRepository.findById(requestDto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다. id=" + requestDto.getProjectId()));

        if(!user.getId().equals(project.getUser().getId())){
            throw new IllegalArgumentException("해당 프로젝트는 유저의 소유가 아닙니다.");
        }


        Post post = requestDto.toEntity();
        post.updateUser(user);
        post.updateProject(project);

        postRepository.save(post);

        List<TagType> tagTypes = requestDto.getTagTypes();
        if(!tagTypes.isEmpty()){
            List<Tag> tags = tagRepository.findByTagTypesWithInClause(tagTypes);
            for(Tag tag : tags){
                PostTag postTag = PostTag.builder()
                        .tag(tag)
                        .post(post)
                        .build();

                postTagRepository.save(postTag);
            }
        }

        PostResponseDto responseDto = new PostResponseDto(post);
        responseDto.setTagTypes(tagTypes);

        return responseDto;
    }

    @Transactional
    public PostResponseDto update(Long userId, Long postId, PostUpdateRequestDto requestDto){
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 포스트가 존재하지 않습니다. id=" + postId));

        if(!userId.equals(post.getUser().getId())){
            throw new IllegalArgumentException("유저정보가 일치하지 않아서 업데이트 할 수 없습니다.");
        }

        post.update(requestDto.getTitle(), requestDto.getContent());

        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto findById(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("해당 포스트가 존재하지 않습니다. id=" + postId));

        post.updateViewCount();

        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllDesc(){

        return postRepository.findAllDesc().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponseDto deleteById(Long userId, Long postId){

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new IllegalArgumentException("해당 포스트가 존재하지 않습니다. id="+postId));

        if(!userId.equals(post.getUser().getId())){
            throw new IllegalArgumentException("유저정보가 일치하지 않아서 삭제할 수 없습니다.");
        }

        postRepository.delete(post);

        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> findAllByProjectId(Long userId, Long projectId){

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다. id="+projectId));

        if(!userId.equals(project.getUser().getId())){
            throw new IllegalArgumentException("유저정보가 일치하지 않아 포스트를 제공할 수 없습니다.");
        }

        List<PostResponseDto> postResponseDtos = postRepository.findAllByProjectId(projectId)
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return postResponseDtos;
    }

    @Transactional(readOnly = true)
    public List<YearPostResponseDto> findYearlyInformation(Long userId){

        LocalDate now = LocalDate.now();
        return postRepository.findYearlyInformation(userId,LocalDate.of(now.getYear(),1,1)
                ,LocalDate.of(now.getYear(),12,31));
    }

    @Transactional(readOnly = true)
    public List<DayPostResponseDto> findDailyInformation(DayPostRequestDto requestDto){

        return postRepository.findDailyInformation(requestDto.getUserId(), requestDto.getDay());
    }

    @Transactional(readOnly = true)
    public List<MonthPostResponseDto> findMonthlyInformation(Long userId){

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        return postRepository.findMothlyInformation(userId,LocalDate.of(year,month,1),
                LocalDate.of(year,month,31));
    }

    @Transactional(readOnly = true)
    public PagePostResponseDto findCurrentPagePostsInProject(Long projectId, int currentPage){

        PageRequest pageRequest = PageRequest.of(currentPage, POSTS_PER_PAGE, Sort.by(Sort.Direction.DESC,"id"));
        Page<Post> result = postRepository.findCurrentPagePostsInProject(projectId, pageRequest);

        List<PostResponseDto> posts = result.getContent()
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        PagePostResponseDto pagePostResponseDto = new PagePostResponseDto(result.getNumber(), result.getTotalPages(), posts);

        return pagePostResponseDto;
    }

    @Transactional(readOnly = true)
    public PagePostResponseDto findCurrentPagePosts(int currentPage){

        PageRequest pageRequest = PageRequest.of(currentPage, POSTS_PER_PAGE, Sort.by(Sort.Direction.DESC,"id"));
        Page<Post> result = postRepository.findCurrentPagePosts(pageRequest);

        List<PostResponseDto> posts = result.getContent()
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        PagePostResponseDto pagePostResponseDto = new PagePostResponseDto(result.getNumber(),result.getTotalPages(),posts);

        return pagePostResponseDto;
    }
}