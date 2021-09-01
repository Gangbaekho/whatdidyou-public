package com.nuitblanche.whatdidyou.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {

    @Query(value = "SELECT t FROM Tag t where t.tagType =:tagType")
    Tag findByTagType(@Param("tagType") TagType tagType);

    @Query(value = "SELECT t FROM Tag t WHERE t.tagType IN (:tagTypes)")
    List<Tag> findByTagTypesWithInClause(@Param("tagTypes") List<TagType> tagTypes);
}
