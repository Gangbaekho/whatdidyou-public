package com.nuitblanche.whatdidyou.domain.tag;


import com.nuitblanche.whatdidyou.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Tag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TagType tagType;

    @Builder
    public Tag(TagType tagType) {
        this.tagType = tagType;
    }
}
