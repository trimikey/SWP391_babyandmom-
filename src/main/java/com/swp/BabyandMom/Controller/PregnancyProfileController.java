package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.PregnancyProfileRequestDTO;
import com.swp.BabyandMom.DTO.PregnancyProfileResponseDTO;
import com.swp.BabyandMom.Service.PregnancyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pregnancy-profile")
@RequiredArgsConstructor
public class PregnancyProfileController {
    private final PregnancyProfileService service;

    @GetMapping
    public List<PregnancyProfileResponseDTO> getAllProfiles() {
        return service.getAllProfiles();
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
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        service.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}