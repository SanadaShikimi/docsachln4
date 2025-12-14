package com.example.docsachln.api;

import android.content.Context;
import com.example.docsachln.utils.PreferenceManager;
import okhttp3.Callback;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthService {
    private final SupabaseClient supabaseClient;
    private final PreferenceManager preferenceManager;

    public AuthService(Context context) {
        this.supabaseClient = SupabaseClient.getInstance(context);
        this.preferenceManager = PreferenceManager.getInstance(context);
    }

    // Sign up with email/password
    public void signUp(String email, String password, String username, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("password", password);

            JSONObject metadata = new JSONObject();
            metadata.put("username", username);
            body.put("data", metadata);

            supabaseClient.post("/auth/v1/signup", body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Sign in with email/password
    public void signIn(String email, String password, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("email", email);
            body.put("password", password);

            supabaseClient.post("/auth/v1/token?grant_type=password", body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Sign out
    public void signOut(Callback callback) {
        supabaseClient.post("/auth/v1/logout", new JSONObject(), callback);
        preferenceManager.clearUserSession();
    }

    // Forgot password
    public void forgotPassword(String email, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("email", email);

            supabaseClient.post("/auth/v1/recover", body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Reset password
    public void resetPassword(String newPassword, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("password", newPassword);

            supabaseClient.patch("/auth/v1/user", body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Get current user
    public void getCurrentUser(Callback callback) {
        supabaseClient.get("/auth/v1/user", callback);
    }
    // signin wwith google
    public void signInWithGoogle(String idToken, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("provider", "google");
            body.put("id_token", idToken);

            // Endpoint để exchange Google ID Token lấy Supabase Session
            supabaseClient.post("/auth/v1/token?grant_type=id_token", body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}