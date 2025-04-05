package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.GrowthRecordRequestDTO;
import com.swp.BabyandMom.DTO.GrowthRecordResponseDTO;
import com.swp.BabyandMom.Entity.Enum.MembershipType;
import com.swp.BabyandMom.Service.GrowthRecordService;
import com.swp.BabyandMom.Utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/growth-records")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class GrowthRecordController {
    private final GrowthRecordService growthRecordService;
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
        response.put("isBasic", type == MembershipType.BASIC);
        response.put("isPremium", type == MembershipType.PREMIUM);
        return response;
    }
    @GetMapping("/currents")
    public ResponseEntity<List<GrowthRecordResponseDTO>> getAllGrowthRecords(@RequestBody Map<String, Long> requestBody) {
        checkPremiumUser();
        checkCompletedPayment();
        Long profileId = requestBody.get("profileId");
        return ResponseEntity.ok(growthRecordService.getGrowthRecordsByCurrentUser(profileId));
    }

    //get currents phien bản truyền id qua param
    @GetMapping("/by-weeks")
    public ResponseEntity<List<GrowthRecordResponseDTO>> getGrowthRecordsByWeeks(@RequestParam Long profileId) {
        checkPremiumUser();
        checkCompletedPayment();
        return ResponseEntity.ok(growthRecordService.getGrowthRecordsByCurrentUser(profileId));
    }

    @GetMapping("/current")
    public ResponseEntity<List<GrowthRecordResponseDTO>> getAllGrowthRecords(@RequestParam Long profileId) {
        checkPremiumUser();
        checkCompletedPayment();
        return ResponseEntity.ok(growthRecordService.getGrowthRecordsByCurrentUser(profileId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrowthRecordResponseDTO> getGrowthRecordById(@PathVariable Long id) {
        checkPremiumUser();
        checkCompletedPayment();
        return ResponseEntity.ok(growthRecordService.getGrowthRecordById(id));
    }

    @PostMapping
    public ResponseEntity<GrowthRecordResponseDTO> createGrowthRecord(@RequestBody GrowthRecordRequestDTO request) {
        checkPremiumUser();
        checkCompletedPayment();
        return ResponseEntity.ok(growthRecordService.createGrowthRecord(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GrowthRecordResponseDTO> updateGrowthRecord(
            @PathVariable Long id,
            @RequestBody GrowthRecordRequestDTO request) {
        checkPremiumUser();
        checkCompletedPayment();
        return ResponseEntity.ok(growthRecordService.updateGrowthRecord(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGrowthRecord(@PathVariable Long id) {
        checkPremiumUser();
        checkCompletedPayment();
        growthRecordService.deleteRecord(id);
        return ResponseEntity.ok("Delete successfully");
    }

    @GetMapping("/weight-gain-range")
    public ResponseEntity<Map<Integer, Map<String, Float>>> getWeightGainRange(@RequestParam Long profileId) {
        checkPremiumUser();
        checkCompletedPayment();
        return ResponseEntity.ok(growthRecordService.getWeightGainRange(profileId));
    }
    @GetMapping("/weight-gain-chart")
    public ResponseEntity<Map<String, List<Float>>> getWeightGainChart(@RequestParam Long profileId) {
        checkPremiumUser();
        checkCompletedPayment();
        Map<String, List<Float>> chartData = growthRecordService.getWeightGainChartData(profileId);
        return ResponseEntity.ok(chartData);
    }

}
