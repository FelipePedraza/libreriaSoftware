package com.co.edu.uniquindio.libreria.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingResponse {
    private Long id;
    private Long bookId;
    private String userId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
