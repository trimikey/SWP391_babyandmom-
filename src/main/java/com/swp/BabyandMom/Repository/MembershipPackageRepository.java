package com.swp.BabyandMom.Repository;

import com.swp.BabyandMom.Entity.Membership_Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipPackageRepository extends JpaRepository<Membership_Package, Long> {
} 