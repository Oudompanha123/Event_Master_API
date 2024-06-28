package com.example.final_project.model.dto.request.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 60)
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
    @Size(min = 1, max = 30)
    private String duration;
    @NotNull
    @NotBlank
    private String address;
    @NotNull
    @NotBlank
    private String poster;
    @NotNull
    private Boolean isPost;
    @NotNull
    @PositiveOrZero
    private Integer maxAttendee;
    @NotNull
    @Positive
    private Integer categoryId;
    @JsonIgnore
    private String dataJsonString;
}

