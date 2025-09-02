
package com.co.edu.uniquindio.libreria.controller;

import com.co.edu.uniquindio.libreria.entity.Review;
import com.co.edu.uniquindio.libreria.service.ReviewService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review createReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.createReview(reviewDTO.getReviewText(), reviewDTO.getBookId());
    }

    @Data
    public static class ReviewDTO {
        private String reviewText;
        private Long bookId;
    }
}
