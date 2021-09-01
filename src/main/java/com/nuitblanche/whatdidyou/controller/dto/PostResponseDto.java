package com.nuitblanche.whatdidyou.controller.dto;

import com.nuitblanche.whatdidyou.domain.post.Post;
import com.nuitblanche.whatdidyou.domain.project.Project;
import com.nuitblanche.whatdidyou.domain.tag.TagType;
import com.nuitblanche.whatdidyou.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String pureText;
    private int likeCount;
    private int viewCount;
    private Long userId;
    private String userName;
    private String imageUrl;
    private Long projectId;
    private String projectTitle;
    private List<TagType> tagTypes;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public PostResponseDto(Post entity){

        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.pureText = entity.getPureText();
        this.likeCount=  entity.getLikeCount();
        this.viewCount = entity.getViewCount();
        this.userId = entity.getUser().getId();
        this.userName = entity.getUser().getName();
        this.imageUrl = entity.getUser().getImageUrl();
        this.projectId = entity.getProject().getId();
        this.projectTitle = entity.getProject().getTitle();
        this.tagTypes = entity.getTagTypes();
        this.createdDate = entity.getCreatedDate();
        this.updatedDate = entity.getUpdatedDate();
    }

    public void setTagTypes(List<TagType> tagTypes){
        this.tagTypes = tagTypes;
    }

}
