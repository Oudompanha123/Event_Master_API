package com.example.final_project.controller;

import com.example.final_project.model.Agenda;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agenda")
@SecurityRequirement(name = "bearerAuth")
public class AgendaController {
    private final AgendaService agendaService;

    @PostMapping("/{eventId}")
    @Operation(summary = "Create agenda through event id")
    public ResponseEntity<?> createAgenda(@RequestBody Agenda agenda, @PathVariable Integer eventId) {
        return PostResponse.postResponse("Create agenda is successfully",agendaService.createAgenda(agenda, eventId));
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Get agenda by event id")
    public ResponseEntity<?> getAgendaByEventId(@PathVariable Integer eventId){
        return GetResponse.getResponse("Get agenda successfully", agendaService.getAgendaByEventId(eventId));
    }

    @DeleteMapping("/{agendaId}")
    @Operation(summary = "Delete agenda by agenda id")
    public ResponseEntity<?> deleteAgendaById(@PathVariable Integer agendaId){
        agendaService.deleteAgendaById(agendaId);
        return GetResponse.getResponse("Delete agenda id : " + agendaId + "  successfully", null);
    }

    @PutMapping("/{agendaId}")
    @Operation(summary = "Update agenda by agenda id")
    public ResponseEntity<?> updateAgendaById(@PathVariable Integer agendaId, @RequestBody Agenda agenda){
        return UpdateResponse.updateResponse("Update agenda successfully", agendaService.updateAgendaById(agenda, agendaId));
    }
}
