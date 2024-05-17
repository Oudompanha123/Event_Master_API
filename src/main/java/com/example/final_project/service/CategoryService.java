package com.example.final_project.service;

import com.example.final_project.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category createCategory(String categoryName);

    void deleteCategoryById(Integer categoryId);

    Category updateCategory(Integer categoryId, String categoryName);
}
