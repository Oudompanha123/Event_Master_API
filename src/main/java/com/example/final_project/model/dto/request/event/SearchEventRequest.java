package com.example.final_project.model.dto.request.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchEventRequest {
    private String eventName;
    private Integer categoryId;
    private Boolean status;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
