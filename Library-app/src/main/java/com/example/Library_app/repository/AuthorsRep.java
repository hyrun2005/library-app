package com.example.Library_app.repository;

import com.example.Library_app.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorsRep extends JpaRepository<Author, Long> {

}
