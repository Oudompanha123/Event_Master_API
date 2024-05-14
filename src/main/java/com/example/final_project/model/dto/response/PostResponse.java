package com.example.final_project.model.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;

public class PostResponse {
    public static ResponseEntity<?> postResponse(String message, Object data){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new APIResponse<>(
                        message,
                        data,
                        HttpStatus.CREATED,
                        LocalDateTime.now()
                )
        );
    }
}
