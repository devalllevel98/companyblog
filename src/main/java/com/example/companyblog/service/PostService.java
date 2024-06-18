package com.example.companyblog.service;

import com.example.companyblog.entity.Post;
import java.util.List;
import java.util.Optional;

public interface PostService {
    List<Post> getAllPosts();
    Optional<Post> getPostById(Long id);
    Post createPost(Post post);
    Post updatePost(Long id, Post postDetails);
    void deletePost(Long id);
    List<Post> searchPosts(String title);
}