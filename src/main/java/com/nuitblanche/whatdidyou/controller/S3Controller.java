package com.nuitblanche.whatdidyou.controller;

import com.nuitblanche.whatdidyou.aws.S3Uploader;
import com.nuitblanche.whatdidyou.response.SingleResult;
import com.nuitblanche.whatdidyou.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.apache.http.protocol.ResponseServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
public class S3Controller {

    private final S3Uploader s3Uploader;
    private final ResponseService responseService;

    @PostMapping("/api/v1/upload")
    public SingleResult<String> upload(@RequestParam(name = "data") MultipartFile multipartFile) throws IOException{
        return responseService.getSingleResult(s3Uploader.upload(multipartFile, "static"));
    }


}
