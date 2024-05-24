package com.example.final_project.repository;

import com.example.final_project.config.JsonbListMapTypeHandler;
import com.example.final_project.model.RegistrationForm;
import org.apache.ibatis.annotations.*;

@Mapper
public interface RegistrationFormRepository {
    @Select("""
        SELECT * FROM registration_form WHERE form_id = #{formId}
    """)
    @Results(id = "registrationFormMapper", value = {
            @Result(property = "formId", column = "form_id"),
            @Result(property = "formName", column = "form_name"),
            @Result(property = "category", column = "cate_id",
                    typeHandler = JsonbListMapTypeHandler.class,
                    one = @One(select = "com.example.final_project.repository.CategoryRepository.getCategoryById"))
    })
    RegistrationForm getRegistrationFormById(Integer formId);
}
