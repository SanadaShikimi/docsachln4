package com.example.docsachln.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // ✅ Đã thêm thư viện Log
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.docsachln.MainActivity;
import com.example.docsachln.R;
// Lưu ý: Kiểm tra lại package của MainActivity cho đúng với project của bạn
// Ví dụ: import com.example.docsachln.ui.main.MainActivity;
import com.example.docsachln.viewmodels.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogleSignIn;
    private TextView tvRegister;
    private ProgressBar progressBar;

    private GoogleAuthHelper googleAuthHelper;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Khởi tạo Google Auth Helper
        googleAuthHelper = new GoogleAuthHelper(this);

        initViews();
        setupObservers();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupObservers() {
        // Quan sát trạng thái Loading
        authViewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnLogin.setEnabled(!isLoading);
            btnGoogleSignIn.setEnabled(!isLoading);
            etEmail.setEnabled(!isLoading);
            etPassword.setEnabled(!isLoading);
        });

        // Quan sát lỗi từ ViewModel (Lỗi Supabase trả về)
        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Log.e("LOGIN_DEBUG", "Supabase Error: " + error); // ✅ Log lỗi Supabase
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        // Quan sát đăng nhập thành công
        authViewModel.getLoginSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                Log.d("LOGIN_DEBUG", "Login Success! Navigating to Main."); // ✅ Log thành công
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void setupListeners() {
        // Nút Đăng nhập thường
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            authViewModel.signIn(email, password);
        });

        // Chuyển sang màn hình Đăng ký
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // Xử lý kết quả Google Sign In
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        googleAuthHelper.handleSignInResult(data, new GoogleAuthHelper.GoogleAuthCallback() {
                            @Override
                            public void onSuccess(String idToken) {
                                // ✅ Log ID Token để kiểm tra xem đã lấy được từ Google chưa
                                Log.d("LOGIN_DEBUG", "Google ID Token received: " + idToken);

                                // Gửi token lên Supabase để xác thực
                                authViewModel.signInWithGoogle(idToken);
                            }

                            @Override
                            public void onError(String error) {
                                // 🔴 LOG QUAN TRỌNG: Xem lỗi Google ở đây (thường là mã 10, 12500, ...)
                                Log.e("LOGIN_DEBUG", "Google Sign-In Failed: " + error);
                                Toast.makeText(LoginActivity.this, "Lỗi Google: " + error, Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Log.e("LOGIN_DEBUG", "Google Sign-In Cancelled or Failed. Result Code: " + result.getResultCode());
                    }
                }
        );

        // Nút Google Sign In
        btnGoogleSignIn.setOnClickListener(v -> {
            Log.d("LOGIN_DEBUG", "Click Google Sign In Button");
            googleAuthHelper.signIn(googleSignInLauncher);
        });
    }
}