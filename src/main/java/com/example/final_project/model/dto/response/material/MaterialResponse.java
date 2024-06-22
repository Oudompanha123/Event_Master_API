package com.example.final_project.model.dto.response.material;

import com.alibaba.fastjson2.JSONObject;
import com.example.final_project.model.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialResponse {
    private int materialId;
    private String materialName;
    private float qty;
    private String unit;
    private LocalDate assignDate;
    private LocalDate dueDate;
    private JSONObject supporters;
    private Status status;
    private String remark;
}
