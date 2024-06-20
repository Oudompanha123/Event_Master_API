package com.example.final_project.repository;

import com.example.final_project.model.Event;
import com.example.final_project.model.dto.request.event.EventRequest;
import com.example.final_project.model.dto.request.event.FormRequest;
import com.example.final_project.util.SqlScriptFilterEvent;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EventRepository {
    @Select("""
        SELECT COUNT(*) FROM event WHERE org_id = #{orgId};
    """)
    Integer getTotalEventRecords(Integer orgId);

    @Select("""
        SELECT * FROM event WHERE org_id = #{orgId} ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset};
    """)
    @Results(id = "eventMapper", value = {
        @Result(property = "eventId", column = "event_id"),
        @Result(property = "eventName", column = "event_name"),
        @Result(property = "startDate", column = "start_date"),
        @Result(property = "endDate", column = "end_date"),
        @Result(property = "maxAttendee", column = "max_attendee"),
        @Result(property = "isOpen", column = "is_open"),
        @Result(property = "isPost", column = "is_post"),
        @Result(property = "form", column = "registration_form"),
        @Result(property = "category", column = "{cateId=cate_id, orgId=org_id}", one = @One(select = "com.example.final_project.repository.CategoryRepository.getCategoryById"))
    })
    List<Event> getAllEvents(Integer orgId, Integer offset, Integer limit);
    @Select("""
        SELECT * FROM event WHERE event_id = #{eventId} AND org_id = #{orgId};
    """)
    @ResultMap("eventMapper")
    Event getEventById(Integer orgId, Integer eventId);

    @Select("""
        SELECT * FROM event WHERE event_id = #{eventId};
    """)
    @ResultMap("eventMapper")
    Event getEventByIdNoOrgId(Integer eventId);
    @Select("""
        INSERT INTO event (event_name, start_date, end_date, duration, address, poster, description,
                is_post, max_attendee, registration_form, cate_id, org_id, created_at)
            VALUES (#{event.eventName}, #{event.startDate}, #{event.endDate}, #{event.duration},
                #{event.address}, #{event.poster}, #{event.description}, #{event.isPost},
                #{event.maxAttendee}, #{event.dataJsonString} :: jsonb, #{event.categoryId}, #{orgId}, current_timestamp) RETURNING *
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
            is_post = #{event.isPost}, max_attendee = #{event.maxAttendee}, cate_id = #{event.categoryId}, created_at = current_timestamp
        WHERE event_id = #{eventId} AND org_id = #{orgId} RETURNING *
    """)
    @ResultMap("eventMapper")
    Event updateEventById(@Param("event") EventRequest eventRequest, Integer eventId, Integer orgId);

    @Update("""
        UPDATE event SET is_open = #{isOpen}, created_at = current_timestamp WHERE event_id = #{eventId} AND org_id = #{orgId}
    """)
    void updateActiveById(Integer eventId, Integer orgId, boolean isOpen);

    @SelectProvider(type = SqlScriptFilterEvent.class, method = "getSqlScriptCountEventRecord")
    Integer getTotalEventRecordsFromSearch(String eventName, Integer categoryId, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer orgId);

    @SelectProvider(type = SqlScriptFilterEvent.class, method = "getSqlScriptSearchEvent")
    @ResultMap("eventMapper")
    List<Event> getSearchAllEvent(String eventName, Integer categoryId, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer orgId, Integer offset, Integer limit);

    @Select("""
        UPDATE event SET registration_form = #{form.data, typeHandler = com.example.final_project.config.JsonbTypeHandler} :: JSONB
        WHERE event_id = #{eventId} AND org_id = #{orgId} RETURNING *;
    """)
    @ResultMap("eventMapper")
    Event modifyRegistrationForm(Integer eventId, @Param("form") FormRequest formRequest, Integer orgId);

    @Update("""
        UPDATE event SET registration_form = null WHERE event_id = #{eventId} AND org_id = #{orgId};
    """)
    void clearRegistrationFormById(Integer eventId, Integer orgId);

    @Update("""
        UPDATE event
            SET is_open = FALSE
            WHERE is_open = TRUE AND end_date <= CURRENT_TIMESTAMP;
    """)
    void autoCloseEvent();
}


