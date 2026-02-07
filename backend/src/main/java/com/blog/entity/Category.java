package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.List;

@Data
@TableName(value = "categories")
public class Category {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String description;

    private List<Blog> blogs;
}
