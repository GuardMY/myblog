package com.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blog.entity.Blog;
import com.blog.entity.User;
import com.blog.service.BlogService;
import com.blog.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Resource
    private BlogService blogService;

    @Resource
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createBlog(@RequestBody Blog blog, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Blog createdBlog = blogService.create(blog, user);
        return new ResponseEntity<>(createdBlog, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @RequestBody Blog blog, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        try {
            Blog updatedBlog = blogService.update(id, blog, user);
            return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        try {
            blogService.delete(id, user);
            return new ResponseEntity<>("Blog deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable Long id) {
        Blog blog = blogService.findById(id);
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllPublishedBlogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<Blog> blogs = blogService.findAllPublished(page, size);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("/my/blogs")
    public ResponseEntity<?> getMyBlogs(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        IPage<Blog> blogs = blogService.findByAuthor(user, page, size);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<?> publishBlog(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        try {
            Blog publishedBlog = blogService.publish(id, user);
            return new ResponseEntity<>(publishedBlog, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchBlogs(@RequestParam String keyword) {
        List<Blog> blogs = blogService.search(keyword);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopViewedBlogs(@RequestParam(defaultValue = "5") int limit) {
        List<Blog> blogs = blogService.findTopViewed(limit);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    // 按分类查询博客
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getBlogsByCategory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<Blog> blogs = blogService.findByCategory(id, page, size);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    // 按标签查询博客
    @GetMapping("/tag/{id}")
    public ResponseEntity<?> getBlogsByTag(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<Blog> blogs = blogService.findByTag(id, page, size);
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = blogService.uploadFile(file);
            return new ResponseEntity<>("{\"url\": \"" + filePath + "\"}", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}