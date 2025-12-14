package com.example.docsachln.api;

import android.content.Context;
import okhttp3.Callback;

public class CategoryService {
    private final SupabaseClient supabaseClient;

    public CategoryService(Context context) {
        this.supabaseClient = SupabaseClient.getInstance(context);
    }

    // Get all categories
    public void getAllCategories(Callback callback) {
        String endpoint = "/rest/v1/categories?select=*&order=name.asc";
        supabaseClient.get(endpoint, callback);
    }

    // Get category by ID
    public void getCategoryById(String categoryId, Callback callback) {
        String endpoint = "/rest/v1/categories?select=*&id=eq." + categoryId;
        supabaseClient.get(endpoint, callback);
    }
}