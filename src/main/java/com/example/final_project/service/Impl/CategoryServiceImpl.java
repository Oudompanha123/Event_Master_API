package com.example.final_project.service.Impl;


import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.Category;
import com.example.final_project.repository.CategoryRepository;
import com.example.final_project.service.CategoryService;
import com.example.final_project.util.Token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        // get orgId by token
        Integer orgId = Token.getOrgIdByToken();
        return categoryRepository.getAllCategories(orgId);
    }

    @Override
    public Category createCategory(String categoryName) {
        // check category name not duplicate
        List<String> categoryNameList = categoryRepository.getAllCategoryName(Token.getOrgIdByToken());
        if(categoryNameList.contains(categoryName))
            throw new BadRequestException("Duplicate category name");
        return categoryRepository.createCategory(categoryName,
                Token.getOrgIdByToken(), Token.getMemberIdByToken());
    }

    @Override
    public void deleteCategoryById(Integer categoryId) {
        // check category exists or not
        if(categoryRepository.getCategoryById(Token.getOrgIdByToken(), categoryId) == null)
            throw new NotFoundException("Cannot find this category");
        // check category is used in registration form or not. If it uses, cannot delete
        if(categoryRepository.countCategoryUseInRegistrationForm(Token.getOrgIdByToken(), categoryId) > 0)
            throw new BadRequestException("Cannot delete this category because it is using in registration form table");
        // check category is used in event or not. If it uses, cannot delete
        if(categoryRepository.countCategoryUseInEvent(Token.getOrgIdByToken(), categoryId) > 0)
            throw new BadRequestException("Cannot delete this category because it is using in event table");

        categoryRepository.deleteCategory(categoryId);
    }

    @Override
    public Category updateCategory(Integer categoryId, String categoryName) {
        // check category exists or not
        if(categoryRepository.getCategoryById(Token.getOrgIdByToken(), categoryId) == null)
            throw new NotFoundException("Cannot find this category");
        // check category name that want to update already exists in table or not
        List<String> categoryNameList = categoryRepository.getAllCategoryName(Token.getOrgIdByToken());
        if(categoryNameList.contains(categoryName))
            throw new BadRequestException("Duplicate category name");
        return categoryRepository.updateCategoryById(categoryId, categoryName);
    }
}
