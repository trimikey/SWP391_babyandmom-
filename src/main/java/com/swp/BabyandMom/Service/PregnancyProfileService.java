package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.PregnancyProfileRequestDTO;
import com.swp.BabyandMom.DTO.PregnancyProfileResponseDTO;
import com.swp.BabyandMom.Entity.Growth_Record;
import com.swp.BabyandMom.Entity.Pregnancy_Profile;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Repository.GrowthRecordRepository;
import com.swp.BabyandMom.Repository.PregnancyRepository;
import com.swp.BabyandMom.Utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PregnancyProfileService {
    private final PregnancyRepository repository;
    private final UserUtils userUtils;
    private final GrowthRecordRepository growthRecordRepository;
    public List<PregnancyProfileResponseDTO> getAllProfiles() {
        User currentUser = userUtils.getCurrentAccount();
        return repository.findByUserAndIsDeletedFalse(currentUser).stream()
                .map(profile -> new PregnancyProfileResponseDTO(
                        profile.getId(), profile.getBabyName(), profile.getBabyGender(), profile.getDueDate(),
                        profile.getCurrentWeek(), profile.getLastPeriod(),
                        profile.getHeight()))
                .collect(Collectors.toList());
    }

    public PregnancyProfileResponseDTO getPregnancyProfileById(Long profileId) {
        User currentUser = userUtils.getCurrentAccount();

        Pregnancy_Profile profile = repository.findByIdAndUserAndIsDeletedFalse(profileId, currentUser)
                .orElseThrow(() -> new RuntimeException("Pregnancy profile not found or access denied"));

        return new PregnancyProfileResponseDTO(
                profile.getId(),
                profile.getBabyName(),
                profile.getBabyGender(),
                profile.getDueDate(),
                profile.getCurrentWeek(),
                profile.getLastPeriod(),
                profile.getHeight()
        );
    }



    public PregnancyProfileResponseDTO createProfile(PregnancyProfileRequestDTO request) {
        User currentUser = userUtils.getCurrentAccount();
        Pregnancy_Profile profile = new Pregnancy_Profile();
        profile.setUser(currentUser);
        profile.setBabyName(request.getBabyName());
        profile.setBabyGender(request.getBabyGender());
        profile.setDueDate(request.getDueDate());
        profile.setCurrentWeek(request.getCurrentWeek());
        profile.setLastPeriod(request.getLastPeriod());
        profile.setHeight(request.getHeight());
        profile.setCreatedAt(LocalDateTime.now());
        repository.save(profile);
        return new PregnancyProfileResponseDTO(
                profile.getId(), profile.getBabyName(),profile.getBabyGender(),profile.getDueDate(),
                profile.getCurrentWeek(), profile.getLastPeriod(),
                profile.getHeight());
    }

    public PregnancyProfileResponseDTO updateProfile(Long id, PregnancyProfileRequestDTO request) {
        Pregnancy_Profile profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setBabyName(request.getBabyName());
        profile.setBabyGender(request.getBabyGender());
        profile.setDueDate(request.getDueDate());
        profile.setCurrentWeek(request.getCurrentWeek());
        profile.setLastPeriod(request.getLastPeriod());
        profile.setHeight(request.getHeight());
        profile.setUpdatedAt(LocalDateTime.now());
        repository.save(profile);

        return new PregnancyProfileResponseDTO(
                profile.getId(), profile.getBabyName(),profile.getBabyGender(), profile.getDueDate(),
                profile.getCurrentWeek(), profile.getLastPeriod(),
                profile.getHeight());
    }

//    public void deleteProfile(Long id) {
//        Pregnancy_Profile profile = repository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Profile not found"));
//        profile.setIsDeleted(true);
//        repository.save(profile);
//    }
    public void deleteProfile(Long id) {
        Pregnancy_Profile profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        List<Growth_Record> growthRecords = growthRecordRepository.findByPregnancyAndIsDeletedFalse(profile);

        for (Growth_Record record : growthRecords) {
            record.setIsDeleted(true);
            growthRecordRepository.save(record);
        }
        profile.setIsDeleted(true);
        repository.save(profile);
    }


}