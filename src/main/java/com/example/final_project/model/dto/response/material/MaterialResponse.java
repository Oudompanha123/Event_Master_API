package com.example.final_project.model.dto.response.material;

import com.example.final_project.model.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialResponse {
    private int materialId;
    private String materialName;
    private float qty;
    private String unit;
    private LocalDate dueDate;
    private Integer handlerId;
    private String handlerName;
    private String picture;
    private List<JSONObject> supporters;
    private Status status;
    private String remark;
}