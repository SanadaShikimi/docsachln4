package com.example.docsachln.models;

public class Rating {
    private String id;
    private String bookId;
    private String userId;
    private int rating;
    private String review;
    private String createdAt;
    private String updatedAt;

    // Additional field from join
    private Profile userProfile;

    public Rating() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public Profile getUserProfile() { return userProfile; }
    public void setUserProfile(Profile userProfile) { this.userProfile = userProfile; }
}