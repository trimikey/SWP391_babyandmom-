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

    private Pregnancy_Profile getPregnancyProfileOfCurrentUser() {
        User currentUser = userUtils.getCurrentAccount();
        return pregnancyRepository.findByUser(currentUser)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("User's pregnancy record not found"));
    }
    private Pregnancy_Profile getPregnancyProfileById(Long profileId) {
        return pregnancyRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Pregnancy profile not found"));
    }



    public List<GrowthRecordResponseDTO> getGrowthRecordsByCurrentUser() {
        Pregnancy_Profile pregnancy = getPregnancyProfileOfCurrentUser();
        return repository.findByPregnancy(pregnancy).stream()
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
        Pregnancy_Profile pregnancy = getPregnancyProfileById(request.getProfileId()); // Lấy profile theo ID

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
                request.getPregnancyWeek()
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

        // Cập nhật cảnh báo BMI
        float preBMI = record.getPrePregnancyBMI();
        float currentBMI = record.getCurrentBMI();
        record.setAlertStatus(determineAlertStatus(
                record.getPrePregnancyBMI(),
                record.getCurrentBMI(),
                request.getPrePregnancyWeight(),
                request.getPregnancyWeight(),
                request.getPregnancyWeek()
        ));


        repository.save(record);
        return convertToDTO(record);
    }

    public void deleteRecord(Long id) {
        Growth_Record record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Growth record not found"));
        validateOwnership(record.getPregnancy().getId());
        repository.delete(record);
    }

    private void validateOwnership(Long profileId) {
        User currentUser = userUtils.getCurrentAccount();
        Pregnancy_Profile pregnancy = pregnancyRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Pregnancy profile not found"));

        if (!pregnancy.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this pregnancy profile");
        }
    }

    private AlertStatus determineAlertStatus(float preBMI, float currentBMI, float preWeight, float currentWeight, int pregnancyWeek) {
        String weightWarning = checkWeightGainWarning(preWeight, currentWeight, pregnancyWeek);

        if (currentBMI < preBMI - 2) {
            return AlertStatus.LOW;  // BMI giảm mạnh -> cảnh báo thấp
        } else if (currentBMI > preBMI + 3) {
            return AlertStatus.HIGH; // BMI tăng mạnh -> cảnh báo cao
        } else if (weightWarning.contains("too little") || weightWarning.contains("too much")) {
            return AlertStatus.WARNING; // Nếu cân nặng tăng bất thường nhưng BMI vẫn ổn
        }
        return AlertStatus.NORMAL;
    }


    private String checkWeightGainWarning(float preWeight, float currentWeight, int pregnancyWeek) {
        float expectedMin = 0, expectedMax = 0;
        float weightGain = currentWeight - preWeight;

        float preBMI = preWeight / (preWeight * preWeight);

        if (preBMI >= 18.5 && preBMI <= 24.9) { // BMI bình thường
            expectedMin = (pregnancyWeek <= 12) ? 1 : (pregnancyWeek <= 24) ? 4 : 10;
            expectedMax = (pregnancyWeek <= 12) ? 1 : (pregnancyWeek <= 24) ? 5 : 12;
        } else if (preBMI < 18.5) { // BMI thấp
            expectedMin = preWeight * 0.25f;
            expectedMax = 18.3f;
        } else if (preBMI >= 25) { // BMI cao
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
                checkWeightGainWarning(record.getPrePregnancyWeight(), record.getPregnancyWeight(), record.getPregnancyWeek()),
                record.getAlertStatus(),
                record.getCreatedAt()
        );
    }
}
