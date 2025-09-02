package com.co.edu.uniquindio.libreria.service;

import com.co.edu.uniquindio.libreria.entity.Book;
import com.co.edu.uniquindio.libreria.repository.BookRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> searchBooks(String title, String author, String isbn) {
        Specification<Book> spec = Specification.where(null);

        if (title != null && !title.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (author != null && !author.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%"));
        }

        if (isbn != null && !isbn.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isbn"), isbn));
        }

        return bookRepository.findAll(spec);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}