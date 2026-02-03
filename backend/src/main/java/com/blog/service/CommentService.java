package com.blog.service;

import com.blog.entity.Comment;
import com.blog.entity.User;
import com.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment create(Comment comment, User user) {
        comment.setUser(user);
        return commentRepository.save(comment);
    }

    public Comment update(Long id, Comment comment, User user) {
        Comment existingComment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!existingComment.getUser().getId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("You don't have permission to update this comment");
        }
        comment.setId(id);
        comment.setUser(user);
        comment.setBlog(existingComment.getBlog());
        comment.setCreatedAt(existingComment.getCreatedAt());
        return commentRepository.save(comment);
    }

    public void delete(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        if (!comment.getUser().getId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("You don't have permission to delete this comment");
        }
        commentRepository.deleteById(id);
    }

    public List<Comment> findByBlogId(Long blogId) {
        return commentRepository.findByBlogIdOrderByCreatedAtDesc(blogId);
    }

    public List<Comment> findByUserId(Long userId) {
        return commentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
    }
}