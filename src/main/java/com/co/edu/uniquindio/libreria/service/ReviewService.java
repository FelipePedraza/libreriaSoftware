
package com.co.edu.uniquindio.libreria.service;

import com.co.edu.uniquindio.libreria.entity.Book;
import com.co.edu.uniquindio.libreria.entity.Review;
import com.co.edu.uniquindio.libreria.repository.BookRepository;
import com.co.edu.uniquindio.libreria.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    public Review createReview(String reviewText, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        Review review = new Review();
        review.setReviewText(reviewText);
        review.setBook(book);

        return reviewRepository.save(review);
    }
}
