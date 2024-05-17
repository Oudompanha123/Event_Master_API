package com.example.final_project.model.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class GetAllResponse {
    public static ResponseEntity<?> getAllResponse(String message, Integer totalRecord, Object data){
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponseAll<>(
                        message,
                        totalRecord,
                        data,
                        HttpStatus.OK,
                        LocalDateTime.now()
                )
        );
    }
}
