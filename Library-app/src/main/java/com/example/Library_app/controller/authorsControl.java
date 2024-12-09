package com.example.Library_app.controller;

import com.example.Library_app.model.Author;
import com.example.Library_app.repository.AuthorsRep;
import com.example.Library_app.repository.BooksRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class authorsControl {

    @Autowired
    private BooksRep booksRep;
    @Autowired
    private AuthorsRep authorsRep;

    // Show all authors
    @GetMapping("/authors")
    public String getAuthors(Model model) {
        model.addAttribute("authors", authorsRep.findAll());
        return "authors";
    }

    // Show add author form
    @GetMapping("/add-author")
    public String showAddAuthorForm(Model model) {
        model.addAttribute("author", new Author());
        return "add-author";  // Corresponds to add-author.html
    }

    // Handle add author form submission
    @PostMapping("/add-author")
    public String addAuthor(@ModelAttribute Author author) {
        authorsRep.save(author);
        return "redirect:/authors";
    }

    // Delete an author by ID
    @GetMapping("/delete-author/{id}")
    public String deleteAuthor(@PathVariable("id") Long id) {
        authorsRep.deleteById(id);
        return "redirect:/authors";  // Redirect to authors list
    }

    // Show edit author form
    @GetMapping("/edit-author/{id}")
    public String showEditAuthorForm(@PathVariable("id") Long id, Model model) {
        Optional<Author> author = authorsRep.findById(id);
        if (author.isPresent()) {
            model.addAttribute("author", author.get());
        } else {
            model.addAttribute("error", "Author not found.");
        }
        return "edit-author";
    }

    // Handle edit author form submission
    @PostMapping("/edit-author/{id}")
    public String editAuthor(@PathVariable("id") Long id, @ModelAttribute Author author) {
        if (authorsRep.existsById(id)) {
            author.setId(id);
            authorsRep.save(author);
        }
        return "redirect:/authors";
    }

    // Show books associated with a specific author (without author details)
    @GetMapping("/author-info/{id}")
    public String getBooksWithoutAuthor(@PathVariable("id") Long id, Model model) {
        Optional<Author> author = authorsRep.findById(id);
        if(author.isPresent()){
            model.addAttribute("author", author.get());
        }else {
            model.addAttribute("error", "Author not found.");
        }
        // Add books to model
        return "author-info";
    }

    // Show books by author
    @GetMapping("/full_info_author/{id}")
    public String getAuthorAll(@PathVariable("id") Long id, Model model) {
        Optional<Author> author = authorsRep.findById(id);
        if (author.isPresent()) {
            model.addAttribute("author", author.get());
            model.addAttribute("books", booksRep.findByAuthorId(id));
            return "full_info_author";
        }
        return "redirect:/authors";
    }
}
