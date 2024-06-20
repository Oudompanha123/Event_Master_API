package com.example.final_project.model.dto.response;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaResponse {
    private Integer agendaId;
    private JSONObject data;
    private Integer eventId;
}
