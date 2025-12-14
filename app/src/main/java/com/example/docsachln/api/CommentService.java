package com.example.docsachln.api;

import android.content.Context;
import okhttp3.Callback;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentService {
    private final SupabaseClient supabaseClient;

    public CommentService(Context context) {
        this.supabaseClient = SupabaseClient.getInstance(context);
    }

    // Get comments by book ID
    public void getCommentsByBookId(String bookId, Callback callback) {
        String endpoint = "/rest/v1/comments?select=*,user_profile:profiles(*)&book_id=eq." + bookId + "&is_deleted=eq.false&order=created_at.desc";
        supabaseClient.get(endpoint, callback);
    }

    // Create comment
    public void createComment(String bookId, String userId, String content, String parentId, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("book_id", bookId);
            body.put("user_id", userId);
            body.put("content", content);
            if (parentId != null) {
                body.put("parent_id", parentId);
            }

            supabaseClient.post("/rest/v1/comments", body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Update comment
    public void updateComment(String commentId, String content, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("content", content);

            String endpoint = "/rest/v1/comments?id=eq." + commentId;
            supabaseClient.patch(endpoint, body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Soft delete comment
    public void deleteComment(String commentId, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("is_deleted", true);

            String endpoint = "/rest/v1/comments?id=eq." + commentId;
            supabaseClient.patch(endpoint, body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}