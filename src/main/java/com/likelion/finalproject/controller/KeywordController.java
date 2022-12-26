package com.likelion.finalproject.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
public class KeywordController {
    @ApiOperation(value = "문자 출력")
    @GetMapping()
    public String printHello(){
        return "happy_new_year";
    }
}
