package com.co.edu.uniquindio.libreria.repository;

import com.co.edu.uniquindio.libreria.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    /**
     * Find all ratings for a specific book
     */
    List<Rating> findByBookId(Long bookId);
    
    /**
     * Find rating by book and user (to check if user already rated this book)
     */
    Optional<Rating> findByBookIdAndUserId(Long bookId, String userId);
    
    /**
     * Calculate average rating for a book
     */
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.book.id = :bookId")
    Double findAverageRatingByBookId(@Param("bookId") Long bookId);
    
    /**
     * Count total ratings for a book
     */
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.book.id = :bookId")
    Long countRatingsByBookId(@Param("bookId") Long bookId);
    
    /**
     * Find all ratings by a specific user
     */
    List<Rating> findByUserId(String userId);
    
    /**
     * Find ratings within a specific range
     */
    List<Rating> findByRatingBetween(Integer minRating, Integer maxRating);
}
