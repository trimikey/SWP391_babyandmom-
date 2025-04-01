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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private void checkCompletedPayment() {
        boolean hasCompletedPayment = userUtils.hasCompletedPayment();
        if (!hasCompletedPayment) {
            throw new RuntimeException("Access denied: Payment must be completed to access this feature.");
        }
    }

    @GetMapping("/membership/status")
    public Map<String, Boolean> getMembershipStatus() {
        Map<String, Boolean> response = new HashMap<>();
        MembershipType type = userUtils.getUserMembershipType();
        response.put("isPremium", type == MembershipType.PREMIUM);
        return response;
    }
    @GetMapping
    public List<ReminderResponseDTO> getAllReminders() {
        checkPremiumUser();
        checkCompletedPayment();
        return service.getAllReminders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponseDTO> getReminderById(@PathVariable Long id) {
        checkPremiumUser();
        checkCompletedPayment();
        return ResponseEntity.ok(service.getReminderById(id));
    }

    @PostMapping
    public ReminderResponseDTO createReminder(@RequestBody ReminderRequestDTO request) {
        checkPremiumUser();
        checkCompletedPayment();
        return service.createReminder(request);
    }

    @PutMapping("/{id}")
    public ReminderResponseDTO updateReminder(@PathVariable Long id, @RequestBody ReminderRequestDTO request) {
        checkPremiumUser();
        checkCompletedPayment();
        return service.updateReminder(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReminder(@PathVariable Long id) {
        checkPremiumUser();
        checkCompletedPayment();
        service.deleteReminder(id);
        return ResponseEntity.ok("Delete successfully");
    }
}
