package com.example.final_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetRequest {
    @NotBlank
    @NotNull
    private String assetName;
    @Positive
    private float qty;
    @NotBlank
    @NotNull
    private String unit;
}