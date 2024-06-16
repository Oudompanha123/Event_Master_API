package com.example.final_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetRequestUpdate {
    @NotBlank
    @NotNull
    private String assetName;
    @NotNull
    @PositiveOrZero
    private float qty;
}
