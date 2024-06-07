package com.example.final_project.service.Impl;

import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.Agenda;
import com.example.final_project.model.dto.response.AgendaResponse;
import com.example.final_project.model.Event;
import com.example.final_project.repository.AgendaRepository;
import com.example.final_project.repository.EventRepository;
import com.example.final_project.service.AgendaService;
import com.example.final_project.util.Token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AgendaServiceImpl implements AgendaService {
    private final AgendaRepository agendaRepository;
    private final EventRepository eventRepository;
    @Override
    public Agenda createAgenda(Agenda agenda, Integer eventId) {
        // Check event id in database or not
        Event event = eventRepository.getEventById(Token.getOrgIdByToken(), eventId);
        if(event == null)
            throw new NotFoundException("Event id : " + eventId + " not found in database");

        // check 1 event has only one agenda
        Integer agendaId = agendaRepository.findAgendaIdByEventId(eventId);
        if(agendaId != null)
            throw new BadRequestException("An event has only one agenda");

        return agendaRepository.createAgenda(agenda, eventId);
    }

    @Override
    public AgendaResponse getAgendaByEventId(Integer eventId) {
        Integer agendaId = agendaRepository.findAgendaIdByEventId(eventId);
        if(agendaId == null)
            throw new NotFoundException("Event id : " + eventId + " not found in database");
        return agendaRepository.getAgendaByEventId(eventId);
    }

    @Override
    public void deleteAgendaByEventId(Integer eventId) {
        // check event id exists or not
        if(agendaRepository.findEventIdByEventIdInAgendaTable(eventId) == null)
            throw new NotFoundException("Event id : " + eventId + " not found in database");

        agendaRepository.deleteAgendaByEventId(eventId);
    }

    @Override
    public AgendaResponse updateAgendaByEventId(Agenda agenda, Integer eventId) {
        // check event id exists or not
        if(agendaRepository.findEventIdByEventIdInAgendaTable(eventId) == null)
            throw new NotFoundException("event id : " + eventId + " not found in database");

        // clear data in agenda
        agendaRepository.clearDataInAgenda(eventId);

        return agendaRepository.updateAgendaByEventId(eventId, agenda);
    }
}
