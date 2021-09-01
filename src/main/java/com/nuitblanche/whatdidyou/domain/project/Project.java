package com.nuitblanche.whatdidyou.domain.project;

import com.nuitblanche.whatdidyou.domain.BaseTimeEntity;
import com.nuitblanche.whatdidyou.domain.post.Post;
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
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "integer default 0")
    private int postCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Post> posts;

    @Builder
    public Project(String title, int postCount, List<Post> posts, User user) {
        this.title = title;
        this.postCount = postCount;
        this.posts = posts;
        this.user = user;
    }

    public void update(String title){
        this.title = title;
    }

    public void increasePostCount(){
        this.postCount++;
    }

    public void decreasePostCount(){
        this.postCount--;
    }

    public void updateUser(User user){
        this.user = user;
    }

    public List<Post> getPosts(){
        if(Objects.isNull(this.posts)){
            this.posts = new ArrayList<>();
        }

        return this.posts;
    }


}
