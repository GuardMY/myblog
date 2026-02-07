package com.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@TableName(value = "blogs")
@Data
public class Blog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String content;

    private String markdownContent;

    private String featuredImage;

    private String status = "DRAFT";

    private Integer viewCount = 0;

    private Long authorId;
    private User author;

    private Long categoryId;
    private Category category;

    private List<Tag> tags;
    private List<Comment> comments;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updatedAt;
    
    private LocalDateTime publishedAt;
}