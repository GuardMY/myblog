package com.blog.service;

import com.blog.entity.Blog;
import com.blog.entity.User;
import com.blog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private BlogRepository blogRepository;

    private static final String UPLOAD_DIR = "uploads/";

    public Blog create(Blog blog, User author) {
        blog.setAuthor(author);
        return blogRepository.save(blog);
    }

    public Blog update(Long id, Blog blog, User author) {
        Blog existingBlog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        if (!existingBlog.getAuthor().getId().equals(author.getId())) {
            throw new RuntimeException("You don't have permission to update this blog");
        }
        blog.setId(id);
        blog.setAuthor(author);
        blog.setCreatedAt(existingBlog.getCreatedAt());
        return blogRepository.save(blog);
    }

    public void delete(Long id, User user) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        if (!blog.getAuthor().getId().equals(user.getId()) && !user.getRole().equals("ADMIN")) {
            throw new RuntimeException("You don't have permission to delete this blog");
        }
        blogRepository.deleteById(id);
    }

    @Cacheable(value = "blogs", key = "#id")
    public Blog findById(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        blog.setViewCount(blog.getViewCount() + 1);
        blogRepository.save(blog);
        return blog;
    }

    @Cacheable(value = "blogs", key = "'allPublished'")
    public List<Blog> findAllPublished() {
        return blogRepository.findByStatusOrderByPublishedAtDesc("PUBLISHED");
    }

    @Cacheable(value = "blogs", key = "'allPublished_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Blog> findAllPublished(Pageable pageable) {
        return blogRepository.findByStatusOrderByPublishedAtDesc("PUBLISHED", pageable);
    }

    // 按分类查询
    @Cacheable(value = "blogs", key = "'byCategory_' + #categoryId")
    public List<Blog> findByCategory(Long categoryId) {
        return blogRepository.findByCategoryIdAndStatusOrderByPublishedAtDesc(categoryId, "PUBLISHED");
    }

    @Cacheable(value = "blogs", key = "'byCategory_' + #categoryId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Blog> findByCategory(Long categoryId, Pageable pageable) {
        return blogRepository.findByCategoryIdAndStatusOrderByPublishedAtDesc(categoryId, "PUBLISHED", pageable);
    }

    // 按标签查询
    @Cacheable(value = "blogs", key = "'byTag_' + #tagId")
    public List<Blog> findByTag(Long tagId) {
        return blogRepository.findByTagIdAndStatusPublished(tagId);
    }

    @Cacheable(value = "blogs", key = "'byTag_' + #tagId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<Blog> findByTag(Long tagId, Pageable pageable) {
        return blogRepository.findByTagIdAndStatusPublished(tagId, pageable);
    }

    public List<Blog> findByAuthor(User author) {
        return blogRepository.findByAuthorIdOrderByCreatedAtDesc(author.getId());
    }

    public Page<Blog> findByAuthor(User author, Pageable pageable) {
        return blogRepository.findByAuthorIdOrderByCreatedAtDesc(author.getId(), pageable);
    }

    public Page<Blog> search(String keyword, Pageable pageable) {
        return blogRepository.searchPublishedBlogs(keyword, pageable);
    }

    public Blog publish(Long id, User author) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        if (!blog.getAuthor().getId().equals(author.getId())) {
            throw new RuntimeException("You don't have permission to publish this blog");
        }
        blog.setStatus("PUBLISHED");
        blog.setPublishedAt(LocalDateTime.now());
        return blogRepository.save(blog);
    }

    public List<Blog> search(String keyword) {
        return blogRepository.searchPublishedBlogs(keyword);
    }

    public List<Blog> findTopViewed(int limit) {
        return blogRepository.findTopByViewCount(limit);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + fileName);
        Files.write(path, file.getBytes());
        return "/" + UPLOAD_DIR + fileName;
    }
}