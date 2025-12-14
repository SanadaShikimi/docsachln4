package com.example.docsachln.ui.auth;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.docsachln.R;
import com.example.docsachln.ui.main.MainActivity;
import com.example.docsachln.viewmodels.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogleSignIn;
    private TextView tvRegister;
    private ProgressBar progressBar;

    // Google Auth Helper (từ đoạn chat cũ của bạn)
    private GoogleAuthHelper googleAuthHelper;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo ViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Khởi tạo Google Auth
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
        });

        // Quan sát lỗi
        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        // Quan sát đăng nhập thành công
        authViewModel.getLoginSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
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
                                // Gọi ViewModel để verify với Supabase
                                // Cần tạo hàm này trong AuthViewModel hoặc gọi repo trực tiếp
                                // Nhưng tốt nhất là qua ViewModel.
                                // Tạm thời gọi trực tiếp repo ở đây nếu ViewModel chưa có hàm này,
                                // nhưng bạn đã có hàm signInWithGoogle trong AuthRepository rồi.
                                // Hãy thêm hàm signInWithGoogle vào AuthViewModel (xem chú thích bên dưới)
                                authViewModel.signInWithGoogle(idToken);
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );

        // Nút Google Sign In
        btnGoogleSignIn.setOnClickListener(v -> {
            googleAuthHelper.signIn(googleSignInLauncher);
        });
    }
}