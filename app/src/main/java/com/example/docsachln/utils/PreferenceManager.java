package com.example.docsachln.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.Set;

public class PreferenceManager {
    private static PreferenceManager instance;
    private final SharedPreferences preferences;

    // Keys mới
    private static final String KEY_READ_CHAPTERS = "key_read_chapters";
    private static final String KEY_BOOKMARK_PREFIX = "bookmark_book_";

    private PreferenceManager(Context context) {
        preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context.getApplicationContext());
        }
        return instance;
    }

    // ... (Giữ nguyên các hàm cũ: saveString, getString, saveBoolean, user session...) ...
    public void saveString(String key, String value) { preferences.edit().putString(key, value).apply(); }
    public String getString(String key, String defaultValue) { return preferences.getString(key, defaultValue); }
    public void saveBoolean(String key, boolean value) { preferences.edit().putBoolean(key, value).apply(); }
    public boolean getBoolean(String key, boolean defaultValue) { return preferences.getBoolean(key, defaultValue); }
    public void saveInt(String key, int value) { preferences.edit().putInt(key, value).apply(); }
    public int getInt(String key, int defaultValue) { return preferences.getInt(key, defaultValue); }
    public void clear() { preferences.edit().clear().apply(); }
    public void saveUserSession(String userId, String accessToken, String refreshToken) {
        preferences.edit().putString(Constants.KEY_USER_ID, userId).putString(Constants.KEY_ACCESS_TOKEN, accessToken).putString(Constants.KEY_REFRESH_TOKEN, refreshToken).putBoolean(Constants.KEY_IS_LOGGED_IN, true).apply();
    }
    public void clearUserSession() {
        preferences.edit().remove(Constants.KEY_USER_ID).remove(Constants.KEY_ACCESS_TOKEN).remove(Constants.KEY_REFRESH_TOKEN).putBoolean(Constants.KEY_IS_LOGGED_IN, false).apply();
    }
    public boolean isLoggedIn() { return preferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false); }
    public String getUserId() { return preferences.getString(Constants.KEY_USER_ID, null); }
    public String getAccessToken() { return preferences.getString(Constants.KEY_ACCESS_TOKEN, null); }

    // --- PHẦN MỚI THÊM VÀO ---

    // 1. Đánh dấu chương đã đọc (Để tô màu xám)
    public void markChapterAsRead(String chapterId) {
        Set<String> readChapters = getReadChapters();
        readChapters.add(chapterId);
        preferences.edit().putStringSet(KEY_READ_CHAPTERS, readChapters).apply();
    }

    public boolean isChapterRead(String chapterId) {
        Set<String> readChapters = getReadChapters();
        return readChapters.contains(chapterId);
    }

    private Set<String> getReadChapters() {
        // Phải tạo new HashSet để tránh lỗi reference khi sửa đổi Set trả về từ SharedPreferences
        return new HashSet<>(preferences.getStringSet(KEY_READ_CHAPTERS, new HashSet<>()));
    }

    // 2. Bookmark (Lưu dấu trang cho từng sách)
    // Lưu chapterId đang đọc cho bookId tương ứng
    public void saveBookmark(String bookId, String chapterId) {
        preferences.edit().putString(KEY_BOOKMARK_PREFIX + bookId, chapterId).apply();
    }

    // Lấy chapterId đã bookmark của sách
    public String getBookmark(String bookId) {
        return preferences.getString(KEY_BOOKMARK_PREFIX + bookId, null);
    }

    // Xóa bookmark (nếu cần)
    public void removeBookmark(String bookId) {
        preferences.edit().remove(KEY_BOOKMARK_PREFIX + bookId).apply();
    }
}