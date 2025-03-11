package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.ReminderRequestDTO;
import com.swp.BabyandMom.DTO.ReminderResponseDTO;
import com.swp.BabyandMom.Entity.Enum.ReminderType;
import com.swp.BabyandMom.Service.ReminderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminder")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReminderController {
    private final ReminderService service;

    @GetMapping
    public List<ReminderResponseDTO> getAllReminders() {
        return service.getAllReminders();
    }

    @GetMapping("/enum/types")
    public ResponseEntity<ReminderType[]> getReminderTypes() {
        return ResponseEntity.ok(ReminderType.values());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponseDTO> getReminderById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getReminderById(id));
    }

    @PostMapping
    public ReminderResponseDTO createReminder(@RequestBody ReminderRequestDTO request) {
        return service.createReminder(request);
    }

    @PutMapping("/{id}")
    public ReminderResponseDTO updateReminder(@PathVariable Long id, @RequestBody ReminderRequestDTO request) {
        return service.updateReminder(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReminder(@PathVariable Long id) {
        service.deleteReminder(id);
        return ResponseEntity.ok("Delete successfully");
    }

}