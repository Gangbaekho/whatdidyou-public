package com.nuitblanche.whatdidyou.domain.post;

import com.nuitblanche.whatdidyou.controller.dto.DayPostResponseDto;
import com.nuitblanche.whatdidyou.controller.dto.YearPostResponseDto;
import com.nuitblanche.whatdidyou.controller.dto.MonthPostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("SELECT p FROM Post p ORDER BY p.id DESC")
    List<Post> findAllDesc();

    @Query("SELECT p FROM Post p ORDER BY p.id DESC")
    Page<Post> findCurrentPagePosts(Pageable pageable);

    @Query("SELECT new com.nuitblanche.whatdidyou.controller.dto.YearPostResponseDto(p.user.id, cast(p.createdDate as LocalDate) as day, count(*) as count) FROM Post p WHERE p.user.id=:userId AND cast(p.createdDate as LocalDate) BETWEEN :startDate AND :endDate GROUP BY cast(p.createdDate as LocalDate)")
     List<YearPostResponseDto> findYearlyInformation(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT new com.nuitblanche.whatdidyou.controller.dto.DayPostResponseDto(p.project.id,p.project.title, count(*) as postCount) FROM Post p WHERE p.user.id=:userId AND cast(p.createdDate as LocalDate)=:day GROUP BY p.project.id")
     List<DayPostResponseDto> findDailyInformation(@Param("userId") Long userId, @Param("day") LocalDate day);

    @Query("SELECT new com.nuitblanche.whatdidyou.controller.dto.MonthPostResponseDto(p.project.id,p.project.title, count(*) as postCount) FROM Post p WHERE p.user.id=:userId AND cast(p.createdDate as LocalDate) BETWEEN :from AND :to GROUP BY p.project.id")
    List<MonthPostResponseDto> findMothlyInformation(@Param("userId") Long userId, @Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("SELECT p FROM Post p WHERE p.project.id=:projectId ORDER BY p.id DESC")
    List<Post> findAllByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT p FROM Post p WHERE p.project.id=:projectId ORDER BY p.id DESC")
    Page<Post> findCurrentPagePostsInProject(@Param("projectId") Long projectId, Pageable pageable);
}
