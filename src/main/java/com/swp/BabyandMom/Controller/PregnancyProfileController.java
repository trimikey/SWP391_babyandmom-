package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.PregnancyProfileRequestDTO;
import com.swp.BabyandMom.DTO.PregnancyProfileResponseDTO;
import com.swp.BabyandMom.Entity.Enum.MembershipType;
import com.swp.BabyandMom.Service.PregnancyProfileService;
import com.swp.BabyandMom.Utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pregnancy-profile")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PregnancyProfileController {
    private final PregnancyProfileService service;
    private final UserUtils userUtils;

    private void checkBasicOrPremiumUser() {
        MembershipType membershipType = userUtils.getUserMembershipType();
        if (membershipType != MembershipType.BASIC && membershipType != MembershipType.PREMIUM) {
            throw new RuntimeException("Access denied: Only BASIC or PREMIUM users can access this feature.");
        }
    }
    @GetMapping("/membership/status")
    public Map<String, Boolean> getMembershipStatus() {
        Map<String, Boolean> response = new HashMap<>();
        MembershipType type = userUtils.getUserMembershipType();
        response.put("isBasic", type == MembershipType.BASIC);
        response.put("isPremium", type == MembershipType.PREMIUM);
        return response;
    }

    @GetMapping
    public List<PregnancyProfileResponseDTO> getAllProfiles() {
        checkBasicOrPremiumUser();
        return service.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PregnancyProfileResponseDTO> getProfileById(@PathVariable Long id) {
        checkBasicOrPremiumUser();
        return ResponseEntity.ok(service.getPregnancyProfileById(id));
    }

    @PostMapping
    public PregnancyProfileResponseDTO createProfile(@RequestBody PregnancyProfileRequestDTO request) {
        checkBasicOrPremiumUser();
        return service.createProfile(request);
    }

    @PutMapping("/{id}")
    public PregnancyProfileResponseDTO updateProfile(@PathVariable Long id, @RequestBody PregnancyProfileRequestDTO request) {
        checkBasicOrPremiumUser();
        return service.updateProfile(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long id) {
        checkBasicOrPremiumUser();
        service.deleteProfile(id);
        return ResponseEntity.ok("Delete successfully");
    }
}
