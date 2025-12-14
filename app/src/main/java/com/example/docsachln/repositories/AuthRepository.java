package com.example.docsachln.repositories;

import android.content.Context;
import com.example.docsachln.api.AuthService;
import okhttp3.Callback;

public class AuthRepository {
    private final AuthService authService;

    public AuthRepository(Context context) {
        this.authService = new AuthService(context);
    }

    public void signUp(String email, String password, String username, Callback callback) {
        authService.signUp(email, password, username, callback);
    }

    public void signIn(String email, String password, Callback callback) {
        authService.signIn(email, password, callback);
    }

    public void signOut(Callback callback) {
        authService.signOut(callback);
    }

    public void forgotPassword(String email, Callback callback) {
        authService.forgotPassword(email, callback);
    }

    public void resetPassword(String newPassword, Callback callback) {
        authService.resetPassword(newPassword, callback);
    }

    public void getCurrentUser(Callback callback) {
        authService.getCurrentUser(callback);
    }

    public void signInWithGoogle(String idToken, Callback callback) {
        authService.signInWithGoogle(idToken, callback);
    }
}