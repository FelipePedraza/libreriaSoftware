package com.co.edu.uniquindio.libreria.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String description;
    private String isbn;
    private LocalDate publicationDate;
    private BigDecimal price;
    private Integer stockQuantity;
    private String genre;
    private String publisher;
    private BigDecimal averageRating;
    private Integer totalRatings;
    private List<com.co.edu.uniquindio.libreria.dto.RatingResponse> ratings;
}
