package com.example.final_project.model.dto.request;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeRequest {
    private JSONObject data;
    @NotNull
    @Positive
    private Integer eventId;
}
