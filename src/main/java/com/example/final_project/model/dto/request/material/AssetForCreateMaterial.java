package com.example.final_project.model.dto.request.material;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetForCreateMaterial {
    @NotNull
    @Positive
    private Integer assetId;
    @NotNull
    @NotBlank
    private String assetName;
    @NotNull
    @Positive
    private Float qty;
    @NotNull
    @NotBlank
    private String unit;
}
