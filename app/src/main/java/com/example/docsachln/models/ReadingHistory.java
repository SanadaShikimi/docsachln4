package com.example.docsachln.models;

public class ReadingHistory {
    private String id;
    private String userId;
    private String bookId;
    private String chapterId;
    private int lastPosition;
    private String lastReadAt;

    // Additional fields from joins
    private Book book;
    private Chapter chapter;

    public ReadingHistory() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getChapterId() { return chapterId; }
    public void setChapterId(String chapterId) { this.chapterId = chapterId; }

    public int getLastPosition() { return lastPosition; }
    public void setLastPosition(int lastPosition) { this.lastPosition = lastPosition; }

    public String getLastReadAt() { return lastReadAt; }
    public void setLastReadAt(String lastReadAt) { this.lastReadAt = lastReadAt; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }

    public Chapter getChapter() { return chapter; }
    public void setChapter(Chapter chapter) { this.chapter = chapter; }
}