package com.nuitblanche.whatdidyou.controller.dto;


import lombok.Getter;

import java.time.LocalDate;

@Getter
public class YearPostResponseDto {

    private Long id;
    private LocalDate day;
    private Long count;

    public YearPostResponseDto(Long id, LocalDate day, Long count) {
        this.id = id;
        this.day = day;
        this.count = count;
    }
}
