package com.example.docsachln.utils;

import com.example.docsachln.BuildConfig;

public class Constants {
    // Supabase
    public static final String SUPABASE_URL = BuildConfig.SUPABASE_URL;
    public static final String SUPABASE_ANON_KEY = BuildConfig.SUPABASE_ANON_KEY;

    // SharedPreferences
    public static final String PREF_NAME = "DocSachLN_Prefs";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_IS_ADMIN = "is_admin";
    public static final String KEY_THEME_MODE = "theme_mode";

    // Theme
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_SYSTEM = 2;

    // Reading settings defaults
    public static final String DEFAULT_FONT_FAMILY = "default";
    public static final int DEFAULT_FONT_SIZE = 16;
    public static final float DEFAULT_LINE_HEIGHT = 1.5f;
    public static final String DEFAULT_TEXT_ALIGN = "left";

    // Image
    public static final int MAX_IMAGE_SIZE = 2048;
    public static final int IMAGE_QUALITY = 80;

    // Pagination
    public static final int PAGE_SIZE = 20;

    // Request codes
    public static final int RC_SIGN_IN = 9001;
    public static final int RC_PICK_IMAGE = 9002;

    // Supabase tables
    public static final String TABLE_PROFILES = "profiles";
    public static final String TABLE_BOOKS = "books";
    public static final String TABLE_CHAPTERS = "chapters";
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_TAGS = "tags";
    public static final String TABLE_RATINGS = "ratings";
    public static final String TABLE_COMMENTS = "comments";
    public static final String TABLE_FAVORITES = "favorites";
    public static final String TABLE_READING_HISTORY = "reading_history";
    public static final String TABLE_NOTEBOOKS = "notebooks";
    public static final String TABLE_READING_SETTINGS = "reading_settings";
    public static final String TABLE_BOOK_STATISTICS = "book_statistics";

    // Storage buckets
    public static final String BUCKET_AVATARS = "avatars";
    public static final String BUCKET_BOOK_COVERS = "book-covers";
}