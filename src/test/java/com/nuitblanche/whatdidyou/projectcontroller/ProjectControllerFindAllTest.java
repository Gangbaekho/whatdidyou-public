package com.nuitblanche.whatdidyou.projectcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuitblanche.whatdidyou.controller.dto.ProjectResponseDto;
import com.nuitblanche.whatdidyou.controller.dto.ProjectSaveRequestDto;
import com.nuitblanche.whatdidyou.domain.post.PostRepository;
import com.nuitblanche.whatdidyou.domain.posttag.PostTagRepository;
import com.nuitblanche.whatdidyou.domain.project.Project;
import com.nuitblanche.whatdidyou.domain.project.ProjectRepository;
import com.nuitblanche.whatdidyou.domain.tag.TagRepository;
import com.nuitblanche.whatdidyou.domain.user.AuthProvider;
import com.nuitblanche.whatdidyou.domain.user.Role;
import com.nuitblanche.whatdidyou.domain.user.User;
import com.nuitblanche.whatdidyou.domain.user.UserRepository;
import com.nuitblanche.whatdidyou.response.ListResult;
import com.nuitblanche.whatdidyou.response.SingleResult;
import com.nuitblanche.whatdidyou.security.TokenProvider;
import com.nuitblanche.whatdidyou.security.UserPrincipal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectControllerFindAllTest {

    @LocalServerPort
    private int port;

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

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void projectSetup(){

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

        // setup project
        User selectedUser = userRepository.findAll().get(0);

        ProjectSaveRequestDto requestDtoOne = ProjectSaveRequestDto.builder()
                .title("title-one")
                .build();

        ProjectSaveRequestDto requestDtoTwo = ProjectSaveRequestDto.builder()
                .title("title-two")
                .build();

        Project projectOne = requestDtoOne.toEntity();
        projectOne.updateUser(selectedUser);

        Project projectTwo = requestDtoTwo.toEntity();
        projectTwo.updateUser(selectedUser);

        projectRepository.save(projectOne);
        projectRepository.save(projectTwo);
    }

    @After
    public void cleanup(){

        postTagRepository.deleteAll();
        tagRepository.deleteAll();
        postRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void findAllProjects(){

        String getUrl = "http://localhost:" + port + "/api/v1/projects";

        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setContentType(MediaType.APPLICATION_JSON);
        getHeaders.set("Authorization","Bearer "+token);
        HttpEntity<Object> getRequestEntity = new HttpEntity<>(null,getHeaders);

        ResponseEntity<ListResult> getResponseEntity = restTemplate.exchange(getUrl, HttpMethod.GET,getRequestEntity,ListResult.class);

        List<Object> objects = getResponseEntity.getBody().getList();
        assertThat(objects.size()).isEqualTo(2);
    }
}
