package com.example.final_project.model;

import com.example.final_project.model.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialStatusCount {
    private Integer total;
    private Integer pending;
    private Integer onGoing;
    private Integer done;
    private Integer issue;
}