package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.ReminderRequestDTO;
import com.swp.BabyandMom.DTO.ReminderResponseDTO;
import com.swp.BabyandMom.Entity.Enum.MembershipType;
import com.swp.BabyandMom.Service.ReminderService;
import com.swp.BabyandMom.Utils.UserUtils;
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
    private final UserUtils userUtils;

    private void checkPremiumUser() {
        MembershipType membershipType = userUtils.getUserMembershipType();
        if (membershipType != MembershipType.PREMIUM) {
            throw new RuntimeException("Access denied: Only PREMIUM users can access this feature.");
        }
    }

    @GetMapping
    public List<ReminderResponseDTO> getAllReminders() {
        checkPremiumUser();
        return service.getAllReminders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponseDTO> getReminderById(@PathVariable Long id) {
        checkPremiumUser();
        return ResponseEntity.ok(service.getReminderById(id));
    }

    @PostMapping
    public ReminderResponseDTO createReminder(@RequestBody ReminderRequestDTO request) {
        checkPremiumUser();
        return service.createReminder(request);
    }

    @PutMapping("/{id}")
    public ReminderResponseDTO updateReminder(@PathVariable Long id, @RequestBody ReminderRequestDTO request) {
        checkPremiumUser();
        return service.updateReminder(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReminder(@PathVariable Long id) {
        checkPremiumUser();
        service.deleteReminder(id);
        return ResponseEntity.ok("Delete successfully");
    }
}
