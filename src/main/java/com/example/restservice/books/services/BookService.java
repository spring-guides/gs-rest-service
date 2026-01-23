package com.example.restservice.books.services;

import com.example.restservice.books.models.Book;

import java.util.List;

public interface BookService {

    List<Book> findAllBooks();

    Book findBookById(int id);

    void deleteAllBooks();

}
