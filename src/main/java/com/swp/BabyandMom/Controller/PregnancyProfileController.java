package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.PregnancyProfileRequestDTO;
import com.swp.BabyandMom.DTO.PregnancyProfileResponseDTO;
import com.swp.BabyandMom.Service.PregnancyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@RestController
@RequestMapping("/api/pregnancy-profile")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PregnancyProfileController {
    private final PregnancyProfileService service;

    @GetMapping
    public List<PregnancyProfileResponseDTO> getAllProfiles() {
        return service.getAllProfiles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PregnancyProfileResponseDTO> getProfileById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPregnancyProfileById(id));
    }

    @PostMapping
    public PregnancyProfileResponseDTO createProfile(@RequestBody PregnancyProfileRequestDTO request) {
        return service.createProfile(request);
    }

    @PutMapping("/{id}")
    public PregnancyProfileResponseDTO updateProfile(@PathVariable Long id, @RequestBody PregnancyProfileRequestDTO request) {
        return service.updateProfile(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long id) {
        service.deleteProfile(id);
        return ResponseEntity.ok("Delete successfully");
    }

}