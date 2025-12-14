package com.example.docsachln.api;

import android.content.Context;
import okhttp3.Callback;
import org.json.JSONException;
import org.json.JSONObject;

public class FavoriteService {
    private final SupabaseClient supabaseClient;

    public FavoriteService(Context context) {
        this.supabaseClient = SupabaseClient.getInstance(context);
    }

    // Get user's favorites
    public void getUserFavorites(String userId, Callback callback) {
        String endpoint = "/rest/v1/favorites?select=*,book:books(*)&user_id=eq." + userId + "&order=created_at.desc";
        supabaseClient.get(endpoint, callback);
    }

    // Check if book is favorited
    public void isFavorited(String userId, String bookId, Callback callback) {
        String endpoint = "/rest/v1/favorites?select=*&user_id=eq." + userId + "&book_id=eq." + bookId;
        supabaseClient.get(endpoint, callback);
    }

    // Add to favorites
    public void addFavorite(String userId, String bookId, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("user_id", userId);
            body.put("book_id", bookId);

            supabaseClient.post("/rest/v1/favorites", body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Remove from favorites
    public void removeFavorite(String userId, String bookId, Callback callback) {
        String endpoint = "/rest/v1/favorites?user_id=eq." + userId + "&book_id=eq." + bookId;
        supabaseClient.delete(endpoint, callback);
    }
}