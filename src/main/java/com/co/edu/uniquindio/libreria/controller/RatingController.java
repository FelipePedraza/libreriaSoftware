
package com.co.edu.uniquindio.libreria.controller;

import com.co.edu.uniquindio.libreria.dto.RatingRequest;
import com.co.edu.uniquindio.libreria.dto.RatingResponse;
import com.co.edu.uniquindio.libreria.entity.Rating;
import com.co.edu.uniquindio.libreria.service.RatingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "http://localhost:3000")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public RatingResponse createRating(@RequestBody RatingRequest ratingRequest) {
        Rating rating = ratingService.createRating(
            ratingRequest.getRating(),
            ratingRequest.getBookId(),
            ratingRequest.getUserId()
        );

        return RatingResponse.builder()
                .id(rating.getId())
                .bookId(rating.getBook().getId())
                .userId(rating.getUserId())
                .rating(rating.getRating())
                .createdAt(rating.getCreatedAt())
                .build();
    }
}
