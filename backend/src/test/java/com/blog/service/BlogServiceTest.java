package com.blog.service;

import com.blog.entity.Blog;
import com.blog.entity.User;
import com.blog.repository.BlogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogService blogService;

    @Test
    public void testCreateBlog() {
        User author = new User();
        author.setId(1L);
        author.setUsername("testuser");

        Blog blog = new Blog();
        blog.setTitle("Test Blog");
        blog.setContent("Test content");

        when(blogRepository.save(any(Blog.class))).thenReturn(blog);

        Blog createdBlog = blogService.create(blog, author);

        assertNotNull(createdBlog);
        assertEquals("Test Blog", createdBlog.getTitle());
        assertEquals(author, createdBlog.getAuthor());
        verify(blogRepository, times(1)).save(blog);
    }

    @Test
    public void testFindById() {
        Blog blog = new Blog();
        blog.setId(1L);
        blog.setTitle("Test Blog");
        blog.setViewCount(0);

        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));
        when(blogRepository.save(any(Blog.class))).thenReturn(blog);

        Blog foundBlog = blogService.findById(1L);

        assertNotNull(foundBlog);
        assertEquals(1, foundBlog.getViewCount());
        verify(blogRepository, times(1)).findById(1L);
        verify(blogRepository, times(1)).save(blog);
    }

    @Test
    public void testFindAllPublished() {
        List<Blog> blogs = new ArrayList<>();
        Blog blog1 = new Blog();
        blog1.setId(1L);
        blog1.setTitle("Blog 1");
        blogs.add(blog1);

        when(blogRepository.findByStatusOrderByPublishedAtDesc("PUBLISHED")).thenReturn(blogs);

        List<Blog> foundBlogs = blogService.findAllPublished();

        assertNotNull(foundBlogs);
        assertEquals(1, foundBlogs.size());
        verify(blogRepository, times(1)).findByStatusOrderByPublishedAtDesc("PUBLISHED");
    }

    @Test
    public void testFindAllPublishedWithPagination() {
        List<Blog> blogs = new ArrayList<>();
        Blog blog1 = new Blog();
        blog1.setId(1L);
        blog1.setTitle("Blog 1");
        blogs.add(blog1);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Blog> page = new PageImpl<>(blogs, pageable, blogs.size());

        when(blogRepository.findByStatusOrderByPublishedAtDesc("PUBLISHED", pageable)).thenReturn(page);

        Page<Blog> foundPage = blogService.findAllPublished(pageable);

        assertNotNull(foundPage);
        assertEquals(1, foundPage.getContent().size());
        verify(blogRepository, times(1)).findByStatusOrderByPublishedAtDesc("PUBLISHED", pageable);
    }
}
