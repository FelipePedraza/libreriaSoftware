package com.co.edu.uniquindio.libreria.controller;

import com.co.edu.uniquindio.libreria.dto.*;
import com.co.edu.uniquindio.libreria.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class BookController {
    
    private final BookService bookService;
    
    @GetMapping("/search")
    public ResponseEntity<List<BookResponse>> searchBooks(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean inStock) {
        
        log.info("Search request - term: {}, genre: {}, inStock: {}", searchTerm, genre, inStock);
        
        BookSearchRequest request = new BookSearchRequest(searchTerm, genre, inStock);
        List<BookResponse> books = bookService.searchBooks(request);
        
        return ResponseEntity.ok(books);
    }
    
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        log.info("Getting all books");
        List<BookResponse> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        log.info("Getting book with id: {}", id);
        return bookService.getBookById(id)
                .map(book -> ResponseEntity.ok(book))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/rate")
    public ResponseEntity<RatingResponse> rateBook(@RequestBody RatingRequest request) {
        log.info("Rating request for book {} by user {}", request.getBookId(), request.getUserId());
        
        try {
            RatingResponse rating = bookService.rateBook(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(rating);
        } catch (RuntimeException e) {
            log.error("Error rating book: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/ratings")
    public ResponseEntity<List<RatingResponse>> getBookRatings(@PathVariable Long id) {
        log.info("Getting ratings for book with id: {}", id);
        List<RatingResponse> ratings = bookService.getBookRatings(id);
        return ResponseEntity.ok(ratings);
    }
    
    @GetMapping("/ratings/user/{userId}")
    public ResponseEntity<List<RatingResponse>> getUserRatings(@PathVariable String userId) {
        log.info("Getting ratings for user: {}", userId);
        List<RatingResponse> ratings = bookService.getUserRatings(userId);
        return ResponseEntity.ok(ratings);
    }
    
    @GetMapping("/simple-search")
    public ResponseEntity<List<BookResponse>> simpleSearch(@RequestParam String q) {
        log.info("Simple search request for: {}", q);
        
        BookSearchRequest request = new BookSearchRequest(q, null, null);
        List<BookResponse> books = bookService.searchBooks(request);
        
        return ResponseEntity.ok(books);
    }
}
