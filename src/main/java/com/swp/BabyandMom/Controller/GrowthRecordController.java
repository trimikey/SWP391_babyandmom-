package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.GrowthRecordRequestDTO;
import com.swp.BabyandMom.DTO.GrowthRecordResponseDTO;
import com.swp.BabyandMom.Service.GrowthRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/growth-records")
@RequiredArgsConstructor
public class GrowthRecordController {
    private final GrowthRecordService growthRecordService;

    @GetMapping
    public ResponseEntity<List<GrowthRecordResponseDTO>> getAllGrowthRecords() {
        return ResponseEntity.ok(growthRecordService.getGrowthRecordsByCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrowthRecordResponseDTO> getGrowthRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(growthRecordService.getGrowthRecordById(id));
    }

    @PostMapping
    public ResponseEntity<GrowthRecordResponseDTO> createGrowthRecord(@RequestBody GrowthRecordRequestDTO request) {
        return ResponseEntity.ok(growthRecordService.createGrowthRecord(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrowthRecordResponseDTO> updateGrowthRecord(
            @PathVariable Long id,
            @RequestBody GrowthRecordRequestDTO request) {
        return ResponseEntity.ok(growthRecordService.updateGrowthRecord(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrowthRecord(@PathVariable Long id) {
        growthRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}
