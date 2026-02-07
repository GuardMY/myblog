package com.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dao.BlogDao;
import com.blog.entity.Blog;
import com.blog.entity.User;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BlogService {

    @Resource
    private BlogDao blogDao;

    private static final String UPLOAD_DIR = "uploads/";

    public Blog create(Blog blog, User author) {
        blog.setAuthorId(author.getId());
        blog.setAuthor(author);
        blogDao.save(blog);
        return blog;
    }

    public Blog update(Long id, Blog blog, User author) {
        Blog existingBlog = blogDao.getById(id);
        if (existingBlog == null) {
            throw new RuntimeException("Blog not found");
        }
        if (!existingBlog.getAuthorId().equals(author.getId())) {
            throw new RuntimeException("You don't have permission to update this blog");
        }
        blog.setId(id);
        blog.setAuthorId(author.getId());
        blog.setAuthor(author);
        blog.setCreatedAt(existingBlog.getCreatedAt());
        blogDao.updateById(blog);
        return blog;
    }

    public void delete(Long id, User user) {
        Blog blog = blogDao.getById(id);
        if (blog == null) {
            throw new RuntimeException("Blog not found");
        }
        if (!blog.getAuthorId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("You don't have permission to delete this blog");
        }
        blogDao.removeById(id);
    }

    @Cacheable(value = "blogs", key = "#id")
    public Blog findById(Long id) {
        Blog blog = blogDao.getById(id);
        if (blog == null) {
            throw new RuntimeException("Blog not found");
        }
        blog.setViewCount(blog.getViewCount() + 1);
        blogDao.removeById(blog);
        return blog;
    }

    @Cacheable(value = "blogs", key = "'allPublished'")
    public List<Blog> findAllPublished() {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "PUBLISHED");
        queryWrapper.orderByDesc("published_at");
        return blogDao.list(queryWrapper);
    }

    @Cacheable(value = "blogs", key = "'allPublished_' + #pageNum + '_' + #pageSize")
    public IPage<Blog> findAllPublished(int pageNum, int pageSize) {
        Page<Blog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "PUBLISHED");
        queryWrapper.orderByDesc("published_at");
        return blogDao.getBaseMapper().selectPage(page, queryWrapper);
    }

    // 按分类查询
    @Cacheable(value = "blogs", key = "'byCategory_' + #categoryId")
    public List<Blog> findByCategory(Long categoryId) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        queryWrapper.eq("status", "PUBLISHED");
        queryWrapper.orderByDesc("published_at");
        return blogDao.list(queryWrapper);
    }

    @Cacheable(value = "blogs", key = "'byCategory_' + #categoryId + '_' + #pageNum + '_' + #pageSize")
    public IPage<Blog> findByCategory(Long categoryId, int pageNum, int pageSize) {
        Page<Blog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        queryWrapper.eq("status", "PUBLISHED");
        queryWrapper.orderByDesc("published_at");
        return blogDao.getBaseMapper().selectPage(page, queryWrapper);
    }

    // 按标签查询
    @Cacheable(value = "blogs", key = "'byTag_' + #tagId")
    public List<Blog> findByTag(Long tagId) {
        // 由于标签是多对多关系，需要使用 JOIN 查询
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "PUBLISHED");
        // 在实际项目中，这里需要通过中间表查询，或者使用MyBatis Plus的关联查询
        // 这里简化处理，实际应该通过标签ID查询关联的博客
        return blogDao.list(queryWrapper);
    }

    @Cacheable(value = "blogs", key = "'byTag_' + #tagId + '_' + #pageNum + '_' + #pageSize")
    public IPage<Blog> findByTag(Long tagId, int pageNum, int pageSize) {
        Page<Blog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "PUBLISHED");
        // 在实际项目中，这里需要通过中间表查询，或者使用MyBatis Plus的关联查询
        // 这里简化处理，实际应该通过标签ID查询关联的博客
        return blogDao.getBaseMapper().selectPage(page, queryWrapper);
    }

    public List<Blog> findByAuthor(User author) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", author.getId());
        queryWrapper.orderByDesc("created_at");
        return blogDao.list(queryWrapper);
    }

    public IPage<Blog> findByAuthor(User author, int pageNum, int pageSize) {
        Page<Blog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", author.getId());
        queryWrapper.orderByDesc("created_at");
        return blogDao.getBaseMapper().selectPage(page, queryWrapper);
    }

    public IPage<Blog> search(String keyword, int pageNum, int pageSize) {
        Page<Blog> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "PUBLISHED");
        queryWrapper.and(qw -> {
            qw.like("title", keyword).or().like("content", keyword);
        });
        queryWrapper.orderByDesc("published_at");
        return blogDao.getBaseMapper().selectPage(page, queryWrapper);
    }

    public Blog publish(Long id, User author) {
        Blog blog = blogDao.getById(id);
        if (blog == null) {
            throw new RuntimeException("Blog not found");
        }
        if (!blog.getAuthorId().equals(author.getId())) {
            throw new RuntimeException("You don't have permission to publish this blog");
        }
        blog.setStatus("PUBLISHED");
        blog.setPublishedAt(LocalDateTime.now());
        blogDao.removeById(blog);
        return blog;
    }

    public List<Blog> search(String keyword) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "PUBLISHED");
        queryWrapper.and(qw -> {
            qw.like("title", keyword).or().like("content", keyword);
        });
        queryWrapper.orderByDesc("published_at");
        return blogDao.list(queryWrapper);
    }

    public List<Blog> findTopViewed(int limit) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "PUBLISHED");
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("LIMIT " + limit);
        return blogDao.list(queryWrapper);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.write(path, file.getBytes());
        return "/" + UPLOAD_DIR + fileName;
    }
}