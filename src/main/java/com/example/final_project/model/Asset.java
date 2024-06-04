package com.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asset {
    private int assetId;
    private String assetName;
    private float qty;
    private String unit;
    private LocalDate createdAt;
}
