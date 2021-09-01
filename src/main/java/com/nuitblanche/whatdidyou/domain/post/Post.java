package com.nuitblanche.whatdidyou.domain.post;

import com.nuitblanche.whatdidyou.domain.BaseTimeEntity;
import com.nuitblanche.whatdidyou.domain.posttag.PostTag;
import com.nuitblanche.whatdidyou.domain.project.Project;
import com.nuitblanche.whatdidyou.domain.tag.Tag;
import com.nuitblanche.whatdidyou.domain.tag.TagType;
import com.nuitblanche.whatdidyou.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String pureText;

    @Column(columnDefinition = "integer default 0")
    private int likeCount;

    @Column(columnDefinition = "integer default 0")
    private int viewCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="PROJECT_ID", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="USER_ID", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<PostTag> postTags = new ArrayList<>();

    @Builder
    public Post(String title, String content,String pureText, Project project,User user) {
        this.title = title;
        this.content = content;
        this.pureText = pureText;
        this.project = project;
        this.user = user;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void updateProject(Project project){

        if(Objects.nonNull(this.project)){
            this.project.getPosts().remove(this);
        }

        this.project = project;

        if(Objects.nonNull(project)){
            project.getPosts().add(this);
        }
    }

    public void updateUser(User user){
        this.user = user;
    }

    public void updateViewCount(){
        this.viewCount++;
    }

    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount(){
        this.likeCount--;
    }

    public void updateTags(List<PostTag> postTags){
        this.postTags = postTags;
    }

    public List<TagType> getTagTypes(){

        List<TagType> tagTypes = new ArrayList<>();
        for(PostTag postTag : this.postTags){
            tagTypes.add(postTag.getTag().getTagType());
        }

        return tagTypes;
    }

}
