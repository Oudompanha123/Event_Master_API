package com.example.final_project.service;

import com.example.final_project.model.Attendee;
import com.example.final_project.model.dto.request.AttendeeRequest;

import java.util.List;

public interface AttendeeService {
    Attendee createAttendee(AttendeeRequest attendeeRequest);

    Integer getTotalAttendeeRecord(Integer eventId);

    List<Attendee> getAttendeesByEventId(Integer eventId, Integer offset, Integer limit);

    void deleteAttendeeById(Integer attendeeId);

    Integer getTotalAttendeeRecordsFromSearch(Integer eventId, String attendeeNameOrPhone);

    List<Attendee> searchAttendeeByNameOrPhone(Integer eventId, String attendeeNameOrPhone, Integer offset, Integer limit);
}
