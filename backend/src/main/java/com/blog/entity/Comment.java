package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@TableName(value = "comments")
@Data
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String content;

    private Long userId;
    private User user;

    private Long blogId;
    private Blog blog;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updatedAt;
}