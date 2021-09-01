package com.nuitblanche.whatdidyou.domain.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("SELECT p FROM Post p WHERE p.user.id =?1 ORDER BY p.title")
    List<Project> findAllByUserIdOrderByTitleASC(Long userId);
}
