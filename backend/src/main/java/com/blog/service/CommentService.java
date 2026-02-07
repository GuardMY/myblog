package com.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.dao.CommentDao;
import com.blog.entity.Comment;
import com.blog.entity.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Resource
    private CommentDao commentDao;

    public Comment create(Comment comment, User user) {
        comment.setUser(user);
        commentDao.save(comment);
        return comment;
    }

    public Comment update(Long id, Comment comment, User user) {
        Comment existingComment = commentDao.getById(id);
        if (existingComment == null) {
            throw new RuntimeException("Comment not found");
        }
        if (!existingComment.getUser().getId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("You don't have permission to update this comment");
        }
        comment.setId(id);
        comment.setUser(user);
        comment.setBlog(existingComment.getBlog());
        comment.setCreatedAt(existingComment.getCreatedAt());
        commentDao.removeById(comment);
        return comment;
    }

    public void delete(Long id, User user) {
        Comment comment = commentDao.getById(id);
        if (comment == null) {
            throw new RuntimeException("Comment not found");
        }
        if (!comment.getUser().getId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("You don't have permission to delete this comment");
        }
        commentDao.removeById(id);
    }

    public List<Comment> findByBlogId(Long blogId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("blog_id", blogId);
        queryWrapper.orderByDesc("created_at");
        return commentDao.list(queryWrapper);
    }

    public List<Comment> findByUserId(Long userId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("created_at");
        return commentDao.list(queryWrapper);
    }

    public Comment findById(Long id) {
        Comment comment = commentDao.getById(id);
        if (comment == null) {
            throw new RuntimeException("Comment not found");
        }
        return comment;
    }
}