package com.example.docsachln.repositories;

import android.content.Context;
import com.example.docsachln.api.CategoryService;
import okhttp3.Callback;

public class CategoryRepository {
    private final CategoryService categoryService;

    public CategoryRepository(Context context) {
        this.categoryService = new CategoryService(context);
    }

    public void getAllCategories(Callback callback) {
        categoryService.getAllCategories(callback);
    }

    public void getCategoryById(String categoryId, Callback callback) {
        categoryService.getCategoryById(categoryId, callback);
    }
}