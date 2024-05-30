package com.example.final_project.repository;

import com.alibaba.fastjson2.JSONObject;
import com.example.final_project.model.Agenda;
import com.example.final_project.model.AgendaResponse;
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
        SELECT agenda_id FROM agenda WHERE agenda_id = #{agendaId};
    """)
    Integer findAgendaIdByAgendaId(Integer agendaId);

    @Select("""
        DELETE FROM agenda WHERE agenda_id = #{agendaId};
    """)
    void deleteAgendaById(Integer agendaId);

    @Update("""
        UPDATE agenda SET data = '[]' WHERE agenda_id = #{agendaId};
    """)
    void clearDataInAgenda(Integer agendaId);

    @Select("""
        UPDATE agenda SET data = data ||  #{agenda.data, typeHandler = com.example.final_project.config.JsonbTypeHandler} :: JSONB ::jsonb
        WHERE agenda_id = #{agendaId} RETURNING *;
    """)
    @ResultMap("agendaMapper")
    AgendaResponse updateAgendaById(Integer agendaId,@Param("agenda") Agenda agendaData);
}
