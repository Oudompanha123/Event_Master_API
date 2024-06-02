package com.example.final_project.repository;

import com.example.final_project.model.Attendee;
import com.example.final_project.model.dto.request.AttendeeRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AttendeeRepository {
    @Select("""
        INSERT INTO attendee (data, event_id)
        VALUES (#{attendee.data, typeHandler = com.example.final_project.config.JsonbTypeHandler} :: JSONB, #{attendee.eventId})
        RETURNING *
    """)
    @Results(id = "attendeeMapper", value = {
            @Result(property = "attendeeId", column = "attendee_id")
    })
    Attendee createAttendee(@Param("attendee") AttendeeRequest attendeeRequest);

    @Select("""
        SELECT COUNT(*) FROM attendee WHERE event_id = #{eventId};
    """)
    Integer getTotalAttendeeRecord(Integer eventId);

    @Select("""
        SELECT attendee_id, data FROM attendee WHERE event_id = #{eventId} LIMIT #{limit} OFFSET #{offset};
    """)
    @ResultMap("attendeeMapper")
    List<Attendee> getAttendeesByEventId(Integer eventId, Integer offset, Integer limit);

    @Select("""
        DELETE FROM attendee WHERE attendee_id = #{attendeeId};
    """)
    void deleteAttendeeById(Integer attendeeId);

    @Select("""
        SELECT * FROM attendee WHERE attendee_id = #{attendeeId};
    """)
    @ResultMap("attendeeMapper")
    Attendee getAttendeeByAttendeeId(Integer attendeeId);

    @Select("""
        SELECT COUNT(*)
        FROM attendee
        WHERE (data->>'name' ILIKE CONCAT('%', #{attendeeNameOrPhone}, '%')
           OR data->>'phone' ILIKE CONCAT('%', #{attendeeNameOrPhone}, '%')
           ) AND event_id = #{eventId};

    """)
    Integer getTotalAttendeeRecordsFromSearch(Integer eventId, String attendeeNameOrPhone);

    @Select("""
        SELECT attendee_id, data
        FROM attendee
        WHERE (data->>'name' ILIKE CONCAT('%', #{attendeeNameOrPhone}, '%')
           OR data->>'phone' ILIKE CONCAT('%', #{attendeeNameOrPhone}, '%')
           ) AND event_id = #{eventId} LIMIT #{limit} OFFSET #{offset};
    """)
    @ResultMap("attendeeMapper")
    List<Attendee> searchAttendeeByNameOrPhone(Integer eventId, String attendeeNameOrPhone, Integer offset, Integer limit);
}
