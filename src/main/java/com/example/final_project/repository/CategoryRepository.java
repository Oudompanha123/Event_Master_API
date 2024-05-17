package com.example.final_project.repository;

import com.example.final_project.model.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryRepository {
    @Select("""
        SELECT cate_id, cate_name, create_at, member.role as create_by FROM category INNER JOIN member ON
            category.create_by = member.member_id  WHERE category.org_id = #{orgId};
    """)
    @Results(id = "categoryMapper", value = {
            @Result(property = "categoryId", column = "cate_id"),
            @Result(property = "categoryName", column = "cate_name"),
            @Result(property = "createAt", column = "create_at"),
            @Result(property = "createBy", column = "create_by")

    })
    List<Category> getAllCategories(Integer orgId);

    @Select("""
        INSERT INTO category (cate_name, org_id, create_by)
        VALUES (#{categoryName}, #{orgIdByToken}, #{memberIdByToken})
        RETURNING *
    """)
    @ResultMap("categoryMapper")
    Category createCategory(String categoryName, Integer orgIdByToken, Integer memberIdByToken);

    @Select("""
        SELECT cate_name FROM category WHERE org_id = #{orgId}
    """)
    List<String> getAllCategoryName(Integer orgId);

    @Select("""
        SELECT count(*) FROM registration_form
        WHERE org_id = #{orgId} AND cate_id = #{categoryId};
    """)
    Integer countCategoryUseInRegistrationForm(Integer orgId, Integer categoryId);

    @Select("""
        SELECT count(*) FROM event
        WHERE org_id = #{orgId} AND cate_id = #{categoryId};
    """)
    Integer countCategoryUseInEvent(Integer orgId, Integer categoryId);

    @Select("""
        DELETE FROM category WHERE cate_id = #{categoryId};
    """)
    void deleteCategory(Integer categoryId);

    @Select("""
        SELECT * FROM category WHERE org_id = #{orgId} AND cate_id = #{categoryId};
    """)
    @ResultMap("categoryMapper")
    Category getCategoryById(Integer orgId ,Integer categoryId);

    @Select("""
        UPDATE category SET cate_name = #{categoryName} WHERE cate_id = #{categoryId} RETURNING *;
    """)
    @ResultMap("categoryMapper")
    Category updateCategoryById(Integer categoryId, String categoryName);
}
