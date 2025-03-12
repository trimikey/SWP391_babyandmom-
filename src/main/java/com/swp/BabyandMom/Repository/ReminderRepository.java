package com.swp.BabyandMom.Repository;

import com.swp.BabyandMom.Entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByReminderDateTimeBetween(LocalDateTime reminderDateTime, LocalDateTime reminderDateTime2);
    List<Reminder> findByIsDeletedFalse();
    Reminder findByIdAndIsDeletedFalse(Long id);
}
