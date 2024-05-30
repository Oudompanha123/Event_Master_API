package com.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.util.List;


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
    private List<JSONObject> form;
}
