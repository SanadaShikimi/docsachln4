package com.example.docsachln.ui.auth;

import android.app.Activity;
import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleAuthHelper {
    // Thay thế bằng WEB CLIENT ID của bạn từ Google Cloud Console
    // (Lấy từ file google-services.json hoặc Console -> Credentials -> OAuth 2.0 Client IDs -> Web client)
    private static final String WEB_CLIENT_ID = "459073457942-m9kr194gr3p4bit0momgg1r8tqdoaouc.apps.googleusercontent.com";

    private final GoogleSignInClient googleSignInClient;

    public interface GoogleAuthCallback {
        void onSuccess(String idToken);
        void onError(String error);
    }

    public GoogleAuthHelper(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID) // Quan trọng: Phải dùng Web Client ID
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    public void signIn(ActivityResultLauncher<Intent> launcher) {
        // Đăng xuất trước để user có thể chọn tài khoản khác nếu muốn
        googleSignInClient.signOut().addOnCompleteListener(task -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            launcher.launch(signInIntent);
        });
    }

    public void handleSignInResult(Intent data, GoogleAuthCallback callback) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            String idToken = account.getIdToken();

            if (idToken != null) {
                callback.onSuccess(idToken);
            } else {
                callback.onError("ID token is null");
            }
        } catch (ApiException e) {
            callback.onError("Google sign-in failed: " + e.getMessage());
        }
    }
}