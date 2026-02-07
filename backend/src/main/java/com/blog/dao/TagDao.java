package com.blog.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.dao.mapper.TagMapper;
import com.blog.entity.Tag;
import org.springframework.stereotype.Repository;

@Repository
public class TagDao extends ServiceImpl<TagMapper, Tag> {

}
