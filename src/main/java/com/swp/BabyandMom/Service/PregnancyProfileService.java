package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.PregnancyProfileRequestDTO;
import com.swp.BabyandMom.DTO.PregnancyProfileResponseDTO;
import com.swp.BabyandMom.Entity.Pregnancy_Profile;
import com.swp.BabyandMom.Entity.User;
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

    public List<PregnancyProfileResponseDTO> getAllProfiles() {
        User currentUser = userUtils.getCurrentAccount();
        return repository.findByUser(currentUser).stream()
                .map(profile -> new PregnancyProfileResponseDTO(
                        profile.getId(), profile.getDueDate(),
                        profile.getCurrentWeek(), profile.getLastPeriod(),
                        profile.getHeight()))
                .collect(Collectors.toList());
    }

    public PregnancyProfileResponseDTO createProfile(PregnancyProfileRequestDTO request) {
        User currentUser = userUtils.getCurrentAccount();
        Pregnancy_Profile profile = new Pregnancy_Profile();
        profile.setUser(currentUser);
        profile.setDueDate(request.getDueDate());
        profile.setCurrentWeek(request.getCurrentWeek());
        profile.setLastPeriod(request.getLastPeriod());
        profile.setHeight(request.getHeight());
        profile.setCreatedAt(LocalDateTime.now());
        repository.save(profile);
        return new PregnancyProfileResponseDTO(
                profile.getId(), profile.getDueDate(),
                profile.getCurrentWeek(), profile.getLastPeriod(),
                profile.getHeight());
    }

    public PregnancyProfileResponseDTO updateProfile(Long id, PregnancyProfileRequestDTO request) {
        Pregnancy_Profile profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setDueDate(request.getDueDate());
        profile.setCurrentWeek(request.getCurrentWeek());
        profile.setLastPeriod(request.getLastPeriod());
        profile.setHeight(request.getHeight());
        profile.setUpdatedAt(LocalDateTime.now());
        repository.save(profile);

        return new PregnancyProfileResponseDTO(
                profile.getId(), profile.getDueDate(),
                profile.getCurrentWeek(), profile.getLastPeriod(),
                profile.getHeight());
    }

    public void deleteProfile(Long id) {
        Pregnancy_Profile profile = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        repository.delete(profile);
    }
}