package com.example.final_project.service.Impl;

import com.example.final_project.model.CategoryEventCount;
import com.example.final_project.model.Dashboard;
import com.example.final_project.repository.DashboardRepository;
import com.example.final_project.service.DashboardService;
import com.example.final_project.util.Token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository dashboardRepository;
    @Override
    public Dashboard getDashboardData() {
        Integer orgId = Token.getOrgIdByToken();
        // get event count
        Integer eventCount = dashboardRepository.getEventCount(orgId);
        // get publish event count
        Integer publishEventCount = dashboardRepository.getPublishEventCount(orgId);
        // get attendee in organization
        Integer attendeeCount = dashboardRepository.getAttendeeCount(orgId);
        // get member count
        Integer memberCount = dashboardRepository.getMemberCount(orgId);
        // get category event count
        List<CategoryEventCount> categoryEventCountList = dashboardRepository.getCategoryEventCountList(orgId);
        // set all data into dashboard model
        return new Dashboard(eventCount, publishEventCount, attendeeCount, memberCount, categoryEventCountList);
    }
}
