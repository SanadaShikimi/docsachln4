package com.example.docsachln.repositories;

import android.content.Context;
import com.example.docsachln.api.ProfileService;
import okhttp3.Callback;

public class ProfileRepository {
    private final ProfileService profileService;

    public ProfileRepository(Context context) {
        this.profileService = new ProfileService(context);
    }

    public void getProfile(String userId, Callback callback) {
        profileService.getProfile(userId, callback);
    }

    public void updateProfile(String userId, String username, String fullName, String avatarUrl, Callback callback) {
        profileService.updateProfile(userId, username, fullName, avatarUrl, callback);
    }
}