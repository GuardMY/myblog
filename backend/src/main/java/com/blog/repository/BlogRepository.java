package com.blog.repository;

import com.blog.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByStatusOrderByPublishedAtDesc(String status);
    Page<Blog> findByStatusOrderByPublishedAtDesc(String status, Pageable pageable);
    List<Blog> findByAuthorIdOrderByCreatedAtDesc(Long authorId);
    Page<Blog> findByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);
    List<Blog> findByTitleContainingOrContentContainingOrderByPublishedAtDesc(String title, String content);
    Page<Blog> findByTitleContainingOrContentContainingOrderByPublishedAtDesc(String title, String content, Pageable pageable);

    // 按分类查询
    List<Blog> findByCategoryIdAndStatusOrderByPublishedAtDesc(Long categoryId, String status);
    Page<Blog> findByCategoryIdAndStatusOrderByPublishedAtDesc(Long categoryId, String status, Pageable pageable);

    // 按标签查询
    @Query("SELECT b FROM Blog b JOIN b.tags t WHERE t.id = :tagId AND b.status = 'PUBLISHED' ORDER BY b.publishedAt DESC")
    List<Blog> findByTagIdAndStatusPublished(@Param("tagId") Long tagId);

    @Query("SELECT b FROM Blog b JOIN b.tags t WHERE t.id = :tagId AND b.status = 'PUBLISHED' ORDER BY b.publishedAt DESC")
    Page<Blog> findByTagIdAndStatusPublished(@Param("tagId") Long tagId, Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.status = 'PUBLISHED' AND (b.title LIKE %:keyword% OR b.content LIKE %:keyword%) ORDER BY b.publishedAt DESC")
    List<Blog> searchPublishedBlogs(@Param("keyword") String keyword);

    @Query("SELECT b FROM Blog b WHERE b.status = 'PUBLISHED' AND (b.title LIKE %:keyword% OR b.content LIKE %:keyword%) ORDER BY b.publishedAt DESC")
    Page<Blog> searchPublishedBlogs(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT b FROM Blog b ORDER BY b.viewCount DESC LIMIT :limit")
    List<Blog> findTopByViewCount(@Param("limit") int limit);
}