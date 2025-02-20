package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.CreateMembershipPackageRequest;
import com.swp.BabyandMom.DTO.MembershipPackageDTO;
import com.swp.BabyandMom.DTO.UpdateMembershipPackageRequest;
import com.swp.BabyandMom.Entity.Membership_Package;
import com.swp.BabyandMom.Repository.MembershipPackageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipPackageService {
    private final MembershipPackageRepository repository;

    public MembershipPackageService(MembershipPackageRepository repository) {
        this.repository = repository;
    }

    public List<MembershipPackageDTO> getAllPackages() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MembershipPackageDTO> getActivePackages() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MembershipPackageDTO getPackageById(Long id) {
        Membership_Package pkg = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));
        return convertToDTO(pkg);
    }

    public MembershipPackageDTO createPackage(CreateMembershipPackageRequest request) {
        Membership_Package pkg = new Membership_Package();
        pkg.setType(request.getType());
        pkg.setFeatures(request.getFeatures());
        pkg.setPrice(request.getPrice());
        pkg.setDurationInMonths(request.getDurationInMonths());
        
        return convertToDTO(repository.save(pkg));
    }

    public MembershipPackageDTO updatePackage(Long id, UpdateMembershipPackageRequest request) {
        Membership_Package pkg = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        pkg.setType(request.getType());
        pkg.setFeatures(request.getFeatures());
        pkg.setPrice(request.getPrice());
        pkg.setDurationInMonths(request.getDurationInMonths());

        return convertToDTO(repository.save(pkg));
    }

    public void deletePackage(Long id) {
        repository.deleteById(id);
    }

    private MembershipPackageDTO convertToDTO(Membership_Package pkg) {
        return new MembershipPackageDTO(
                pkg.getId(),
                pkg.getType(),
                pkg.getFeatures(),
                pkg.getPrice(),
                pkg.getDurationInMonths()
        );
    }
} 