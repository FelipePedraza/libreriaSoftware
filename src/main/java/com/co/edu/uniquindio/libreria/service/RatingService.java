
package com.co.edu.uniquindio.libreria.service;

import com.co.edu.uniquindio.libreria.entity.Book;
import com.co.edu.uniquindio.libreria.entity.Rating;
import com.co.edu.uniquindio.libreria.repository.BookRepository;
import com.co.edu.uniquindio.libreria.repository.RatingRepository;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;

    public RatingService(RatingRepository ratingRepository, BookRepository bookRepository) {
        this.ratingRepository = ratingRepository;
        this.bookRepository = bookRepository;
    }

    public Rating createRating(Integer ratingValue, Long bookId, String userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        Rating rating = new Rating();
        rating.setRating(ratingValue);
        rating.setBook(book);
        rating.setUserId(userId);

        return ratingRepository.save(rating);
    }
}
