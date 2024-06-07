package com.example.final_project.repository;

import com.example.final_project.model.Agenda;
import com.example.final_project.model.dto.response.AgendaResponse;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AgendaRepository {
    @Select("""
        INSERT INTO agenda (data, event_id) VALUES(#{agenda.data, typeHandler = com.example.final_project.config.JsonbTypeHandler} :: JSONB, #{eventId})
        RETURNING *;
    """)
    Agenda createAgenda(@Param("agenda") Agenda agendaData, Integer eventId);

    @Select("""
        SELECT agenda_id FROM agenda WHERE event_id = #{eventId};
    """)
    Integer findAgendaIdByEventId(Integer eventId);

    @Select("""
        SELECT * FROM agenda WHERE event_id = #{eventId};
    """)
    @Results(id = "agendaMapper", value = {
            @Result(property = "agendaId", column = "agenda_id"),
            @Result(property = "eventId", column = "event_id")
    })
    AgendaResponse getAgendaByEventId(Integer eventId);

    @Select("""
        SELECT event_id FROM agenda WHERE event_id = #{eventId};
    """)
    Integer findEventIdByEventIdInAgendaTable(Integer eventId);

    @Select("""
        DELETE FROM agenda WHERE event_id = #{eventId};
    """)
    void deleteAgendaByEventId(Integer eventId);

    @Update("""
        UPDATE agenda SET data = null WHERE event_id = #{eventId};
    """)
    void clearDataInAgenda(Integer eventId);

    @Select("""
        UPDATE agenda SET data = #{agenda.data, typeHandler = com.example.final_project.config.JsonbTypeHandler} :: JSONB
        WHERE event_id = #{eventId} RETURNING *;
    """)
    @ResultMap("agendaMapper")
    AgendaResponse updateAgendaByEventId(Integer eventId, @Param("agenda") Agenda agendaData);

}
