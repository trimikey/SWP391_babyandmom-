package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.GrowthRecordRequestDTO;
import com.swp.BabyandMom.DTO.GrowthRecordResponseDTO;
import com.swp.BabyandMom.Service.GrowthRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/growth-records")
@RequiredArgsConstructor
public class GrowthRecordController {
    private final GrowthRecordService service;

    @GetMapping("/current")
    public ResponseEntity<List<GrowthRecordResponseDTO>> getGrowthRecordsByCurrentUser() {
        return ResponseEntity.ok(service.getGrowthRecordsByCurrentUser());
    }

    @PostMapping
    public ResponseEntity<GrowthRecordResponseDTO> createGrowthRecord(@Valid @RequestBody GrowthRecordRequestDTO request) {
        return ResponseEntity.ok(service.createGrowthRecord(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrowthRecordResponseDTO> updateGrowthRecord(@PathVariable Long id, @Valid @RequestBody GrowthRecordRequestDTO request) {
        return ResponseEntity.ok(service.updateGrowthRecord(id, request));
    }

}
