package com.example.final_project.service;

import com.example.final_project.model.Agenda;
import com.example.final_project.model.dto.response.AgendaResponse;

public interface AgendaService {
    Agenda createAgenda(Agenda agenda, Integer eventId);

    AgendaResponse getAgendaByEventId(Integer eventId);

    void deleteAgendaById(Integer agendaId);

    AgendaResponse updateAgendaById(Agenda agenda, Integer agendaId);
}
