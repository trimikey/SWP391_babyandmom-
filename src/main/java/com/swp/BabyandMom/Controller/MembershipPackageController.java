package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.CreateMembershipPackageRequest;
import com.swp.BabyandMom.DTO.MembershipPackageDTO;
import com.swp.BabyandMom.DTO.UpdateMembershipPackageRequest;   
import com.swp.BabyandMom.Service.MembershipPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/membership-packages")
@CrossOrigin("*")
public class MembershipPackageController {

    @Autowired
    private MembershipPackageService membershipPackageService;

    @GetMapping
    public ResponseEntity<List<MembershipPackageDTO>> getAllPackages() {
        return ResponseEntity.ok(membershipPackageService.getAllPackages());
    }

    @GetMapping("/active")
    public ResponseEntity<List<MembershipPackageDTO>> getActivePackages() {
        return ResponseEntity.ok(membershipPackageService.getActivePackages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembershipPackageDTO> getPackageById(@PathVariable Long id) {
        return ResponseEntity.ok(membershipPackageService.getPackageById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MembershipPackageDTO> createPackage(@RequestBody CreateMembershipPackageRequest request) {
        return new ResponseEntity<>(membershipPackageService.createPackage(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MembershipPackageDTO> updatePackage(
            @PathVariable Long id,
            @RequestBody UpdateMembershipPackageRequest request) {
        return ResponseEntity.ok(membershipPackageService.updatePackage(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deletePackage(@PathVariable Long id) {
        membershipPackageService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }
} 