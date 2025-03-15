package com.swp.BabyandMom.Repository;

import com.swp.BabyandMom.Entity.Reminder;
import com.swp.BabyandMom.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByReminderDateTimeBetween(LocalDate startDate, LocalDate endDate);
    List<Reminder> findByIsDeletedFalse();
    Reminder findByIdAndIsDeletedFalse(Long id);

    // add them tu TRI
    List<Reminder> findByPregnancy_UserAndIsDeletedFalse(User user);
}
