package com.example.final_project.repository;

import com.example.final_project.model.Event;
import com.example.final_project.model.dto.request.event.EventRequest;
import com.example.final_project.model.dto.request.event.SearchEventRequest;
import com.example.final_project.util.SqlScriptFilterEvent;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface EventRepository {
    @Select("""
        SELECT COUNT(*) FROM event WHERE org_id = #{orgId};
    """)
    Integer getTotalEventRecords(Integer orgId);

    @Select("""
        SELECT * FROM event WHERE org_id = #{orgId} ORDER BY event_name LIMIT #{limit} OFFSET #{offset};
    """)
    @Results(id = "eventMapper", value = {
            @Result(property = "eventId", column = "event_id"),
            @Result(property = "eventName", column = "event_name"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "endDate", column = "end_date"),
            @Result(property = "maxAttendee", column = "max_attendee"),
            @Result(property = "isOpen", column = "is_open"),
            @Result(property = "isPost", column = "is_post"),
            @Result(property = "data", column = "registration_form")
    })
    List<Event> getAllEvents(Integer orgId, Integer offset, Integer limit);

    @Select("""
        SELECT * FROM event WHERE event_id = #{eventId} AND org_id = #{orgId};
    """)
    @ResultMap("eventMapper")
    Event getEventById(Integer orgId, Integer eventId);

    @Select("""
        INSERT INTO event (event_name, start_date, end_date, duration, address, poster, description,
                is_post, max_attendee, registration_form, cate_id, org_id)
            VALUES (#{event.eventName}, #{event.startDate}, #{event.endDate}, #{event.duration},
                #{event.address}, #{event.poster}, #{event.description}, #{event.isPost},
                #{event.maxAttendee}, #{event.dataJsonString} :: jsonb, #{event.categoryId}, #{orgId})     RETURNING *
    """)
    @ResultMap("eventMapper")
    Event createEvent(Integer orgId, @Param("event") EventRequest eventRequest);

    @Delete("""
        DELETE FROM event WHERE event_id = #{eventId} AND org_id = #{orgId};
    """)
    void deleteEventById(Integer orgId, Integer eventId);

    @Select("""
        UPDATE event SET event_name = #{event.eventName}, start_date = #{event.startDate}, end_date = #{event.endDate},
            duration = #{event.duration}, address = #{event.address}, poster = #{event.poster}, description = #{event.description},
            is_post = #{event.isPost}, max_attendee = #{event.maxAttendee}, cate_id = #{event.categoryId}
        WHERE event_id = #{eventId} AND org_id = #{orgId} RETURNING *
    """)
    @ResultMap("eventMapper")
    Event updateEventById(@Param("event") EventRequest eventRequest, Integer eventId, Integer orgId);

    @Update("""
        UPDATE event SET is_open = #{isOpen} WHERE event_id = #{eventId} AND org_id = #{orgId}
    """)
    void updateActiveById(Integer eventId, Integer orgId, boolean isOpen);

    @SelectProvider(type = SqlScriptFilterEvent.class, method = "getSqlScriptCountEventRecord")
    Integer getTotalEventRecordsFromSearch(SearchEventRequest searchEventRequest, Integer orgId);

    @SelectProvider(type = SqlScriptFilterEvent.class, method = "getSqlScriptSearchEvent")
    @ResultMap("eventMapper")
    List<Event> getSearchAllEvent(SearchEventRequest searchEventRequest, Integer orgId, Integer offset, Integer limit);

    @Select("""
        UPDATE event SET registration_form = registration_form ||  #{newJsonFormString} ::jsonb
        WHERE event_id = #{eventId} AND org_id = #{orgId} RETURNING *;
    """)
    @ResultMap("eventMapper")
    Event modifyRegistrationForm(Integer eventId, String newJsonFormString, Integer orgId);

    @Update("""
        UPDATE event SET registration_form = '[]' WHERE event_id = #{eventId} AND org_id = #{orgId};
    """)
    void clearRegistrationFormById(Integer eventId, Integer orgId);
}


