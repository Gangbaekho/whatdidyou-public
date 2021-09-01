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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerFindCurrentPagePostsTest {
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

        ProjectSaveRequestDto projectTworequestDto = ProjectSaveRequestDto.builder()
                .title("project-two")
                .build();

        Project projectTwo = projectTworequestDto.toEntity();
        projectTwo.updateUser(selectedUser);

        projectRepository.save(projectOne);
        projectRepository.save(projectTwo);

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
    public void findCurrentPagePostsInProject(){
        Project projectOne = projectRepository.findAll().get(0);
        Project projectTwo = projectRepository.findAll().get(1);

        String title = "title";
        String content = "content";
        String pureText = "pureText";
        List<TagType> tagTypes = Arrays.asList(TagType.IT,TagType.SOCIETY,TagType.SCIENCE);

        PostSaveRequestDto requestDtoOne = PostSaveRequestDto.builder()
                .title(title)
                .content(content)
                .pureText(pureText)
                .tagTypes(tagTypes)
                .projectId(projectOne.getId())
                .build();

        PostSaveRequestDto requestDtoTwo = PostSaveRequestDto.builder()
                .title(title)
                .content(content)
                .pureText(pureText)
                .tagTypes(tagTypes)
                .projectId(projectTwo.getId())
                .build();

        String postUrl = "http://localhost:" + port + "/api/v1/posts";

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.setContentType(MediaType.APPLICATION_JSON);
        postHeaders.set("Authorization","Bearer "+token);
        HttpEntity<PostSaveRequestDto> postRequestEntity = new HttpEntity<>(requestDtoOne,postHeaders);
        HttpEntity<PostSaveRequestDto> postRequestEntityTwo = new HttpEntity<>(requestDtoTwo,postHeaders);

        for(int i = 0 ; i < 9 ; i++){
            restTemplate.exchange(postUrl, HttpMethod.POST,postRequestEntity,SingleResult.class);
        }
        for(int i = 0 ; i < 9 ; i++){
            restTemplate.exchange(postUrl, HttpMethod.POST,postRequestEntityTwo,SingleResult.class);
        }

        String getUrl =  "http://localhost:" + port + "/api/v1/posts/pages/"+1L;

        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.setContentType(MediaType.APPLICATION_JSON);
        getHeaders.set("Authorization","Bearer "+token);
        HttpEntity<Object> updateRequestEntity = new HttpEntity<>(null,getHeaders);

        ResponseEntity<SingleResult> getResponseEntity = restTemplate.exchange(getUrl, HttpMethod.GET,updateRequestEntity,SingleResult.class);
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        PagePostResponseDto responseDto = mapper.convertValue(getResponseEntity.getBody().getData(), PagePostResponseDto.class);

        assertThat(responseDto.getPosts().size()).isEqualTo(8);
    }
}
