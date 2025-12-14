package com.example.docsachln.ui.auth;

import android.app.AlertDialog; // ✅ Mới thêm
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater; // ✅ Mới thêm
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
import com.example.docsachln.viewmodels.AuthViewModel;

public class LoginActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogleSignIn;
    private TextView tvRegister, tvForgotPassword; // ✅ Đã thêm tvForgotPassword
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
        tvForgotPassword = findViewById(R.id.tvForgotPassword); // ✅ Ánh xạ View mới (Đảm bảo ID này có trong XML)
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
            if (tvForgotPassword != null) tvForgotPassword.setEnabled(!isLoading); // Khóa nút khi loading
        });

        // Quan sát lỗi từ ViewModel (Lỗi Supabase trả về)
        // Lưu ý: AuthViewModel cũ sẽ bắn cả thông báo thành công (như "Đã gửi email...") vào đây
        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Log.e("LOGIN_DEBUG", "Auth Message: " + error);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        // Quan sát đăng nhập thành công
        authViewModel.getLoginSuccess().observe(this, isSuccess -> {
            if (isSuccess) {
                Log.d("LOGIN_DEBUG", "Login Success! Navigating to Main.");
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

        // ✅ Sự kiện bấm nút Quên mật khẩu
        if (tvForgotPassword != null) {
            tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
        }

        // Xử lý kết quả Google Sign In
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        googleAuthHelper.handleSignInResult(data, new GoogleAuthHelper.GoogleAuthCallback() {
                            @Override
                            public void onSuccess(String idToken) {
                                Log.d("LOGIN_DEBUG", "Google ID Token received: " + idToken);
                                authViewModel.signInWithGoogle(idToken);
                            }

                            @Override
                            public void onError(String error) {
                                Log.e("LOGIN_DEBUG", "Google Sign-In Failed: " + error);
                                Toast.makeText(LoginActivity.this, "Lỗi Google: " + error, Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Log.e("LOGIN_DEBUG", "Google Sign-In Cancelled. Result Code: " + result.getResultCode());
                    }
                }
        );

        // Nút Google Sign In
        btnGoogleSignIn.setOnClickListener(v -> {
            Log.d("LOGIN_DEBUG", "Click Google Sign In Button");
            googleAuthHelper.signIn(googleSignInLauncher);
        });
    }

    // ✅ HÀM HIỂN THỊ DIALOG QUÊN MẬT KHẨU
    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Inflate layout dialog_forgot_password.xml
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_forgot_password, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        // Làm nền trong suốt để bo góc đẹp hơn
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Ánh xạ các view trong Dialog
        EditText edtResetEmail = view.findViewById(R.id.edt_reset_email);
        Button btnSend = view.findViewById(R.id.btn_send);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        // Nút Hủy
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        // Nút Gửi Link
        btnSend.setOnClickListener(v -> {
            String email = edtResetEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập Email", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Gọi hàm ViewModel (Chỉ truyền Email - dùng hàm cũ 1 tham số)
            authViewModel.forgotPassword(email);

            // 2. Đóng Dialog ngay lập tức
            dialog.dismiss();

            // 3. Thông báo cho người dùng biết đang xử lý
            Toast.makeText(LoginActivity.this, "Đang gửi yêu cầu...", Toast.LENGTH_SHORT).show();

            // Kết quả (Thành công/Thất bại) sẽ được hiện ra bởi Observer "errorMessage"
            // mà bạn đã cài đặt trong hàm setupObservers() ở trên.
        });

        dialog.show();
    }
}