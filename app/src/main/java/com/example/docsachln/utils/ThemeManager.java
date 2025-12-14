package com.example.docsachln.utils;

import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    private static ThemeManager instance;
    private final PreferenceManager preferenceManager;

    private ThemeManager(Context context) {
        preferenceManager = PreferenceManager.getInstance(context);
    }

    public static synchronized ThemeManager getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeManager(context.getApplicationContext());
        }
        return instance;
    }

    public void applyTheme() {
        int themeMode = preferenceManager.getInt(Constants.KEY_THEME_MODE, Constants.THEME_SYSTEM);
        switch (themeMode) {
            case Constants.THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Constants.THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case Constants.THEME_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    public void setThemeMode(int mode) {
        preferenceManager.saveInt(Constants.KEY_THEME_MODE, mode);
        applyTheme();
    }

    public int getThemeMode() {
        return preferenceManager.getInt(Constants.KEY_THEME_MODE, Constants.THEME_SYSTEM);
    }

    public boolean isDarkMode(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES;
    }
}