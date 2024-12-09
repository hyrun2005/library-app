package com.example.Library_app.controller;

import com.example.Library_app.model.Book;
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
public class booksControl {
        @Autowired
        private BooksRep booksRep;

        @Autowired
        private AuthorsRep authorsRep;

        // Show all books
        @GetMapping("/books")
        public String getBooks(Model model) {
            model.addAttribute("books", booksRep.findAll());
            return "books";
        }


        @GetMapping("/add-book")
        public String showAddBookForm(Model model) {
            model.addAttribute("book", new Book());
            model.addAttribute("authors", authorsRep.findAll());
            return "add-book";
        }

        @PostMapping("/add-book")
        public String addBook(@ModelAttribute Book book) {
            booksRep.save(book);
            return "redirect:/books";
        }

        @GetMapping("/delete-book/{id}")
        public String deleteBook(@PathVariable("id") Long id) {
            booksRep.deleteById(id);
            return "redirect:/books";
        }

        // Show edit book form
        @GetMapping("/edit-book/{id}")
        public String showEditBookForm(@PathVariable("id") Long id, Model model) {
            Optional<Book> book = booksRep.findById(id);
            if (book.isPresent()) {
                model.addAttribute("book", book.get());
                model.addAttribute("authors", authorsRep.findAll());
            } else {
                model.addAttribute("error", "Book not found.");
            }
            return "edit-book";
        }

        // Handle edit book form submission
        @PostMapping("/edit-book/{id}")
        public String editBook(@PathVariable("id") Long id, @ModelAttribute Book book) {
            if (booksRep.existsById(id)) {
                book.setId(id);
                booksRep.save(book);
            }
            return "redirect:/books"; // Redirect back to books list
        }

        @GetMapping("/book-info/{id}")
        public String getBooksWithoutAuthor(@PathVariable("id") Long id, Model model) {
            Optional<Book> book = booksRep.findById(id);

            if (book.isPresent()) {
                model.addAttribute("book", book.get());
            } else {
                model.addAttribute("error", "Book not found.");
            }
            return "book-info";
        }

        // Show books by author
        @GetMapping("/full_info_book/{id}")
        public String getAuthorAll(@PathVariable("id") Long id, Model model) {
            Optional<Book> book = booksRep.findById(id);
            if (book.isPresent()) {
                model.addAttribute("book", book.get());
                model.addAttribute("author", book.get().getAuthor());
                return "full_info_book";
            }
            return "redirect:/books";
        }
}
