package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.dto.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @PostMapping
    public String registerPost(){
        return "Post Test";
    }
}
