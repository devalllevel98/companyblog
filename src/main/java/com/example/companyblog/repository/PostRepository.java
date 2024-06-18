package com.example.companyblog.repository;

import com.example.companyblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String title);
}

// ProductRepository.java
