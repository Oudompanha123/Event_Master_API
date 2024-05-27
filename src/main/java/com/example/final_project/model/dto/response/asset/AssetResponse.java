package com.example.final_project.model.dto.response.asset;

import com.example.final_project.model.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetResponse {
    private int assetId;
    private String assetName;
    private float qty;
    private String unit;
    private LocalDate addDate;
    private Organization organization;
}
