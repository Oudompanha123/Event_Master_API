package com.example.final_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetRequest {
    @NotBlank
    @NotNull
    @Size(min = 1, max = 40)
    private String assetName;
    @NotNull
    @PositiveOrZero
    private float qty;
    @NotBlank
    @NotNull
    @Size(min = 1, max = 20)
    private String unit;
}