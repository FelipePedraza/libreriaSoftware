package com.co.edu.uniquindio.libreria.service;

import com.co.edu.uniquindio.libreria.dto.*;
import com.co.edu.uniquindio.libreria.entity.Book;
import com.co.edu.uniquindio.libreria.entity.Rating;
import com.co.edu.uniquindio.libreria.repository.BookRepository;
import com.co.edu.uniquindio.libreria.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookService {
    
    private final BookRepository bookRepository;
    private final RatingRepository ratingRepository;
    
    /**
     * Search for books by title or author
     */
    @Transactional(readOnly = true)
    public List<BookResponse> searchBooks(BookSearchRequest request) {
        log.info("Searching books with term: {}", request.getSearchTerm());
        
        List<Book> books;
        
        if (request.getSearchTerm() != null && !request.getSearchTerm().trim().isEmpty()) {
            books = bookRepository.searchByTitleOrAuthor(request.getSearchTerm().trim());
        } else {
            books = bookRepository.findAll();
        }
        
        // Apply additional filters
        if (request.getGenre() != null && !request.getGenre().trim().isEmpty()) {
            books = books.stream()
                    .filter(book -> book.getGenre() != null && 
                            book.getGenre().equalsIgnoreCase(request.getGenre().trim()))
                    .collect(Collectors.toList());
        }
        
        if (request.getInStock() != null && request.getInStock()) {
            books = books.stream()
                    .filter(book -> book.getStockQuantity() != null && book.getStockQuantity() > 0)
                    .collect(Collectors.toList());
        }
        
        return books.stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all books
     */
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToBookResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get book by ID
     */
    @Transactional(readOnly = true)
    public Optional<BookResponse> getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToBookResponse);
    }
    
    /**
     * Rate a book
     */
    public RatingResponse rateBook(RatingRequest request) {
        log.info("Rating book {} with rating {} by user {}", 
                request.getBookId(), request.getRating(), request.getUserId());
        
        // Validate book exists
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + request.getBookId()));
        
        // Check if user already rated this book
        Optional<Rating> existingRating = ratingRepository.findByBookIdAndUserId(
                request.getBookId(), request.getUserId());
        
        Rating rating;
        if (existingRating.isPresent()) {
            // Update existing rating
            rating = existingRating.get();
            rating.setRating(request.getRating());
            rating.setComment(request.getComment());
            log.info("Updated existing rating for book {} by user {}", 
                    request.getBookId(), request.getUserId());
        } else {
            // Create new rating
            rating = new Rating();
            rating.setBook(book);
            rating.setUserId(request.getUserId());
            rating.setRating(request.getRating());
            rating.setComment(request.getComment());
            log.info("Created new rating for book {} by user {}", 
                    request.getBookId(), request.getUserId());
        }
        
        rating = ratingRepository.save(rating);
        
        // Update book's average rating and total ratings count
        updateBookRatingStats(book);
        
        return convertToRatingResponse(rating);
    }
    
    /**
     * Get ratings for a specific book
     */
    @Transactional(readOnly = true)
    public List<RatingResponse> getBookRatings(Long bookId) {
        return ratingRepository.findByBookId(bookId).stream()
                .map(this::convertToRatingResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get user's ratings
     */
    @Transactional(readOnly = true)
    public List<RatingResponse> getUserRatings(String userId) {
        return ratingRepository.findByUserId(userId).stream()
                .map(this::convertToRatingResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Update book's average rating and total ratings count
     */
    private void updateBookRatingStats(Book book) {
        Double averageRating = ratingRepository.findAverageRatingByBookId(book.getId());
        Long totalRatings = ratingRepository.countRatingsByBookId(book.getId());
        
        if (averageRating != null) {
            book.setAverageRating(BigDecimal.valueOf(averageRating).setScale(2, RoundingMode.HALF_UP));
        } else {
            book.setAverageRating(BigDecimal.ZERO);
        }
        
        book.setTotalRatings(totalRatings.intValue());
        bookRepository.save(book);
        
        log.info("Updated rating stats for book {}: average={}, total={}", 
                book.getId(), book.getAverageRating(), book.getTotalRatings());
    }
    
    /**
     * Convert Book entity to BookResponse DTO
     */
    private BookResponse convertToBookResponse(Book book) {
        List<RatingResponse> ratings = book.getRatings() != null ? 
                book.getRatings().stream()
                        .map(this::convertToRatingResponse)
                        .collect(Collectors.toList()) : 
                List.of();
        
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .isbn(book.getIsbn())
                .publicationDate(book.getPublicationDate())
                .price(book.getPrice())
                .stockQuantity(book.getStockQuantity())
                .genre(book.getGenre())
                .publisher(book.getPublisher())
                .averageRating(book.getAverageRating())
                .totalRatings(book.getTotalRatings())
                .ratings(ratings)
                .build();
    }
    
    /**
     * Convert Rating entity to RatingResponse DTO
     */
    private RatingResponse convertToRatingResponse(Rating rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .bookId(rating.getBook().getId())
                .userId(rating.getUserId())
                .rating(rating.getRating())
                .comment(rating.getComment())
                .createdAt(rating.getCreatedAt())
                .build();
    }
}
