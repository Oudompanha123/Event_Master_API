package com.example.final_project.controller;

import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dashboards")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Get Dashboard Data")
    public ResponseEntity<?> getDashboardData(){
        return GetResponse.getResponse("Get Data Successfully",
                dashboardService.getDashboardData());
    }
}
