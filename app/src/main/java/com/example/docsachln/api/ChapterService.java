package com.example.docsachln.api;

import android.content.Context;
import okhttp3.Callback;
import org.json.JSONException;
import org.json.JSONObject;

public class ChapterService {
    private final SupabaseClient supabaseClient;

    public ChapterService(Context context) {
        // Khởi tạo client 1 lần duy nhất
        this.supabaseClient = SupabaseClient.getInstance(context);
    }

    // Get chapters by book ID
    public void getChaptersByBookId(String bookId, Callback callback) {
        String endpoint = "/rest/v1/chapters?select=*&book_id=eq." + bookId + "&order=chapter_number.asc";
        supabaseClient.get(endpoint, callback);
    }

    // Get chapter by ID
    public void getChapterById(String chapterId, Callback callback) {
        String endpoint = "/rest/v1/chapters?select=*&id=eq." + chapterId;
        supabaseClient.get(endpoint, callback);
    }

    // Create chapter
    public void createChapter(String bookId, String title, int chapterNumber, String content, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("book_id", bookId);
            body.put("title", title);
            body.put("chapter_number", chapterNumber);
            body.put("content", content);

            supabaseClient.post("/rest/v1/chapters", body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Update chapter
    public void updateChapter(String chapterId, String title, String content, Callback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("title", title);
            body.put("content", content);

            String endpoint = "/rest/v1/chapters?id=eq." + chapterId;
            supabaseClient.patch(endpoint, body, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Delete chapter
    public void deleteChapter(String chapterId, Callback callback) {
        String endpoint = "/rest/v1/chapters?id=eq." + chapterId;
        supabaseClient.delete(endpoint, callback);
    }

    // ✅ ĐÃ SỬA: Lấy chương theo số thứ tự (cho nút Next/Prev)
    public void getChapterByNumber(String bookId, int chapterNumber, Callback callback) {
        // 1. Thêm /rest/v1/ cho đồng bộ
        // 2. Thêm select=* để đảm bảo lấy về đủ dữ liệu
        String endpoint = "/rest/v1/chapters?select=*&book_id=eq." + bookId + "&chapter_number=eq." + chapterNumber;

        // Sử dụng biến instance 'supabaseClient' đã khởi tạo ở trên
        this.supabaseClient.get(endpoint, callback);
    }
}