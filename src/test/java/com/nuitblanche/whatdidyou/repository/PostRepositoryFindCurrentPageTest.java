package com.nuitblanche.whatdidyou.repository;

import com.nuitblanche.whatdidyou.controller.dto.PostResponseDto;
import com.nuitblanche.whatdidyou.controller.dto.PostSaveRequestDto;
import com.nuitblanche.whatdidyou.controller.dto.ProjectSaveRequestDto;
import com.nuitblanche.whatdidyou.controller.dto.TagSaveRequestDto;
import com.nuitblanche.whatdidyou.domain.post.Post;
import com.nuitblanche.whatdidyou.domain.post.PostRepository;
import com.nuitblanche.whatdidyou.domain.posttag.PostTagRepository;
import com.nuitblanche.whatdidyou.domain.project.Project;
import com.nuitblanche.whatdidyou.domain.project.ProjectRepository;
import com.nuitblanche.whatdidyou.domain.tag.TagRepository;
import com.nuitblanche.whatdidyou.domain.tag.TagType;
import com.nuitblanche.whatdidyou.domain.user.AuthProvider;
import com.nuitblanche.whatdidyou.domain.user.Role;
import com.nuitblanche.whatdidyou.domain.user.User;
import com.nuitblanche.whatdidyou.domain.user.UserRepository;
import com.nuitblanche.whatdidyou.security.TokenProvider;
import com.nuitblanche.whatdidyou.security.UserPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepositoryFindCurrentPageTest {
    @PersistenceUnit
    private EntityManagerFactory factory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostTagRepository postTagRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    private String token;

    @Before
    public void setup(){
        // setup user
        User user = User.builder()
                .name("tester")
                .email("tester@test.com")
                .password(passwordEncoder.encode("test"))
                .provider(AuthProvider.local)
                .role(Role.USER)
                .providerId("test")
                .emailVerified(false)
                .imageUrl("test")
                .build();

        userRepository.save(user);
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        token = tokenProvider.createTokenForTest(userPrincipal);

        // setup project one
        User selectedUser = userRepository.findAll().get(0);

        ProjectSaveRequestDto projectOnerequestDto = ProjectSaveRequestDto.builder()
                .title("project-one")
                .build();

        Project projectOne = projectOnerequestDto.toEntity();
        projectOne.updateUser(selectedUser);


        projectRepository.save(projectOne);

        // setup tags
        TagSaveRequestDto itTag = TagSaveRequestDto.builder()
                .tagType(TagType.IT)
                .build();

        TagSaveRequestDto societyTag = TagSaveRequestDto.builder()
                .tagType(TagType.SOCIETY)
                .build();

        TagSaveRequestDto scienceTag = TagSaveRequestDto.builder()
                .tagType(TagType.SCIENCE)
                .build();

        tagRepository.save(itTag.toEntity());
        tagRepository.save(societyTag.toEntity());
        tagRepository.save(scienceTag.toEntity());



        PostSaveRequestDto postSaveRequestDtoOne = PostSaveRequestDto.builder()
                .title("title")
                .content("content")
                .pureText("pure-text")
                .tagTypes(Arrays.asList(TagType.IT,TagType.SOCIETY,TagType.SCIENCE))
                .build();


        Post postOne = postSaveRequestDtoOne.toEntity();
        Post postTwo = postSaveRequestDtoOne.toEntity();
        Post postThree = postSaveRequestDtoOne.toEntity();

        postOne.updateUser(selectedUser);
        postOne.updateProject(projectOne);

        postTwo.updateUser(selectedUser);
        postTwo.updateProject(projectOne);

        postThree.updateUser(selectedUser);
        postThree.updateProject(projectOne);


        postRepository.save(postOne);
        postRepository.save(postTwo);
        postRepository.save(postThree);
    }

    @Test
    public void findCurrentPageNPlusOneCheck(){

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC,"id"));
        Page<Post> result = postRepository.findCurrentPagePosts(pageRequest);

        List<PostResponseDto> posts = result.getContent()
                .stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

    }
}
