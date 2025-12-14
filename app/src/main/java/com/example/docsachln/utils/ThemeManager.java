package com.example.docsachln.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    // --- 1. KHAI BÁO CÁC HẰNG SỐ (Để ProfileFragment gọi được) ---
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_SYSTEM = 2;
    // -------------------------------------------------------------

    private static final String PREF_NAME = "DocSach_Theme";
    private static final String KEY_THEME_MODE = "theme_mode";

    private static ThemeManager instance;
    private final SharedPreferences sharedPreferences;

    private ThemeManager(Context context) {
        // Dùng SharedPreferences trực tiếp để tránh lỗi thiếu hàm bên PreferenceManager
        sharedPreferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized ThemeManager getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeManager(context);
        }
        return instance;
    }

    public int getThemeMode() {
        return sharedPreferences.getInt(KEY_THEME_MODE, THEME_SYSTEM);
    }

    public void setThemeMode(int mode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_THEME_MODE, mode);
        editor.apply();

        // Áp dụng ngay lập tức
        applyTheme(mode);
    }

    public void applyTheme(int mode) {
        switch (mode) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
}