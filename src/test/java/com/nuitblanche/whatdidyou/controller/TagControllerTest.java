package com.nuitblanche.whatdidyou.controller;

import com.nuitblanche.whatdidyou.controller.dto.ProjectSaveRequestDto;
import com.nuitblanche.whatdidyou.controller.dto.TagResponseDto;
import com.nuitblanche.whatdidyou.controller.dto.TagSaveRequestDto;
import com.nuitblanche.whatdidyou.domain.project.Project;
import com.nuitblanche.whatdidyou.domain.tag.Tag;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TagControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    private String token;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup(){

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
    }

    @After
    public void cleanup(){
        tagRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void saveTag(){

        TagType tagType = TagType.IT;

        TagSaveRequestDto requestDto = TagSaveRequestDto.builder()
                .tagType(tagType)
                .build();

        String url = "http://localhost:" + port + "/api/v1/tags";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+token);
        HttpEntity<TagSaveRequestDto> requestEntity = new HttpEntity<>(requestDto,headers);

        ResponseEntity<SingleResult> responseEntity = restTemplate.exchange(url, HttpMethod.POST,requestEntity,SingleResult.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getData()).isNotEqualTo(Long.valueOf(0L));

        List<Tag> tags = tagRepository.findAll();
        assertThat(tags.get(0).getTagType()).isEqualTo(tagType);
    }

    @Test
    public void getTag(){

        TagType tagType = TagType.IT;

        TagSaveRequestDto requestDto = TagSaveRequestDto.builder()
                .tagType(tagType)
                .build();

        String url = "http://localhost:" + port + "/api/v1/tags";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization","Bearer "+token);
        HttpEntity<TagSaveRequestDto> requestEntity = new HttpEntity<>(requestDto,headers);

        ResponseEntity<SingleResult> postResponseEntity = restTemplate.exchange(url, HttpMethod.POST,requestEntity,SingleResult.class);

        assertThat(postResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(postResponseEntity.getBody().getData()).isNotEqualTo(Long.valueOf(0L));

        List<Tag> tags = tagRepository.findAll();
        assertThat(tags.get(0).getTagType()).isEqualTo(tagType);

        HttpEntity<Object> getRequestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<SingleResult> getResponseEntity = restTemplate.
                exchange(url + "/IT" ,HttpMethod.GET,getRequestEntity, SingleResult.class);

        assertThat(getResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}
