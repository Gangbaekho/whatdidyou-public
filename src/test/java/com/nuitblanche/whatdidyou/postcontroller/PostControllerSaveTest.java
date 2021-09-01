package com.nuitblanche.whatdidyou.postcontroller;

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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerSaveTest {

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
    public void savePost(){

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

        String url = "http://localhost:" + port + "/api/v1/posts";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+token);
        HttpEntity<PostSaveRequestDto> requestEntity = new HttpEntity<>(requestDto,headers);

        ResponseEntity<SingleResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST,requestEntity,SingleResult.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        Post post = postRepository.findAll().get(0);

        EntityManager em = factory.createEntityManager();
        try{
            em.getTransaction().begin();

            Post selectedPost = em.find(Post.class,post.getId());
            assertThat(selectedPost.getTitle()).isEqualTo(title);
            assertThat(selectedPost.getContent()).isEqualTo(content);
            assertThat(selectedPost.getPureText()).isEqualTo(pureText);
            assertThat(selectedPost.getPostTags().size()).isEqualTo(3);

            em.getTransaction().commit();
        } catch(Exception e){
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
