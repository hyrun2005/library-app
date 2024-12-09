package com.example.Library_app.controller;

import com.example.Library_app.model.Book;
import com.example.Library_app.repository.AuthorsRep;
import com.example.Library_app.repository.BooksRep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class booksControlTest {

    @InjectMocks
    private booksControl booksControl;

    @Mock
    private BooksRep booksRep;

    @Mock
    private AuthorsRep authorsRep;

    @Mock
    private Model model; // Мок для Model

    private Book book;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
    }

    @Test
    public void testGetBooks() {
        when(booksRep.findAll()).thenReturn(Arrays.asList(book));
        String viewName = booksControl.getBooks(model);
        verify(model).addAttribute("books", Arrays.asList(book));
        assertEquals("books", viewName);
    }

    @Test
    public void testShowAddBookForm() {
        String viewName = booksControl.showAddBookForm(model);
        verify(model).addAttribute("book", new Book());
        assertEquals("add-book", viewName);
    }

    @Test
    public void testAddBook() {
        String viewName = booksControl.addBook(book);
        verify(booksRep).save(book);
        assertEquals("redirect:/books", viewName);
    }

    @Test
    public void testDeleteBook() {
        String viewName = booksControl.deleteBook(1L);
        verify(booksRep).deleteById(1L);
        assertEquals("redirect:/books", viewName);
    }

    @Test
    public void testShowEditBookForm_WhenBookExists() {
        when(booksRep.findById(1L)).thenReturn(Optional.of(book));
        when(authorsRep.findAll()).thenReturn(Arrays.asList());
        String viewName = booksControl.showEditBookForm(1L, model);
        verify(model).addAttribute("book", book);
        assertEquals("edit-book", viewName);
    }

    @Test
    public void testShowEditBookForm_WhenBookNotFound() {
        when(booksRep.findById(1L)).thenReturn(Optional.empty());
        String viewName = booksControl.showEditBookForm(1L, model);
        verify(model).addAttribute("error", "Book not found.");
        assertEquals("edit-book", viewName);
    }

    @Test
    public void testEditBook() {
        when(booksRep.existsById(1L)).thenReturn(true);
        String viewName = booksControl.editBook(1L, book);
        verify(booksRep).save(book);
        assertEquals("redirect:/books", viewName);
    }

    @Test
    public void testGetBooksWithoutAuthor() {
        when(booksRep.findById(1L)).thenReturn(Optional.of(book));
        String viewName = booksControl.getBooksWithoutAuthor(1L, model);
        verify(model).addAttribute("book", book);
        assertEquals("book-info", viewName);
    }

    @Test
    public void testGetBooksWithoutAuthor_WhenBookNotFound() {
        when(booksRep.findById(1L)).thenReturn(Optional.empty());
        String viewName = booksControl.getBooksWithoutAuthor(1L, model);
        verify(model).addAttribute("error", "Book not found.");
        assertEquals("book-info", viewName);
    }

    @Test
    public void testGetAuthorAll() {
        when(booksRep.findById(1L)).thenReturn(Optional.of(book));
        String viewName = booksControl.getAuthorAll(1L, model);
        verify(model).addAttribute("book", book);
        verify(model).addAttribute("author", book.getAuthor());
        assertEquals("full_info_book", viewName);
    }

    @Test
    public void testGetAuthorAll_WhenBookNotFound() {
        when(booksRep.findById(1L)).thenReturn(Optional.empty());
        String viewName = booksControl.getAuthorAll(1L, model);
        assertEquals("redirect:/books", viewName);
    }
}
