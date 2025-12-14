package com.example.docsachln.repositories;

import android.content.Context;
import com.example.docsachln.api.BookService;
import okhttp3.Callback;

public class BookRepository {
    private final BookService bookService;

    public BookRepository(Context context) {
        this.bookService = new BookService(context);
    }

    public void getBooks(int offset, int limit, Callback callback) {
        bookService.getBooks(offset, limit, callback);
    }

    public void getBooksByCategory(String categoryId, int offset, int limit, Callback callback) {
        bookService.getBooksByCategory(categoryId, offset, limit, callback);
    }

    public void searchBooks(String query, Callback callback) {
        bookService.searchBooks(query, callback);
    }

    public void getBookById(String bookId, Callback callback) {
        bookService.getBookById(bookId, callback);
    }

    public void createBook(String title, String author, String description, String coverImageUrl, String categoryId, String userId, Callback callback) {
        bookService.createBook(title, author, description, coverImageUrl, categoryId, userId, callback);
    }

    public void updateBook(String bookId, String title, String author, String description, String coverImageUrl, String categoryId, Callback callback) {
        bookService.updateBook(bookId, title, author, description, coverImageUrl, categoryId, callback);
    }

    public void deleteBook(String bookId, Callback callback) {
        bookService.deleteBook(bookId, callback);
    }

    public void getPopularBooks(int limit, Callback callback) {
        bookService.getPopularBooks(limit, callback);
    }

    public void getUserBooks(String userId, Callback callback) {
        bookService.getUserBooks(userId, callback);
    }
}