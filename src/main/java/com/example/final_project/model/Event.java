package com.example.final_project.model;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event{
    private Integer eventId;
    private String eventName;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String duration;
    private String address;
    private String poster;
    private Boolean isOpen;
    private Boolean isPost;
    private Integer maxAttendee;
    private Category category;
    private JSONObject form;
}
