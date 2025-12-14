package com.example.docsachln.models;

public class BookStatistics {
    private String id;
    private String bookId;
    private int totalViews;
    private int totalFavorites;
    private int totalComments;
    private float averageRating;
    private int totalRatings;
    private String updatedAt;

    public BookStatistics() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public int getTotalViews() { return totalViews; }
    public void setTotalViews(int totalViews) { this.totalViews = totalViews; }

    public int getTotalFavorites() { return totalFavorites; }
    public void setTotalFavorites(int totalFavorites) { this.totalFavorites = totalFavorites; }

    public int getTotalComments() { return totalComments; }
    public void setTotalComments(int totalComments) { this.totalComments = totalComments; }

    public float getAverageRating() { return averageRating; }
    public void setAverageRating(float averageRating) { this.averageRating = averageRating; }

    public int getTotalRatings() { return totalRatings; }
    public void setTotalRatings(int totalRatings) { this.totalRatings = totalRatings; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}