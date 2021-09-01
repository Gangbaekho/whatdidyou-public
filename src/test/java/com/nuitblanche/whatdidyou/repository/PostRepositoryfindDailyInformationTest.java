package com.nuitblanche.whatdidyou.repository;

import com.nuitblanche.whatdidyou.controller.dto.*;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostRepositoryfindDailyInformationTest {

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

        postOne.updateUser(selectedUser);
        postOne.updateProject(projectOne);

        postTwo.updateUser(selectedUser);
        postTwo.updateProject(projectOne);


        postRepository.save(postOne);
        postRepository.save(postTwo);
    }

    @Test
    public void dayPostEagerFetchTest(){

        User user = userRepository.findAll().get(0);

        List<DayPostResponseDto> posts = postRepository.findDailyInformation(user.getId(), LocalDate.now());
    }

    @Test
    public void monthPostEagerFetchTest(){

        User user = userRepository.findAll().get(0);

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        List<MonthPostResponseDto> posts = postRepository.findMothlyInformation(user.getId(), LocalDate.of(year,month,1), LocalDate.of(year,month,31));
        System.out.println("hello ");
    }
}
