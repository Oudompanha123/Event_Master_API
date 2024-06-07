package com.example.final_project.controller;

import com.example.final_project.model.Agenda;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agendas")
@SecurityRequirement(name = "bearerAuth")
public class AgendaController {
    private final AgendaService agendaService;

    @PostMapping("/{eventId}")
    @Operation(summary = "Create agenda by event id")
    public ResponseEntity<?> createAgenda(@RequestBody @Valid Agenda agenda, @PathVariable  @Positive @NotNull Integer eventId) {
        return PostResponse.postResponse("Create agenda successfully",agendaService.createAgenda(agenda, eventId));
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Get agenda by event id")
    public ResponseEntity<?> getAgendaByEventId(@PathVariable @Positive @NotNull Integer eventId){
        return GetResponse.getResponse("Get agenda successfully", agendaService.getAgendaByEventId(eventId));
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "Delete agenda by event id")
    public ResponseEntity<?> deleteAgendaByEventId(@PathVariable @Positive @NotNull Integer eventId){
        agendaService.deleteAgendaByEventId(eventId);
        return GetResponse.getResponse("Delete agenda by event id : " + eventId + "  successfully", null);
    }

    @PutMapping("/{eventId}")
    @Operation(summary = "Update agenda by event id")
    public ResponseEntity<?> updateAgendaById(@PathVariable @Positive @NotNull Integer eventId, @RequestBody @Valid Agenda agenda){
        return UpdateResponse.updateResponse("Update agenda successfully", agendaService.updateAgendaByEventId(agenda, eventId));
    }
}
