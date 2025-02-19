package com.swp.BabyandMom.Service;

import com.swp.BabyandMom.DTO.FAQRequestDTO;
import com.swp.BabyandMom.DTO.FAQResponseDTO;
import com.swp.BabyandMom.Entity.FAQ;
import com.swp.BabyandMom.Repository.FAQRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FAQService {
    private final FAQRepository faqRepository;

    public FAQService(FAQRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public List<FAQResponseDTO> getAllFAQs() {
        return faqRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public FAQResponseDTO getFAQById(Long id) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));
        return convertToDTO(faq);
    }

    public FAQResponseDTO createFAQ(FAQRequestDTO request) {
        FAQ faq = new FAQ();
        faq.setName(request.getName());
        faq.setDescription(request.getDescription());
        faq.setDisplayOrder(request.getDisplayOrder());
        faq.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        faq.setCreatedAt(LocalDateTime.now());

        faqRepository.save(faq);
        return convertToDTO(faq);
    }

    public FAQResponseDTO updateFAQ(Long id, FAQRequestDTO request) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));

        faq.setName(request.getName());
        faq.setDescription(request.getDescription());
        faq.setDisplayOrder(request.getDisplayOrder());
        faq.setIsActive(request.getIsActive());
        faq.setUpdatedAt(LocalDateTime.now());

        faqRepository.save(faq);
        return convertToDTO(faq);
    }

    public void deleteFAQ(Long id) {
        FAQ faq = faqRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FAQ not found"));
        faqRepository.delete(faq);
    }

    private FAQResponseDTO convertToDTO(FAQ faq) {
        return new FAQResponseDTO(
                faq.getId(),
                faq.getName(),
                faq.getDescription(),
                faq.getDisplayOrder(),
                faq.getIsActive(),
                faq.getCreatedAt(),
                faq.getUpdatedAt()
        );
    }
}