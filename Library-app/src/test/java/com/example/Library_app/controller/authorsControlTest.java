package com.example.Library_app.controller;

import com.example.Library_app.model.Author;
import com.example.Library_app.repository.AuthorsRep;
import com.example.Library_app.repository.BooksRep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class authorsControlTest {

    @InjectMocks
    private authorsControl authorsControl;

    @Mock
    private AuthorsRep authorsRep;

    @Mock
    private BooksRep booksRep;

    @Mock
    private Model model;

    private Author existingAuthor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingAuthor = new Author();
        existingAuthor.setId(1L);
        existingAuthor.setName("John");
        existingAuthor.setLastname("Snow");
        existingAuthor.setDateOfBirth(LocalDate.of(1980, 5, 15));
        existingAuthor.setNationality("American");
        existingAuthor.setAuthor(null);
        when(authorsRep.findById(1L)).thenReturn(Optional.of(existingAuthor));
        when(booksRep.findByAuthorId(1L)).thenReturn(new ArrayList<>());
    }

    @Test
    public void testGetAuthors() {
        when(authorsRep.findAll()).thenReturn(Collections.emptyList());
        String viewName = authorsControl.getAuthors(model);
        verify(model).addAttribute("authors", Collections.emptyList());
        assertEquals("authors", viewName);
    }

    @Test
    public void testShowAddAuthorForm() throws Exception {
        String viewName = authorsControl.showAddAuthorForm(model);
        ArgumentCaptor<Author> authorCaptor = ArgumentCaptor.forClass(Author.class);
        verify(model).addAttribute(eq("author"), authorCaptor.capture());
        Author capturedAuthor = authorCaptor.getValue();
        assertEquals(new Author(), capturedAuthor);
        assertEquals("add-author", viewName);
    }

    @Test
    public void testAddAuthor() throws Exception {
        Author newAuthor = new Author();
        newAuthor.setName("Test");
        newAuthor.setLastname("Author");
        String viewName = authorsControl.addAuthor(newAuthor);
        verify(authorsRep).save(newAuthor);
        assertEquals("redirect:/authors", viewName);
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        Long authorId = 1L;
        when(authorsRep.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        String viewName = authorsControl.deleteAuthor(authorId);
        verify(authorsRep).deleteById(authorId);
        assertEquals("redirect:/authors", viewName);
    }

    @Test
    public void testDeleteAuthor_NonExistingAuthor() throws Exception {
        Long authorId = 1L;
        when(authorsRep.findById(authorId)).thenReturn(Optional.empty());

        String viewName = authorsControl.deleteAuthor(authorId);

        verify(authorsRep, never()).deleteById(authorId);
        assertEquals("redirect:/authors", viewName);
    }

    @Test
    public void testShowEditAuthorForm() throws Exception {
        Long authorId = 1L;
        when(authorsRep.findById(authorId)).thenReturn(Optional.of(existingAuthor));

        String viewName = authorsControl.showEditAuthorForm(authorId, model);

        verify(model).addAttribute("author", existingAuthor);
        assertEquals("edit-author", viewName);
    }

    @Test
    public void testEditAuthor() throws Exception {
        Long authorId = 1L;
        Author updatedAuthor = new Author();
        updatedAuthor.setId(authorId);
        updatedAuthor.setName("Updated");
        updatedAuthor.setLastname("Author");

        when(authorsRep.existsById(authorId)).thenReturn(true);

        String viewName = authorsControl.editAuthor(authorId, updatedAuthor);

        verify(authorsRep).save(updatedAuthor);
        assertEquals("redirect:/authors", viewName);
    }

    @Test
    public void testGetAuthorBooks() throws Exception {
        Long authorId = 1L;
        when(authorsRep.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        String viewName = authorsControl.getAuthorAll(authorId, model);
        verify(model).addAttribute("author", existingAuthor);
        verify(model).addAttribute("books", new ArrayList<>());
        assertEquals("full_info_author", viewName);
    }
}
