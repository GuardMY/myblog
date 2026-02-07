package com.blog.service.impl;

import com.blog.dao.CategoryDao;
import com.blog.entity.Category;
import com.blog.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryDao categoryDao;

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.list();
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryDao.getById(id);
        if (category == null) {
            throw new RuntimeException("Category not found");
        }
        return category;
    }

    @Override
    public Category createCategory(Category category) {
        categoryDao.save(category);
        return category;
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category existingCategory = categoryDao.getById(id);
        if (existingCategory == null) {
            throw new RuntimeException("Category not found");
        }
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        categoryDao.updateById(existingCategory);
        return existingCategory;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryDao.removeById(id);
    }
}
