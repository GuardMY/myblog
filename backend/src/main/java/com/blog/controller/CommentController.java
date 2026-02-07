package com.blog.controller;

import com.blog.entity.Comment;
import com.blog.entity.User;
import com.blog.service.CommentService;
import com.blog.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        Comment createdComment = commentService.create(comment, user);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody Comment comment, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        try {
            Comment updatedComment = commentService.update(id, comment, user);
            return new ResponseEntity<>(updatedComment, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByUsername(authentication.getName()).orElseThrow();
        try {
            commentService.delete(id, user);
            return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/blog/{blogId}")
    public ResponseEntity<?> getCommentsByBlog(@PathVariable Long blogId) {
        List<Comment> comments = commentService.findByBlogId(blogId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCommentsByUser(@PathVariable Long userId) {
        List<Comment> comments = commentService.findByUserId(userId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentById(@PathVariable Long id) {
        Comment comment = commentService.findById(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }
}