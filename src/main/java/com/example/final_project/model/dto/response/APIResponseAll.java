package com.example.final_project.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class APIResponseAll<T> {
    private String message;
    private Integer totalRecord;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T payload;
    private HttpStatus status;
    private LocalDateTime localDateTime;
}
