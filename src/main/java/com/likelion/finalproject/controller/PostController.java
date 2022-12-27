package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.dto.post.PostDto;
import com.likelion.finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping()
    public String list(Model model, @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> posts = postService.printPosts(pageable);
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/{id}")
    public String onePost(Model model, @PathVariable Long id) {
        PostDto post = postService.printOnePost(id);
        model.addAttribute("post", post);
        return "postInfo";
    }
}
