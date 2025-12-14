package com.example.docsachln.api;

import android.content.Context;
import okhttp3.Callback;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileService {
    private final SupabaseClient supabaseClient;

    public ProfileService(Context context) {
        this.supabaseClient = SupabaseClient.getInstance(context);
    }

    // Get profile by user ID
    public void getProfile(String userId, Callback callback) {
        String endpoint = "/rest/v1/profiles?select=*&id=eq." + userId;
        supabaseClient.get(endpoint, callback);
    }

    // Update profile
    public void updateProfile(String userId, String username, String fullName, String avatarUrl, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            if (username != null) body.put("username", username);
            if (fullName != null) body.put("full_name", fullName);
            if (avatarUrl != null) body.put("avatar_url", avatarUrl);

            String endpoint = "/rest/v1/profiles?id=eq." + userId;
            supabaseClient.patch(endpoint, body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}