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
import java.util.List;
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
                record.getPrePregnancyBMI(),
                record.getCurrentBMI(),
                request.getPrePregnancyWeight(),
                request.getPregnancyWeight(),
                request.getPregnancyWeek(),
                request.getPrePregnancyHeight()
        ));

        repository.save(record);
        return convertToDTO(record);
    }


    public GrowthRecordResponseDTO updateGrowthRecord(Long id, GrowthRecordRequestDTO request) {
        Growth_Record record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Growth record not found"));
        record.setPregnancyWeek(request.getPregnancyWeek());
        record.setPregnancyWeight(request.getPregnancyWeight());
        record.setPregnancyHeight(request.getPregnancyHeight());
        record.setNotes(request.getNotes());
        record.setPrePregnancyWeight(request.getPrePregnancyWeight());
        record.setPrePregnancyHeight(request.getPrePregnancyHeight());

        float preBMI = record.getPrePregnancyBMI();
        float currentBMI = record.getCurrentBMI();
        record.setAlertStatus(determineAlertStatus(
                record.getPrePregnancyBMI(),
                record.getCurrentBMI(),
                request.getPrePregnancyWeight(),
                request.getPregnancyWeight(),
                request.getPregnancyWeek(),
                request.getPrePregnancyHeight()
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

    private AlertStatus determineAlertStatus(float preBMI, float currentBMI, float preWeight, float currentWeight, int pregnancyWeek, float prePregnancyHeight) {
        String warning = checkWeightGainWarning(preWeight, currentWeight, pregnancyWeek, preBMI);

        if (currentBMI < preBMI - 2 || warning.contains("too little")) {
            return AlertStatus.LOW;
        }
        else if (currentBMI > preBMI + 3 || warning.contains("too much")) {
            return AlertStatus.HIGH;
        }
        return AlertStatus.NORMAL;
    }

    private String checkWeightGainWarning(float preWeight, float currentWeight, int pregnancyWeek, float preBMI) {
        float expectedMin = 0, expectedMax = 0;
        float weightGain = currentWeight - preWeight;

        if (preBMI >= 18.5 && preBMI <= 24.9) { // BMI normal
            expectedMin = (pregnancyWeek <= 12) ? 1 : (pregnancyWeek <= 24) ? 4 : 10;
            expectedMax = (pregnancyWeek <= 12) ? 1 : (pregnancyWeek <= 24) ? 5 : 12;
        } else if (preBMI < 18.5) { // BMI low
            expectedMin = preWeight * 0.25f;
            expectedMax = 18.3f;
        } else if (preBMI >= 25) { // BMI high
            expectedMin = preWeight * 0.15f;
            expectedMax = 11.3f;
        }

        if (weightGain < expectedMin) return "Gaining too little weight can affect the fetus";
        if (weightGain > expectedMax) return "Gain too much weight, need to control diet";
        return "Normal weight gain";
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

}