package com.blog.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dao.mapper.CategoryMapper;
import com.blog.entity.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao extends ServiceImpl<CategoryMapper, Category> {

}
