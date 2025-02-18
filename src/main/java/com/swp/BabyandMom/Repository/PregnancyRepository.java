package com.swp.BabyandMom.Repository;
import com.swp.BabyandMom.Entity.Pregnancy_Profile;
import com.swp.BabyandMom.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PregnancyRepository extends JpaRepository<Pregnancy_Profile, Long> {
    List<Pregnancy_Profile> findByUser(User user);

}

