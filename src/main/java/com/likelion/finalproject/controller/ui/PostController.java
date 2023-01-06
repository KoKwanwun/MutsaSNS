package com.likelion.finalproject.controller.ui;

import com.likelion.finalproject.domain.dto.post.PostDto;
import com.likelion.finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping()
    public String list(Model model) {
        List<PostDto> posts = postService.printPostsList();
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
