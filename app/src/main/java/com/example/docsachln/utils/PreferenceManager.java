package com.example.docsachln.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static PreferenceManager instance;
    private final SharedPreferences preferences;

    private PreferenceManager(Context context) {
        preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public void saveBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public void saveInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    // User session
    public void saveUserSession(String userId, String accessToken, String refreshToken) {
        preferences.edit()
                .putString(Constants.KEY_USER_ID, userId)
                .putString(Constants.KEY_ACCESS_TOKEN, accessToken)
                .putString(Constants.KEY_REFRESH_TOKEN, refreshToken)
                .putBoolean(Constants.KEY_IS_LOGGED_IN, true)
                .apply();
    }

    public void clearUserSession() {
        preferences.edit()
                .remove(Constants.KEY_USER_ID)
                .remove(Constants.KEY_ACCESS_TOKEN)
                .remove(Constants.KEY_REFRESH_TOKEN)
                .putBoolean(Constants.KEY_IS_LOGGED_IN, false)
                .apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    public String getUserId() {
        return preferences.getString(Constants.KEY_USER_ID, null);
    }

    public String getAccessToken() {
        return preferences.getString(Constants.KEY_ACCESS_TOKEN, null);
    }

}