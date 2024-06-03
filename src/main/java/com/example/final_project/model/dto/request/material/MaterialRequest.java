package com.example.final_project.model.dto.request.material;

import com.alibaba.fastjson2.JSONObject;
import com.example.final_project.model.Event;
import com.example.final_project.model.Member;
import com.example.final_project.model.constant.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @NotNull
    @NotBlank
    private String remark;
    @NotNull
    @NotBlank
    private Status status;
    private LocalDate assignDate;
    private LocalDate dueDate;
    @NotNull
    @NotBlank
    private Member handlerId;
    @NotNull
    private Event eventId;
    @NotNull
    private JSONObject supporters;
}