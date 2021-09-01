package com.nuitblanche.whatdidyou.repository;

import com.nuitblanche.whatdidyou.controller.dto.TagSaveRequestDto;
import com.nuitblanche.whatdidyou.domain.tag.Tag;
import com.nuitblanche.whatdidyou.domain.tag.TagRepository;
import com.nuitblanche.whatdidyou.domain.tag.TagType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Before
    public void setUp(){

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

        tagRepository.deleteAll();
    }

    @Test
    @Transactional
    public void getMultipleTagsWithOneQuery(){

        List<TagType> tagTypes = new ArrayList<>();
        tagTypes.add(TagType.IT);
        tagTypes.add(TagType.SOCIETY);
        tagTypes.add(TagType.SCIENCE);

        List<Tag> tags = tagRepository.findByTagTypesWithInClause(tagTypes);
        assertThat(tags.size()).isEqualTo(3);
    }

}
