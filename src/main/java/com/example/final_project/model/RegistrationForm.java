package com.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {
    private Integer formId;
    private String formName;
    private String description;
    private List<Map<String, Object>> data;
    private Category category;
}
