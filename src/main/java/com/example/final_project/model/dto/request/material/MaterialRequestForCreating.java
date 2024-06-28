package com.example.final_project.model.dto.request.material;

import com.alibaba.fastjson2.JSONObject;
import com.example.final_project.model.constant.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialRequestForCreating {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String materialName;
    @NotNull
    @Positive
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
    @NotNull
    @Positive
    private Integer eventId;
    private JSONObject supporters;
}
