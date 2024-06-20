package com.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dashboard {
    private Integer eventCount;
    private Integer publishEvent;
    private Integer totalAttendee;
    private Integer member;
    private List<CategoryEventCount> categoryEventCountList;
}
