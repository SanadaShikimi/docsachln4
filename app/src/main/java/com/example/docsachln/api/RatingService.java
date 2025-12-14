package com.example.docsachln.api;

import android.content.Context;
import okhttp3.Callback;
import org.json.JSONException;
import org.json.JSONObject;

public class RatingService {
    private final SupabaseClient supabaseClient;

    public RatingService(Context context) {
        this.supabaseClient = SupabaseClient.getInstance(context);
    }

    // Get ratings by book ID
    public void getRatingsByBookId(String bookId, Callback callback) {
        String endpoint = "/rest/v1/ratings?select=*,user_profile:profiles(*)&book_id=eq." + bookId + "&order=created_at.desc";
        supabaseClient.get(endpoint, callback);
    }

    // Get user's rating for a book
    public void getUserRating(String userId, String bookId, Callback callback) {
        String endpoint = "/rest/v1/ratings?select=*&user_id=eq." + userId + "&book_id=eq." + bookId;
        supabaseClient.get(endpoint, callback);
    }

    // Create or update rating
    public void upsertRating(String userId, String bookId, int rating, String review, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("user_id", userId);
            body.put("book_id", bookId);
            body.put("rating", rating);
            body.put("review", review);

            // Supabase upsert
            supabaseClient.post("/rest/v1/ratings?on_conflict=user_id,book_id", body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Delete rating
    public void deleteRating(String userId, String bookId, Callback callback) {
        String endpoint = "/rest/v1/ratings?user_id=eq." + userId + "&book_id=eq." + bookId;
        supabaseClient.delete(endpoint, callback);
    }
}