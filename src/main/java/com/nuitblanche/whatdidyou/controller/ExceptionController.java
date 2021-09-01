package com.nuitblanche.whatdidyou.controller;

import com.nuitblanche.whatdidyou.advice.exception.CAuthenticationEntryPointException;
import com.nuitblanche.whatdidyou.response.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/exception")
@RestController
public class ExceptionController {

    @GetMapping("/entrypoint")
    public CommonResult entrypointException(){
        throw new CAuthenticationEntryPointException("해당 리소스에 접근하기 위한 권한이 없습니다.");
    }

    @GetMapping("/accessdenied")
    public CommonResult accessdeniedException(){
        throw new AccessDeniedException("보유한 권한으로 접글할 수 없는 리소스입니다.");
    }
}
