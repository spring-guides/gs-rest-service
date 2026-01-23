package com.example.restservice.books.controllers;

import com.example.restservice.books.models.Book;
import com.example.restservice.books.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String home() {
        return "Welcome to the Book API!";
    }

    @GetMapping("/findbyid/{id}")
    public Book findBookById(@PathVariable int id) {
        return bookService.findBookById(id);
    }

    @GetMapping("/findall")
    public List<Book> findAllBooks() {
        return bookService.findAllBooks();
    }

    @DeleteMapping("/delete")
    public String deleteAllBooks() {
        bookService.deleteAllBooks();
        return "All books have been deleted.";
    }
}