package com.example.Library_app.repository;

import com.example.Library_app.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRep extends JpaRepository<Book, Long> {
    // Method to find books by author ID
    List<Book> findByAuthorId(Long id);
}
