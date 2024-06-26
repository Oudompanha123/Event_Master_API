package com.example.final_project.service.Impl;

import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.Attendee;
import com.example.final_project.model.dto.request.AttendeeRequest;
import com.example.final_project.repository.AttendeeRepository;
import com.example.final_project.repository.EventRepository;
import com.example.final_project.service.AttendeeService;
import com.example.final_project.util.Token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AttendeeServiceImpl implements AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final EventRepository eventRepository;
    @Override
    public Attendee createAttendee(AttendeeRequest attendeeRequest) {
        // check event id exist or not
        if(eventRepository.getEventByIdNoOrgId(attendeeRequest.getEventId()) == null){
            throw new NotFoundException("Event id : " + attendeeRequest.getEventId() + " not found");
        }
        return attendeeRepository.createAttendee(attendeeRequest);
    }

    @Override
    public Integer getTotalAttendeeRecord(Integer eventId) {
        if(attendeeRepository.getTotalAttendeeRecord(eventId) == 0)
            throw new NotFoundException("Attendee is not found in database");
        return attendeeRepository.getTotalAttendeeRecord(eventId);
    }

    @Override
    public List<Attendee> getAttendeesByEventId(Integer eventId, Integer offset, Integer limit) {
        // check event id exist or not
        if(eventRepository.getEventById(Token.getOrgIdByToken(), eventId) == null)
            throw new NotFoundException("Event id : " + eventId + " not found");
        offset = (offset - 1) * limit;
        return attendeeRepository.getAttendeesByEventId(eventId, offset, limit);
    }

    @Override
    public void deleteAttendeeById(Integer attendeeId) {
        if(attendeeRepository.getAttendeeByAttendeeId(attendeeId) == null)
            throw new NotFoundException("Attendee id : " + attendeeId + " not found in database");
        attendeeRepository.deleteAttendeeById(attendeeId);
    }

    @Override
    public Integer getTotalAttendeeRecordsFromSearch(Integer eventId, String attendeeNameOrPhone) {

        // if attendeeNameOrPhone is null then getAllTotalAttendeeRecords
        if(Objects.equals(attendeeNameOrPhone, "") || attendeeNameOrPhone == null)
            return getTotalAttendeeRecord(eventId);

        if(eventRepository.getEventById(Token.getOrgIdByToken(), eventId) == null)
            throw new NotFoundException("Event id : " + eventId + " not found");
        if(attendeeRepository.getTotalAttendeeRecordsFromSearch(eventId, attendeeNameOrPhone) == 0)
            throw new NotFoundException("Attendee is not found in database");
        return attendeeRepository.getTotalAttendeeRecordsFromSearch(eventId, attendeeNameOrPhone.trim());
    }

    @Override
    public List<Attendee> searchAttendeeByNameOrPhone(Integer eventId, String attendeeNameOrPhone, Integer offset, Integer limit) {
        offset = (offset - 1) * limit;
        // if attendeeNameOrPhone is null then getAllTotalAttendeeRecords
        if(Objects.equals(attendeeNameOrPhone, "") || attendeeNameOrPhone == null)
            return getAttendeesByEventId(eventId, offset, limit);

        return attendeeRepository.searchAttendeeByNameOrPhone(eventId, attendeeNameOrPhone.trim(), offset, limit);
    }
}
