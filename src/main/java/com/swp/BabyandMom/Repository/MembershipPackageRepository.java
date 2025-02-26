package com.swp.BabyandMom.Repository;

import com.swp.BabyandMom.Entity.Membership_Package;
import com.swp.BabyandMom.Entity.Enum.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipPackageRepository extends JpaRepository<Membership_Package, Long> {
    Optional<Membership_Package> findByType(MembershipType type);
} 