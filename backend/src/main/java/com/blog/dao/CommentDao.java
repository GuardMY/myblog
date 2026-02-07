package com.blog.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dao.mapper.CommentMapper;
import com.blog.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDao extends ServiceImpl<CommentMapper, Comment> {

}
