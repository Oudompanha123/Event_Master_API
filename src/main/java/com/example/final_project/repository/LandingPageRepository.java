package com.example.final_project.repository;

import com.example.final_project.model.dto.response.landingPage.AllFieldInLandingPage;
import com.example.final_project.model.dto.response.landingPage.EventDetailInLandingPage;
import com.example.final_project.model.dto.response.landingPage.FormResponse;
import com.example.final_project.util.SqlScriptFilterEvent;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LandingPageRepository {

    @Select("""
        SELECT c.cate_name, e.event_id, e.event_name, e.description, e.start_date,
               e.address, e.poster, e.is_open, o.org_name, o.logo FROM (event e INNER JOIN category c ON c.cate_id = e.cate_id)
                                              INNER JOIN organization o ON o.org_id = e.org_id
                WHERE e.is_post = true ORDER BY e.start_date
        ;
    """)
    @Results(id = "landingPageMapper", value = {
            @Result(property = "cateName", column = "cate_name"),
            @Result(property = "eventId", column = "event_id"),
            @Result(property = "eventName", column = "event_name"),
            @Result(property = "startDate", column = "start_date"),
            @Result(property = "isOpen", column = "is_open"),
            @Result(property = "orgName", column = "org_name")
    })
    List<AllFieldInLandingPage> getAllEventsByCategory();

    @Select("""
        SELECT event.event_id as event_id, event_name, description, address, start_date, poster, agenda_id
        FROM event LEFT JOIN agenda ON event.event_id = agenda.event_id WHERE is_post = true AND event.event_id = #{eventId};
    """)
    @Results(id = "eventDetailMapper", value = {
            @Result(property = "eventId", column = "event_id"),
            @Result(property = "eventName", column = "event_name"),
            @Result(property = "startDateTime", column = "start_date"),
            @Result(property = "location", column = "address"),
            @Result(property = "agenda", column = "event_id", one = @One(select = "com.example.final_project.repository.AgendaRepository.getAgendaByEventId"))
    })
    EventDetailInLandingPage getDetailEventByEventId(Integer eventId);

    @Select("""
        SELECT registration_form FROM event WHERE event_id = #{eventId};
    """)
    @Result(property = "data", column = "registration_form")
    FormResponse getFormByEventId(Integer eventId);

    @SelectProvider(type = SqlScriptFilterEvent.class, method = "getSqlScriptSearchEventOnLandingPage")
    @ResultMap("landingPageMapper")
    List<AllFieldInLandingPage> searchEvent(String eventName, Integer categoryId, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
