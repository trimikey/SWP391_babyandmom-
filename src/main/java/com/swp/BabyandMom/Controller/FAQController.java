package com.swp.BabyandMom.Controller;

import com.swp.BabyandMom.DTO.FAQRequestDTO;
import com.swp.BabyandMom.DTO.FAQResponseDTO;
import com.swp.BabyandMom.Service.FAQService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
public class FAQController {
    private final FAQService faqService;

    public FAQController(FAQService faqService) {
        this.faqService = faqService;
    }

    @GetMapping
    public ResponseEntity<List<FAQResponseDTO>> getAllFAQs() {
        return ResponseEntity.ok(faqService.getAllFAQs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FAQResponseDTO> getFAQById(@PathVariable Long id) {
        return ResponseEntity.ok(faqService.getFAQById(id));
    }

    @PostMapping
    public ResponseEntity<FAQResponseDTO> createFAQ(@RequestBody FAQRequestDTO request) {
        return ResponseEntity.ok(faqService.createFAQ(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FAQResponseDTO> updateFAQ(@PathVariable Long id, @RequestBody FAQRequestDTO request) {
        return ResponseEntity.ok(faqService.updateFAQ(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFAQ(@PathVariable Long id) {
        faqService.deleteFAQ(id);
        return ResponseEntity.ok("FAQ đã được xóa thành công");
    }
}
