package com.blog.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dao.mapper.BlogMapper;
import com.blog.entity.Blog;
import org.springframework.stereotype.Repository;

@Repository
public class BlogDao extends ServiceImpl<BlogMapper, Blog> {
    
}
