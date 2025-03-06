package com.swp.BabyandMom.Repository;
import com.swp.BabyandMom.Entity.Pregnancy_Profile;
import com.swp.BabyandMom.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PregnancyRepository extends JpaRepository<Pregnancy_Profile, Long> {
    List<Pregnancy_Profile> findByUser(User user);
    List<Pregnancy_Profile> findByUserAndIsDeletedFalse(User user);
    Optional<Pregnancy_Profile> findByIdAndUserAndIsDeletedFalse(Long id, User user);

}

