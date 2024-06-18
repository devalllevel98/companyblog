package com.example.companyblog.controller;


import com.example.companyblog.entity.Post;
import com.example.companyblog.exception.PostNotFoundException;
import com.example.companyblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        try {
            Long postId = Long.parseLong(id);
            Optional<Post> post = postService.getPostById(postId);
            return post.map(ResponseEntity::ok)
                    .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid id format: " + id);
        }
    }

    @PostMapping("")
    public ResponseEntity<Post> createPost(@RequestParam("title") String title,
                                           @RequestParam("content") String content,
                                           @RequestParam("image") MultipartFile image) throws IOException {
        // Lưu ảnh vào thư mục trên server
        String uploadDir = "./uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Lưu file ảnh
        Path filePath = Paths.get(uploadDir + image.getOriginalFilename());
        Files.write(filePath, image.getBytes());

        // Tạo đường dẫn đầy đủ của ảnh
        String imgPath = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(image.getOriginalFilename())
                .toUriString();

        // Tạo bài viết mới và lưu vào CSDL
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setImg(imgPath);


        Post createdPost = postService.createPost(post);
        System.out.println("This í post: "+post.getImg());
        System.out.println("This is image");

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }


    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        return postService.updatePost(id, postDetails);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @GetMapping("/search")
    public List<Post> searchPosts(@RequestParam String title) {
        return postService.searchPosts(title);
    }
}