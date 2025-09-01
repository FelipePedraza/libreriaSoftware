package com.co.edu.uniquindio.libreria.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {
    
    private Long bookId;
    private String userId;
    private Integer rating;
    private String comment;
}
