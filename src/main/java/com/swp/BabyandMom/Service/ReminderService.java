package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.ReminderRequestDTO;
import com.swp.BabyandMom.DTO.ReminderResponseDTO;
import com.swp.BabyandMom.Entity.Enum.RoleType;
import com.swp.BabyandMom.Entity.Pregnancy_Profile;
import com.swp.BabyandMom.Entity.Reminder;
import com.swp.BabyandMom.Entity.User;
import com.swp.BabyandMom.Repository.PregnancyRepository;
import com.swp.BabyandMom.Repository.ReminderRepository;
import com.swp.BabyandMom.Utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository repository;
    private final UserUtils userUtils;
    private final PregnancyRepository pregnancyRepository;

//    public List<ReminderResponseDTO> getAllReminders() {
//        return repository.findByIsDeletedFalse().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
public List<ReminderResponseDTO> getAllReminders() {
    User currentUser = userUtils.getCurrentAccount();

    // Nếu là ADMIN thì có thể xem tất cả
    if (currentUser.getRole().equals(RoleType.ADMIN)) {
        return repository.findByIsDeletedFalse().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Người dùng thường chỉ xem được lời nhắc của mình
    return repository.findByPregnancy_UserAndIsDeletedFalse(currentUser).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
}

    public ReminderResponseDTO getReminderById(Long id) {
        Reminder reminder = repository.findByIdAndIsDeletedFalse(id);
        if (reminder == null) {
            throw new RuntimeException("Reminder not found or has been deleted");
        }
        return convertToDTO (reminder);
    }


    public ReminderResponseDTO createReminder(ReminderRequestDTO request) {
        Reminder reminder = new Reminder();
        Pregnancy_Profile pregnancy = pregnancyRepository.findById(request.getPregnancyId())
                .orElseThrow(() -> new RuntimeException("Pregnancy not found"));
        reminder.setPregnancy(pregnancy);
        reminder.setTitle(request.getTitle());
        reminder.setType(request.getType());
        reminder.setDescription(request.getDescription());
        reminder.setReminderDateTime(request.getReminderDateTime());

        repository.save(reminder);
        return convertToDTO(reminder);
    }

    public ReminderResponseDTO updateReminder(Long id, ReminderRequestDTO request) {
        Reminder reminder = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));
        Pregnancy_Profile profile = pregnancyRepository.findById(request.getPregnancyId()).orElseThrow(() -> new RuntimeException("Pregnancy not found"));
        validateOwnershipOrAdmin(reminder.getPregnancy().getUser());
        reminder.setPregnancy(profile);
        reminder.setTitle(request.getTitle());
        reminder.setDescription(request.getDescription());
        reminder.setType(request.getType());
        reminder.setReminderDateTime(request.getReminderDateTime());
        repository.save(reminder);
        return convertToDTO(reminder);
    }

    public void deleteReminder(Long id) {
        Reminder reminder = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        validateOwnershipOrAdmin(reminder.getPregnancy().getUser());

        reminder.setIsDeleted(true);
        repository.save(reminder);
    }


    private void validateOwnershipOrAdmin(User reminderOwner) {
        User currentUser = userUtils.getCurrentAccount();
        if (currentUser == null || (!reminderOwner.getId().equals(currentUser.getId()) && !currentUser.getRole().equals(RoleType.ADMIN))) {
            throw new AccessDeniedException("You do not have permission to modify this reminder");
        }
    }

    private ReminderResponseDTO convertToDTO(Reminder reminder) {
        return ReminderResponseDTO.builder()
                .id(reminder.getId())
                .description(reminder.getDescription())
                .pregnancyId(reminder.getPregnancy().getId())
                .reminderDateTime(reminder.getReminderDateTime())
                .title(reminder.getTitle())
                .type(reminder.getType())
                .build();
    }
}
