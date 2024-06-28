package com.example.final_project.model.dto.request.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchEventRequest {
    @NotNull
    @NotBlank
    private String eventName;
    @NotNull
    @Positive
    private Integer categoryId;
    @NotNull
    private Boolean status;
    @NotNull
    private LocalDateTime startDateTime;
    @NotNull
    private LocalDateTime endDateTime;
}
