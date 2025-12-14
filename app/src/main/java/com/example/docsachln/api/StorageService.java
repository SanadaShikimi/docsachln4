package com.example.docsachln.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import okhttp3.*;

public class StorageService {
    private final Context context;
    private final String baseUrl;
    private final String anonKey;

    public StorageService(Context context, String baseUrl, String anonKey) {
        this.context = context;
        this.baseUrl = baseUrl;
        this.anonKey = anonKey;
    }

    // Upload avatar
    public void uploadAvatar(String userId, Uri imageUri, Callback callback) {
        String fileName = userId + "_" + System.currentTimeMillis() + ".jpg";
        uploadToStorage("avatars", userId + "/" + fileName, imageUri, callback);
    }

    // Upload book cover
    public void uploadBookCover(String bookId, Uri imageUri, Callback callback) {
        String fileName = bookId + "_" + System.currentTimeMillis() + ".jpg";
        uploadToStorage("book-covers", bookId + "/" + fileName, imageUri, callback);
    }

    // Generic upload method
    private void uploadToStorage(String bucket, String path, Uri imageUri, Callback callback) {
        try {
            // Compress image
            Bitmap bitmap = BitmapFactory.decodeStream(
                    context.getContentResolver().openInputStream(imageUri)
            );

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] imageBytes = baos.toByteArray();

            // Create request
            RequestBody requestBody = RequestBody.create(imageBytes, MediaType.parse("image/jpeg"));

            Request request = new Request.Builder()
                    .url(baseUrl + "/storage/v1/object/" + bucket + "/" + path)
                    .post(requestBody)
                    .header("apikey", anonKey)
                    .header("Authorization", "Bearer " + anonKey)
                    .header("Content-Type", "image/jpeg")
                    .build();

            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(callback);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get public URL
    public String getPublicUrl(String bucket, String path) {
        return baseUrl + "/storage/v1/object/public/" + bucket + "/" + path;
    }
}