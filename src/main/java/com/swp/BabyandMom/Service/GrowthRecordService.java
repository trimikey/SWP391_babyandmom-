package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.GrowthRecordRequestDTO;
import com.swp.BabyandMom.DTO.GrowthRecordResponseDTO;
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

    public List<GrowthRecordResponseDTO> getGrowthRecordsByProfile(Long profileId) {
        Pregnancy_Profile pregnancy = validateOwnership(profileId);
        return repository.findByPregnancy(pregnancy).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public GrowthRecordResponseDTO createGrowthRecord(GrowthRecordRequestDTO request) {
        Pregnancy_Profile pregnancy = validateOwnership(request.getProfileId());

        Growth_Record record = new Growth_Record();
        record.setPregnancy(pregnancy);
        record.setPregnancyWeek(request.getPregnancyWeek());
        record.setPregnancyWeight(request.getPregnancyWeight());
        record.setPregnancyHeight(request.getPregnancyHeight());
        record.setNotes(request.getNotes());
        record.setPrePregnancyWeight(request.getPrePregnancyWeight());
        record.setPrePregnancyHeight(request.getPrePregnancyHeight());
        record.setCreatedAt(LocalDateTime.now());

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

        repository.save(record);
        return convertToDTO(record);
    }

    private Pregnancy_Profile validateOwnership(Long profileId) {
        User currentUser = userUtils.getCurrentAccount();
        Pregnancy_Profile pregnancy = pregnancyRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Pregnancy profile not found"));

        if (!pregnancy.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not have permission to access this pregnancy profile");
        }
        return pregnancy;
    }

    public List<GrowthRecordResponseDTO> getGrowthRecordsByCurrentUser() {
        User currentUser = userUtils.getCurrentAccount();
        Pregnancy_Profile pregnancy = pregnancyRepository.findByUser(currentUser)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No pregnancy profile found for current user"));

        return repository.findByPregnancy(pregnancy).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
                record.getCreatedAt()
        );
    }
}
