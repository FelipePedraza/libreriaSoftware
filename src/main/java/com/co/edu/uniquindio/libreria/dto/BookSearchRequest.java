package com.co.edu.uniquindio.libreria.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchRequest {
    private String searchTerm;
    private String genre;
    private Boolean inStock;
}
