package com.example.docsachln.api;

import android.content.Context;
import okhttp3.Callback;
import org.json.JSONException;
import org.json.JSONObject;

public class ReadingHistoryService {
    private final SupabaseClient supabaseClient;

    public ReadingHistoryService(Context context) {
        this.supabaseClient = SupabaseClient.getInstance(context);
    }

    // Get user's reading history
    public void getUserReadingHistory(String userId, Callback callback) {
        String endpoint = "/rest/v1/reading_history?select=*,book:books(*),chapter:chapters(*)&user_id=eq." + userId + "&order=last_read_at.desc";
        supabaseClient.get(endpoint, callback);
    }

    // Update reading progress
    public void updateReadingProgress(String userId, String bookId, String chapterId, int lastPosition, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("user_id", userId);
            body.put("book_id", bookId);
            body.put("chapter_id", chapterId);
            body.put("last_position", lastPosition);

            // Upsert
            supabaseClient.post("/rest/v1/reading_history?on_conflict=user_id,book_id", body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Delete reading history entry
    public void deleteReadingHistory(String userId, String bookId, Callback callback) {
        String endpoint = "/rest/v1/reading_history?user_id=eq." + userId + "&book_id=eq." + bookId;
        supabaseClient.delete(endpoint, callback);
    }
}