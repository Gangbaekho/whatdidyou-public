package com.nuitblanche.whatdidyou.response;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {
    
    @ApiModelProperty(value = "응답 성공여부 : true/false")
    private boolean success;
    
    @ApiModelProperty(value = "응답 코드 번호: >= 0 정상, < 0 비정상")
    private int code;
    
    @ApiModelProperty(value = "응답 메세지")
    private String msg;

    @ApiModelProperty(value = "상세 에러 메세지")
    private String description;
}
