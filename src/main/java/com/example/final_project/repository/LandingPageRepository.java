package com.example.final_project.repository;

import com.example.final_project.model.dto.response.landingPage.*;
import com.example.final_project.util.SqlScriptFilterEvent;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LandingPageRepository {

    @Select("""
        SELECT event.event_id as event_id, event_name, description, address, start_date, poster, is_open, agenda_id
        FROM event LEFT JOIN agenda ON event.event_id = agenda.event_id WHERE is_post = true AND event.event_id = #{eventId};
    """)
    @Results(id = "eventDetailMapper", value = {
            @Result(property = "eventId", column = "event_id"),
            @Result(property = "eventName", column = "event_name"),
            @Result(property = "startDateTime", column = "start_date"),
            @Result(property = "location", column = "address"),
            @Result(property = "isOpen", column = "is_open"),
            @Result(property = "agenda", column = "event_id", one = @One(select = "com.example.final_project.repository.AgendaRepository.getAgendaByEventId"))
    })
    EventDetailInLandingPage getDetailEventByEventId(Integer eventId);

    @Select("""
        SELECT registration_form FROM event WHERE event_id = #{eventId};
    """)
    @Result(property = "data", column = "registration_form")
    FormResponse getFormByEventId(Integer eventId);

    @SelectProvider(type = SqlScriptFilterEvent.class, method = "getSqlScriptSearchEventOnLandingPage")
    @Results(id = "landingPageMapper", value = {
            @Result(property = "cateName", column = "cate_name"),
            @Result(property = "eventId", column = "event_id"),
            @Result(property = "eventName", column = "event_name"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "isOpen", column = "is_open"),
            @Result(property = "orgName", column = "org_name")
    })
    List<AllFieldInLandingPage> searchEvent(String eventName, String categoryName, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Select("""
        SELECT
            e.event_id,
            e.event_name,
            e.poster,
            e.description,
            COUNT(a.attendee_id) AS attendee
        FROM event e INNER JOIN attendee a ON e.event_id = a.event_id
        WHERE e.is_post = true AND e.is_open = true
        GROUP BY e.event_id
        HAVING COUNT(a.attendee_id) >= #{attendees} AND COUNT(a.attendee_id) != 0
        ORDER BY attendee DESC LIMIT 8
    """)
    @Results(id = "popularEventMapper", value = {
            @Result(property = "eventId", column = "event_id"),
            @Result(property = "eventName", column = "event_name")
    })
    List<PopularEventResponse> getAllPopularEvent(Double attendees);

    @Select("""
        SELECT COUNT(a.attendee_id) AS attendee
        FROM event e INNER JOIN attendee a ON e.event_id = a.event_id
        WHERE e.is_post = true AND e.is_open = true
        GROUP BY e.event_id
        ORDER BY attendee DESC LIMIT 1
    """)
    Integer getMaximumAttendeeInEvents();

    @Select("""
        SELECT distinct on (c.cate_name) c.cate_name FROM category c
            INNER JOIN event e ON c.cate_id = e.cate_id
                                    WHERE c.cate_id = e.cate_id AND e.is_post = true;
    """)
    List<String> getAllCategoryNames();

    @Select("""
        SELECT e.event_id, e.event_name, e.description,
               e.start_date, e.address, e.poster, e.is_open, o.org_name, o.logo
        FROM (event e INNER JOIN category c ON c.cate_id = e.cate_id)
                 INNER JOIN organization o ON o.org_id = e.org_id
        WHERE e.is_post = true AND c.cate_name = #{cateName} ORDER BY e.start_date LIMIT #{limit} OFFSET #{offset}
    """)
    @Results(id = "eventMapper", value = {
            @Result(property = "eventId", column = "event_id"),
            @Result(property = "eventName", column = "event_name"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "isOpen", column = "is_open"),
            @Result(property = "orgName", column = "org_name")
    })
    List<EventResponseLandingPage> getEventByCategoryName(String cateName, Integer offset, Integer limit);

    @Select("""
        SELECT event_id FROM event WHERE event_id = #{eventId} AND is_post = true;
    """)
    Integer getEventById(Integer eventId);

    @Select("""
        SELECT COUNT(*) FROM attendee WHERE event_id = #{eventId};
    """)
    Integer getTotalAttendees(Integer eventId);
}
