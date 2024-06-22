package com.example.final_project.model;

import com.alibaba.fastjson2.JSONObject;
import com.example.final_project.model.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Material {
    private int materialId;
    private String materialName;
    private float qty;
    private String unit;
    private LocalDate assignDate;
    private LocalDate dueDate;
    private Integer handlerId;
    private String handlerName;
    private String picture;
    private JSONObject supporters;
    private Status status;
    private String remark;
}