package com.example.final_project.model;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaResponse {
    private Integer agendaId;
    private List<JSONObject> data;
    private Integer eventId;
}
