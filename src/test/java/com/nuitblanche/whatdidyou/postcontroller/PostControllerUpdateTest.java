package com.nuitblanche.whatdidyou.postcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import javax.persistence.PostUpdate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerUpdateTest {

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

        ProjectSaveRequestDto requestDto = ProjectSaveRequestDto.builder()
                .title("title")
                .build();

        Project project = requestDto.toEntity();
        project.updateUser(selectedUser);

        projectRepository.save(project);

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
    public void findPostById(){
        Project project = projectRepository.findAll().get(0);

        String title = "title";
        String content = "content";
        String pureText = "pureText";
        Long projectId = project.getId();
        List<TagType> tagTypes = Arrays.asList(TagType.IT,TagType.SOCIETY,TagType.SCIENCE);

        PostSaveRequestDto requestDto = PostSaveRequestDto.builder()
                .title(title)
                .content(content)
                .pureText(pureText)
                .tagTypes(tagTypes)
                .projectId(projectId)
                .build();

        String postUrl = "http://localhost:" + port + "/api/v1/posts";

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.setContentType(MediaType.APPLICATION_JSON);
        postHeaders.set("Authorization","Bearer "+token);
        HttpEntity<PostSaveRequestDto> postRequestEntity = new HttpEntity<>(requestDto,postHeaders);

        ResponseEntity<SingleResult> postResponseEntity = restTemplate.exchange(postUrl, HttpMethod.POST,postRequestEntity,SingleResult.class);
        assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Post selectedPost = postRepository.findAll().get(0);

        // update

        String updatedTitle = "updated-title";
        String updatedContent = "updated-content";

        PostUpdateRequestDto updateRequestDto = PostUpdateRequestDto.builder()
                .title(updatedTitle)
                .content(updatedContent)
                .build();

        String updateUrl = "http://localhost:" + port + "/api/v1/posts/" + selectedPost.getId();

        HttpHeaders updateHeaders = new HttpHeaders();
        updateHeaders.setContentType(MediaType.APPLICATION_JSON);
        updateHeaders.set("Authorization","Bearer "+token);
        HttpEntity<PostUpdateRequestDto> updateRequestEntity = new HttpEntity<>(updateRequestDto,updateHeaders);

        ResponseEntity<SingleResult> getResponseEntity = restTemplate.exchange(updateUrl, HttpMethod.PUT,updateRequestEntity,SingleResult.class);
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        PostResponseDto responseDto = mapper.convertValue(getResponseEntity.getBody().getData(),PostResponseDto.class);

        assertThat(responseDto.getId()).isEqualTo(selectedPost.getId());
        assertThat(responseDto.getTitle()).isEqualTo(updatedTitle);
        assertThat(responseDto.getContent()).isEqualTo(updatedContent);
        assertThat(responseDto.getPureText()).isEqualTo(pureText);
        assertThat(responseDto.getTagTypes().size()).isEqualTo(3);
    }
}
