package com.example.docsachln.repositories;

import android.content.Context;
import com.example.docsachln.api.RatingService;
import okhttp3.Callback;

public class RatingRepository {
    private final RatingService ratingService;

    public RatingRepository(Context context) {
        this.ratingService = new RatingService(context);
    }

    public void getRatingsByBookId(String bookId, Callback callback) {
        ratingService.getRatingsByBookId(bookId, callback);
    }

    public void getUserRating(String userId, String bookId, Callback callback) {
        ratingService.getUserRating(userId, bookId, callback);
    }

    public void upsertRating(String userId, String bookId, int rating, String review, Callback callback) {
        ratingService.upsertRating(userId, bookId, rating, review, callback);
    }

    public void deleteRating(String userId, String bookId, Callback callback) {
        ratingService.deleteRating(userId, bookId, callback);
    }
}