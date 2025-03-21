package com.swp.BabyandMom.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BlogPostRequestDTO {
    private String title;
    private String content;
}
