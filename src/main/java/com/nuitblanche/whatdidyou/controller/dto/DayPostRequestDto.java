package com.nuitblanche.whatdidyou.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class DayPostRequestDto {

    private Long userId;
    private LocalDate day;

    @Builder
    public DayPostRequestDto(Long userId, LocalDate day) {
        this.userId = userId;
        this.day = day;
    }
}
