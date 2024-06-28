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
    public List<Category> getAllCategories(Integer offset, Integer limit) {
        offset = (offset - 1) * limit;
        // get orgId by token
        Integer orgId = Token.getOrgIdByToken();
        return categoryRepository.getAllCategories(orgId, offset, limit);
    }

    @Override
    public Category createCategory(String categoryName) {
        categoryName = categoryName.trim();
        // check category name not duplicate
        List<String> categoryNameList = categoryRepository.getAllCategoryName(Token.getOrgIdByToken());
        for(String cateName : categoryNameList)
            if(cateName.equalsIgnoreCase(categoryName))
                throw new BadRequestException("Duplicate category name");
        return categoryRepository.createCategory(categoryName,
                Token.getOrgIdByToken(), Token.getMemberIdByToken());
    }

    @Override
    public void deleteCategoryById(Integer categoryId) {
        // check category exists or not
        if(categoryRepository.getCategoryById(categoryId, Token.getOrgIdByToken()) == null)
            throw new NotFoundException("Cannot find this category");
        // check category is used in event or not. If it uses, cannot delete
        if(categoryRepository.countCategoryUseInEvent(Token.getOrgIdByToken(), categoryId) > 0)
            throw new BadRequestException("Cannot delete this category because it is using in event table");

        categoryRepository.deleteCategory(categoryId);
    }

    @Override
    public Category updateCategory(Integer categoryId, String categoryName) {
        categoryName = categoryName.trim();
        // check category exists or not
        if(categoryRepository.getCategoryById(categoryId, Token.getOrgIdByToken()) == null)
            throw new NotFoundException("Cannot find this category");
        // check category name not duplicate
        List<String> categoryNameList = categoryRepository.getAllCategoryName(Token.getOrgIdByToken());
        for(String cateName : categoryNameList)
            if(cateName.equalsIgnoreCase(categoryName))
                throw new BadRequestException("Duplicate category name");
        return categoryRepository.updateCategoryById(categoryId, categoryName);
    }

    @Override
    public Integer getTotalCategoryRecords() {
        return categoryRepository.getAllCategoryRecords(Token.getOrgIdByToken());
    }

    @Override
    public Category getCategoryByCategoryName(String cateName) {
        if(categoryRepository.getCategoryByName(cateName, Token.getOrgIdByToken()) == null)
            throw new NotFoundException("Cannot find this category");

        return categoryRepository.getCategoryByName(cateName, Token.getOrgIdByToken());
    }
}
