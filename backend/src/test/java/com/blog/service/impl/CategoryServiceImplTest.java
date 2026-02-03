package com.blog.service.impl;

import com.blog.entity.Category;
import com.blog.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void testGetAllCategories() {
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Technology");
        categories.add(category1);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> foundCategories = categoryService.getAllCategories();

        assertNotNull(foundCategories);
        assertEquals(1, foundCategories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetCategoryById() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Technology");
        category.setDescription("Technology related blogs");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.getCategoryById(1L);

        assertNotNull(foundCategory);
        assertEquals("Technology", foundCategory.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateCategory() {
        Category category = new Category();
        category.setName("Technology");
        category.setDescription("Technology related blogs");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category createdCategory = categoryService.createCategory(category);

        assertNotNull(createdCategory);
        assertEquals("Technology", createdCategory.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testUpdateCategory() {
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("Tech");
        existingCategory.setDescription("Tech blogs");

        Category updatedCategory = new Category();
        updatedCategory.setName("Technology");
        updatedCategory.setDescription("Technology related blogs");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        Category result = categoryService.updateCategory(1L, updatedCategory);

        assertNotNull(result);
        assertEquals("Technology", result.getName());
        assertEquals("Technology related blogs", result.getDescription());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(existingCategory);
    }

    @Test
    public void testDeleteCategory() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
