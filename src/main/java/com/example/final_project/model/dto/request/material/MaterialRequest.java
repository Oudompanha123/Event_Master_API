package com.example.final_project.model.dto.request.material;

import com.alibaba.fastjson2.JSONObject;
import com.example.final_project.model.constant.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequest {
    @NotNull
    @NotBlank
    private String materialName;
    @NotNull
    @Positive
    private float qty;
    @NotNull
    @NotBlank
    private String unit;
    private String remark;
    @NotNull
    private Status status;
    private LocalDate assignDate;
    private LocalDate dueDate;
    @NotNull
    @Positive
    private Integer handlerId;
    @NotNull
    @Positive
    private Integer eventId;
    private JSONObject supporters;
}