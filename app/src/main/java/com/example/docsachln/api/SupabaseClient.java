package com.example.docsachln.api;

import android.content.Context;
import com.example.docsachln.utils.Constants;
import com.example.docsachln.utils.PreferenceManager;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SupabaseClient {
    private static SupabaseClient instance;
    private final OkHttpClient client;
    private final String baseUrl;
    private final String anonKey;
    private PreferenceManager preferenceManager;

    private SupabaseClient(Context context) {
        this.baseUrl = Constants.SUPABASE_URL;
        this.anonKey = Constants.SUPABASE_ANON_KEY;
        this.preferenceManager = PreferenceManager.getInstance(context);

        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor())
                .build();
    }

    public static synchronized SupabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new SupabaseClient(context.getApplicationContext());
        }
        return instance;
    }

    // GET request
    public void get(String endpoint, Callback callback) {
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }

    // POST request
    public void post(String endpoint, JSONObject body, Callback callback) {
        RequestBody requestBody = RequestBody.create(
                body.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    // PATCH request
    public void patch(String endpoint, JSONObject body, Callback callback) {
        RequestBody requestBody = RequestBody.create(
                body.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .patch(requestBody)
                .build();

        client.newCall(request).enqueue(callback);
    }

    // DELETE request
    public void delete(String endpoint, Callback callback) {
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .delete()
                .build();

        client.newCall(request).enqueue(callback);
    }

    // Auth interceptor to add headers
    private class AuthInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .header("apikey", anonKey)
                    .header("Content-Type", "application/json");

            String accessToken = preferenceManager.getAccessToken();
            if (accessToken != null && !accessToken.isEmpty()) {
                builder.header("Authorization", "Bearer " + accessToken);
            }

            return chain.proceed(builder.build());
        }
    }
}