package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.GrowthRecordRequestDTO;
import com.swp.BabyandMom.DTO.GrowthRecordResponseDTO;
import com.swp.BabyandMom.Entity.Enum.AlertStatus;
import com.swp.BabyandMom.Entity.Growth_Record;
import com.swp.BabyandMom.Entity.Pregnancy_Profile;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Repository.GrowthRecordRepository;
import com.swp.BabyandMom.Repository.PregnancyRepository;
import com.swp.BabyandMom.Utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrowthRecordService {
    private final GrowthRecordRepository repository;
    private final PregnancyRepository pregnancyRepository;
    private final UserUtils userUtils;

    private Pregnancy_Profile getPregnancyProfileOfCurrentUser(Long profileId) {
        User currentUser = userUtils.getCurrentAccount();
        return pregnancyRepository.findById(profileId)
                .filter(profile -> profile.getUser().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new RuntimeException("Pregnancy profile not found or not owned by user"));
    }

    private Pregnancy_Profile getPregnancyProfileById(Long profileId) {
        return pregnancyRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Pregnancy profile not found"));
    }

    public List<GrowthRecordResponseDTO> getGrowthRecordsByCurrentUser(Long profileId) {
        Pregnancy_Profile pregnancy = getPregnancyProfileOfCurrentUser(profileId);
        return repository.findByPregnancyAndIsDeletedFalse(pregnancy).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GrowthRecordResponseDTO getGrowthRecordById(Long id) {
        Growth_Record record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Growth record not found"));
        validateOwnership(record.getPregnancy().getId());
        return convertToDTO(record);
    }

    public GrowthRecordResponseDTO createGrowthRecord(GrowthRecordRequestDTO request) {
        Pregnancy_Profile pregnancy = getPregnancyProfileById(request.getProfileId());

        Growth_Record record = new Growth_Record();
        record.setPregnancy(pregnancy);
        record.setPregnancyWeek(request.getPregnancyWeek());
        record.setPregnancyWeight(request.getPregnancyWeight());
        record.setPregnancyHeight(request.getPregnancyHeight());
        record.setNotes(request.getNotes());
        record.setPrePregnancyWeight(request.getPrePregnancyWeight());
        record.setPrePregnancyHeight(request.getPrePregnancyHeight());
        record.setCreatedAt(LocalDateTime.now());

        float preBMI = record.getPrePregnancyBMI();
        float currentBMI = record.getCurrentBMI();
        record.setAlertStatus(determineAlertStatus(
                preBMI, currentBMI, request.getPrePregnancyWeight(), request.getPregnancyWeight(),
                request.getPregnancyWeek(), request.getPrePregnancyHeight()
        ));

        repository.save(record);
        return convertToDTO(record);
    }

    public GrowthRecordResponseDTO updateGrowthRecord(Long id, GrowthRecordRequestDTO request) {
        Growth_Record record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Growth record not found"));
        validateOwnership(record.getPregnancy().getId());

        record.setPregnancyWeek(request.getPregnancyWeek());
        record.setPregnancyWeight(request.getPregnancyWeight());
        record.setPregnancyHeight(request.getPregnancyHeight());
        record.setNotes(request.getNotes());
        record.setPrePregnancyWeight(request.getPrePregnancyWeight());
        record.setPrePregnancyHeight(request.getPrePregnancyHeight());

        float preBMI = record.getPrePregnancyBMI();
        float currentBMI = record.getCurrentBMI();
        record.setAlertStatus(determineAlertStatus(
                preBMI, currentBMI, request.getPrePregnancyWeight(), request.getPregnancyWeight(),
                request.getPregnancyWeek(), request.getPrePregnancyHeight()
        ));

        repository.save(record);
        return convertToDTO(record);
    }

    public void deleteRecord(Long id) {
        Growth_Record record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Growth record not found"));
        validateOwnership(record.getPregnancy().getId());
        record.setIsDeleted(true);
        repository.save(record);
    }

    private void validateOwnership(Long profileId) {
        User currentUser = userUtils.getCurrentAccount();
        Pregnancy_Profile pregnancy = pregnancyRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Pregnancy profile not found"));
        if (!pregnancy.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this pregnancy profile");
        }
    }

    private AlertStatus determineAlertStatus(float preBMI, float currentBMI, float preWeight, float currentWeight,
                                             int pregnancyWeek, float prePregnancyHeight) {
        String warning = checkWeightGainWarning(preWeight, currentWeight, pregnancyWeek, preBMI);

        if (currentBMI < preBMI - 2 || warning.contains("too little")) {
            return AlertStatus.LOW;
        } else if (currentBMI > preBMI + 3 || warning.contains("too much")) {
            return AlertStatus.HIGH;
        }
        return AlertStatus.NORMAL;
    }

    private String checkWeightGainWarning(float preWeight, float currentWeight, int pregnancyWeek, float preBMI) {
        float weightGain = currentWeight - preWeight;
        float expectedMin, expectedMax;

        if (pregnancyWeek <= 13) {
            expectedMin = 0.5f;
            expectedMax = 2.0f;
        } else {
            float weeklyMin = (preBMI < 18.5) ? 0.5f : (preBMI >= 25) ? 0.2f : 0.4f;
            float weeklyMax = (preBMI < 18.5) ? 0.6f : (preBMI >= 25) ? 0.3f : 0.5f;
            expectedMin = 0.5f + weeklyMin * (pregnancyWeek - 13);
            expectedMax = 2.0f + weeklyMax * (pregnancyWeek - 13);
        }

        if (weightGain < expectedMin) return "Gaining too little weight can affect the fetus";
        if (weightGain > expectedMax) return "Gain too much weight, need to control diet";
        return "Normal weight gain";
    }

    public Map<Integer, Map<String, Float>> getWeightGainRange(Long profileId) {
        Pregnancy_Profile pregnancy = getPregnancyProfileById(profileId);

        List<Growth_Record> records = repository.findByPregnancyAndIsDeletedFalse(pregnancy);
        if (records.isEmpty()) {
            throw new IllegalStateException("No growth records found for this pregnancy profile");
        }

        Growth_Record latestRecord = records.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No valid growth records found"));

        float preWeight = latestRecord.getPrePregnancyWeight();
        float preHeight = latestRecord.getPrePregnancyHeight();
        float preBMI = preWeight / ((preHeight / 100) * (preHeight / 100));

        if (preWeight == 0 || preHeight == 0) {
            throw new IllegalArgumentException("Pre-pregnancy weight or height is missing or invalid in the latest record");
        }

        Map<Integer, Map<String, Float>> ranges = new HashMap<>();
        for (int week = 1; week <= 40; week++) {
            Map<String, Float> range = new HashMap<>();
            float expectedMin, expectedMax;

            if (week <= 13) {
                expectedMin = 0.5f;
                expectedMax = 2.0f;
            } else {
                float weeklyMin = (preBMI < 18.5) ? 0.5f : (preBMI >= 25) ? 0.2f : 0.4f;
                float weeklyMax = (preBMI < 18.5) ? 0.6f : (preBMI >= 25) ? 0.3f : 0.5f;
                expectedMin = 0.5f + weeklyMin * (week - 13);
                expectedMax = 2.0f + weeklyMax * (week - 13);
            }

            range.put("min", expectedMin);
            range.put("max", expectedMax);
            range.put("avg", (expectedMin + expectedMax) / 2);
            ranges.put(week, range);
        }
        return ranges;
    }

    private GrowthRecordResponseDTO convertToDTO(Growth_Record record) {
        return new GrowthRecordResponseDTO(
                record.getId(),
                record.getPregnancyWeek(),
                record.getPregnancyWeight(),
                record.getPregnancyHeight(),
                record.getNotes(),
                record.getPrePregnancyWeight(),
                record.getPrePregnancyHeight(),
                record.getPrePregnancyBMI(),
                record.getCurrentBMI(),
                checkWeightGainWarning(
                        record.getPrePregnancyWeight(),
                        record.getPregnancyWeight(),
                        record.getPregnancyWeek(),
                        record.getPrePregnancyBMI()
                ),
                record.getAlertStatus(),
                record.getCreatedAt()
        );
    }
    public Map<String, List<Float>> getWeightGainChartData(Long profileId) {
        List<GrowthRecordResponseDTO> growthRecords = getGrowthRecordsByCurrentUser(profileId);
        Map<Integer, Map<String, Float>> weightGainRange = getWeightGainRange(profileId);

        List<Float> actualWeights = new ArrayList<>();
        List<Float> standardWeights = new ArrayList<>();

        float prePregnancyWeight = growthRecords.stream()
                .findFirst()
                .map(GrowthRecordResponseDTO::getPrePregnancyWeight)
                .orElse(0f);

        for (int week = 1; week <= 40; week++) {
            final int currentWeek = week;

            float actualWeight = growthRecords.stream()
                    .filter(record -> record.getPregnancyWeek() == currentWeek)
                    .map(GrowthRecordResponseDTO::getPregnancyWeight)
                    .findFirst()
                    .orElse(0f);

            Map<String, Float> range = weightGainRange.get(currentWeek);
            float minWeight = range != null ? range.get("min") : 0f;
            float maxWeight = range != null ? range.get("max") : 0f;
            float avgWeight = (minWeight + maxWeight) / 2;

            float standardWeight = prePregnancyWeight + avgWeight;

            actualWeights.add(actualWeight);
            standardWeights.add(standardWeight);
        }


        return Map.of(
                "actualWeights", actualWeights,
                "standardWeights", standardWeights
        );
    }

}