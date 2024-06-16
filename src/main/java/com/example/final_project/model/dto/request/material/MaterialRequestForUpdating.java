package com.example.final_project.model.dto.request.material;

import com.alibaba.fastjson2.JSONObject;
import com.example.final_project.model.constant.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialRequestForUpdating {
    @NotNull
    @NotBlank
    private String materialName;
    @NotNull
    @Positive
    private float qty;
    @NotNull
    private float toGet;
    @NotNull
    @NotBlank
    private String unit;
    @NotNull
    private Status status;
    private LocalDate dueDate;
    @NotNull
    @Positive
    private Integer handlerId;
    @NotNull
    @Positive
    private JSONObject supporters;
}

