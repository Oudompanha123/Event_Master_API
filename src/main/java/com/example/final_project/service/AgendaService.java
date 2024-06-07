package com.example.final_project.service;

import com.example.final_project.model.Agenda;
import com.example.final_project.model.dto.response.AgendaResponse;

public interface AgendaService {
    Agenda createAgenda(Agenda agenda, Integer eventId);

    AgendaResponse getAgendaByEventId(Integer eventId);

    void deleteAgendaByEventId(Integer eventId);

    AgendaResponse updateAgendaByEventId(Agenda agenda, Integer eventId);
}
