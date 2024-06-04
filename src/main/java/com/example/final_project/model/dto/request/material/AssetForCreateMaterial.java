package com.example.final_project.model.dto.request.material;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetForCreateMaterial {
    private Integer assetId;
    private String assetName;
    private Float qty;
    private String unit;
}
