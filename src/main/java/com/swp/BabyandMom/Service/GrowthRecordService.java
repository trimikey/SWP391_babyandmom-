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
        Pregnancy_Profile pregnancy = getPregnancyProfileOfCurrentUser();

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

    public void deleteRecord(Long id) {
        Growth_Record record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Growth record not found"));
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

    private Pregnancy_Profile getPregnancyProfileOfCurrentUser() {
        User currentUser = userUtils.getCurrentAccount();
        return pregnancyRepository.findByUser(currentUser)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No pregnancy profile found for current user"));
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
