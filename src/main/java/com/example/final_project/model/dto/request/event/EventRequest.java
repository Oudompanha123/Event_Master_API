package com.example.final_project.model.dto.request.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    @NotNull
    @NotBlank
    private String eventName;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime startDate;
    @NotNull
    private LocalDateTime endDate;
    @NotNull
    @NotBlank
    private String duration;
    @NotNull
    @NotBlank
    private String address;
    @NotNull
    @NotBlank
    private String poster;
    @NotNull
    private Boolean isPost;
    @Positive
    private Integer maxAttendee;
    @NotNull
    private Integer categoryId;
    @JsonIgnore
    private String dataJsonString;
}

