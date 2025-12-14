package com.example.docsachln.repositories;

import android.content.Context;
import com.example.docsachln.api.FavoriteService;
import okhttp3.Callback;

public class FavoriteRepository {
    private final FavoriteService favoriteService;

    public FavoriteRepository(Context context) {
        this.favoriteService = new FavoriteService(context);
    }

    public void getUserFavorites(String userId, Callback callback) {
        favoriteService.getUserFavorites(userId, callback);
    }

    public void isFavorited(String userId, String bookId, Callback callback) {
        favoriteService.isFavorited(userId, bookId, callback);
    }

    public void addFavorite(String userId, String bookId, Callback callback) {
        favoriteService.addFavorite(userId, bookId, callback);
    }

    public void removeFavorite(String userId, String bookId, Callback callback) {
        favoriteService.removeFavorite(userId, bookId, callback);
    }
}