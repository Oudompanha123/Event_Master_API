package com.example.final_project.model.dto.request.material;

import com.alibaba.fastjson2.JSONObject;
import com.example.final_project.model.constant.Status;
import jakarta.validation.constraints.*;
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
    @Size(min = 1, max = 50)
    private String materialName;
    @NotNull
    @Positive
    private Integer eventId;
    @NotNull
    @PositiveOrZero
    private float qty;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 20)
    private String unit;
    @NotNull
    private Status status;
    private LocalDate dueDate;
    @Positive
    private Integer handlerId;
    private JSONObject supporters;
}

