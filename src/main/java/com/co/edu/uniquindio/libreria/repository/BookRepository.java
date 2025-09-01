package com.co.edu.uniquindio.libreria.repository;

import com.co.edu.uniquindio.libreria.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    /**
     * Search for books by title or author (case-insensitive)
     * @param searchTerm the search term to look for in title or author fields
     * @return list of books matching the search criteria
     */
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Book> searchByTitleOrAuthor(@Param("searchTerm") String searchTerm);
    
    /**
     * Find books by exact title (case-insensitive)
     */
    List<Book> findByTitleIgnoreCaseContaining(String title);
    
    /**
     * Find books by exact author (case-insensitive)
     */
    List<Book> findByAuthorIgnoreCaseContaining(String author);
    
    /**
     * Find books by genre
     */
    List<Book> findByGenreIgnoreCase(String genre);
    
    /**
     * Find books with stock available
     */
    List<Book> findByStockQuantityGreaterThan(Integer quantity);
}
