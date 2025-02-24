package com.swp.BabyandMom.Repository;

import com.swp.BabyandMom.Entity.Growth_Record;
import com.swp.BabyandMom.Entity.Pregnancy_Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GrowthRecordRepository extends JpaRepository<Growth_Record, Long> {
    List<Growth_Record> findByPregnancy(Pregnancy_Profile pregnancy);
}
