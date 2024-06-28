package com.example.final_project.repository;

import com.example.final_project.model.Category;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CategoryRepository {
    @Select("""
        SELECT cate_id, cate_name, created_at, member.member_name as created_by FROM category INNER JOIN member ON
            category.created_by = member.member_id  WHERE category.org_id = #{orgId} ORDER BY update_at DESC LIMIT #{limit} OFFSET #{offset};
    """)
    @Results(id = "categoryMapper", value = {
            @Result(property = "categoryId", column = "cate_id"),
            @Result(property = "categoryName", column = "cate_name"),
            @Result(property = "createAt", column = "created_at"),
            @Result(property = "createBy", column = "created_by")
    })
    List<Category> getAllCategories(Integer orgId, Integer offset, Integer limit);

    @Select("""
        SELECT count(*) FROM category INNER JOIN member ON
        category.created_by = member.member_id  WHERE category.org_id = #{orgId};
    """)
    Integer getAllCategoryRecords(Integer orgId);

    @Select("""
        INSERT INTO category (cate_name, org_id, created_by)
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
        SELECT count(*) FROM event
        WHERE org_id = #{orgId} AND cate_id = #{categoryId};
    """)
    Integer countCategoryUseInEvent(Integer orgId, Integer categoryId);

    @Select("""
        DELETE FROM category WHERE cate_id = #{categoryId};
    """)
    void deleteCategory(Integer categoryId);

    @Select("""
        SELECT * FROM category WHERE cate_id = #{cateId} AND org_id = #{orgId};
    """)
    @ResultMap("categoryMapper")
    Category getCategoryById(@Param("cateId") Integer cateId, @Param("orgId") Integer orgId);

    @Select("""
        UPDATE category SET cate_name = #{categoryName}, update_at = current_timestamp WHERE cate_id = #{categoryId} RETURNING *;
    """)
    @ResultMap("categoryMapper")
    Category updateCategoryById(Integer categoryId, String categoryName);

    @Select("""
        SELECT cate_id, cate_name, created_at, created_by
            FROM category WHERE cate_name = #{cateName} AND org_id = #{orgId};
    """)
    @ResultMap("categoryMapper")
    Category getCategoryByName(String cateName, Integer orgId);

    @Select("""
        SELECT * FROM category WHERE cate_id = #{cateId};
    """)
    @ResultMap("categoryMapper")
    Category getCategoryByCatId(@Param("cateId") Integer cateId);
}
