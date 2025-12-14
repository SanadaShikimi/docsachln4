package com.example.docsachln.api;

import android.content.Context;
import okhttp3.Callback;
import org.json.JSONException;
import org.json.JSONObject;

public class BookService {
    private final SupabaseClient supabaseClient;

    public BookService(Context context) {
        this.supabaseClient = SupabaseClient.getInstance(context);
    }

    // Get all books with pagination
    public void getBooks(int offset, int limit, Callback callback) {
        String endpoint = "/rest/v1/books?select=*,category:categories(*),user_profile:profiles(*)&is_deleted=eq.false&order=created_at.desc&offset=" + offset + "&limit=" + limit;
        supabaseClient.get(endpoint, callback);
    }

    // Get books by category
    public void getBooksByCategory(String categoryId, int offset, int limit, Callback callback) {
        String endpoint = "/rest/v1/books?select=*,category:categories(*),user_profile:profiles(*)&category_id=eq." + categoryId + "&is_deleted=eq.false&order=created_at.desc&offset=" + offset + "&limit=" + limit;
        supabaseClient.get(endpoint, callback);
    }

    // Search books
    public void searchBooks(String query, Callback callback) {
        String endpoint = "/rest/v1/books?select=*,category:categories(*),user_profile:profiles(*)&is_deleted=eq.false&or=(title.ilike.*" + query + "*,author.ilike.*" + query + "*)&order=created_at.desc";
        supabaseClient.get(endpoint, callback);
    }

    // Get book by ID
    public void getBookById(String bookId, Callback callback) {
        String endpoint = "/rest/v1/books?select=*,category:categories(*),user_profile:profiles(*)&id=eq." + bookId;
        supabaseClient.get(endpoint, callback);
    }

    // Create book
    public void createBook(String title, String author, String description, String coverImageUrl, String categoryId, String userId, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("title", title);
            body.put("author", author);
            body.put("description", description);
            body.put("cover_image_url", coverImageUrl);
            body.put("category_id", categoryId);
            body.put("user_id", userId);

            supabaseClient.post("/rest/v1/books", body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Update book
    public void updateBook(String bookId, String title, String author, String description, String coverImageUrl, String categoryId, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("title", title);
            body.put("author", author);
            body.put("description", description);
            body.put("cover_image_url", coverImageUrl);
            body.put("category_id", categoryId);

            String endpoint = "/rest/v1/books?id=eq." + bookId;
            supabaseClient.patch(endpoint, body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Soft delete book
    public void deleteBook(String bookId, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("is_deleted", true);

            String endpoint = "/rest/v1/books?id=eq." + bookId;
            supabaseClient.patch(endpoint, body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Get popular books (most views)
    public void getPopularBooks(int limit, Callback callback) {
        String endpoint = "/rest/v1/books?select=*,category:categories(*),user_profile:profiles(*)&is_deleted=eq.false&order=view_count.desc&limit=" + limit;
        supabaseClient.get(endpoint, callback);
    }

    // Get user's books
    public void getUserBooks(String userId, Callback callback) {
        String endpoint = "/rest/v1/books?select=*,category:categories(*)&user_id=eq." + userId + "&is_deleted=eq.false&order=created_at.desc";
        supabaseClient.get(endpoint, callback);
    }
}