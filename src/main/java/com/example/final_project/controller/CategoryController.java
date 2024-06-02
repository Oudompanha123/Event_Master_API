package com.example.final_project.controller;

import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/category")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get All categories")
    public ResponseEntity<?> getAllCategories(
            @RequestParam(defaultValue = "1") @Positive Integer offset,
            @RequestParam(defaultValue = "8") @Positive Integer limit
    ){
        return GetResponse.getResponse("Get all categories successfully", categoryService.getAllCategories(offset, limit));
    }

    @PostMapping
    @Operation(summary = "Create new category")
    public ResponseEntity<?> createCategory(@RequestParam @NotBlank @NotNull String categoryName){
        return PostResponse.postResponse("Create category successfully", categoryService.createCategory(categoryName));
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete category by id")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId){
        categoryService.deleteCategoryById(categoryId);
        return GetResponse.getResponse("Delete category id : " + categoryId + "  successfully", null);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update category by id")
    public ResponseEntity<?> updateCategory(@PathVariable Integer categoryId,
                                            @RequestParam @NotBlank @NotNull String categoryName){
        return UpdateResponse.updateResponse("Update category id : " + categoryId + " successfully"
                ,categoryService.updateCategory(categoryId, categoryName));
    }
}
