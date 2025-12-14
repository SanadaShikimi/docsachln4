package com.example.docsachln.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.docsachln.repositories.AuthRepository;
import com.example.docsachln.utils.PreferenceManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final PreferenceManager preferenceManager;

    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public AuthViewModel(@NonNull Application application) {
        super(application);
        this.authRepository = new AuthRepository(application);
        this.preferenceManager = PreferenceManager.getInstance(application);
    }

    public MutableLiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void signIn(String email, String password) {
        isLoading.setValue(true);

        authRepository.signIn(email, password, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoading.postValue(false);

                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);

                        String userId = jsonObject.getJSONObject("user").getString("id");
                        String accessToken = jsonObject.getString("access_token");
                        String refreshToken = jsonObject.getString("refresh_token");

                        preferenceManager.saveUserSession(userId, accessToken, refreshToken);
                        loginSuccess.postValue(true);

                    } catch (JSONException e) {
                        errorMessage.postValue("Lỗi parse dữ liệu");
                    }
                } else {
                    errorMessage.postValue("Email hoặc mật khẩu không đúng");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi kết nối: " + e.getMessage());
            }
        });
    }

    public void signUp(String email, String password, String username) {
        isLoading.setValue(true);

        authRepository.signUp(email, password, username, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoading.postValue(false);

                if (response.isSuccessful()) {
                    loginSuccess.postValue(true);
                } else {
                    errorMessage.postValue("Đăng ký thất bại. Email có thể đã được sử dụng.");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi kết nối: " + e.getMessage());
            }
        });
    }

    public void signOut() {
        authRepository.signOut(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                preferenceManager.clearUserSession();
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                preferenceManager.clearUserSession();
            }
        });
    }

    public void forgotPassword(String email) {
        isLoading.setValue(true);

        authRepository.forgotPassword(email, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoading.postValue(false);

                if (response.isSuccessful()) {
                    errorMessage.postValue("Email khôi phục mật khẩu đã được gửi");
                } else {
                    errorMessage.postValue("Email không tồn tại");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi kết nối: " + e.getMessage());
            }
        });
    }
    public void signInWithGoogle(String idToken) {
        isLoading.setValue(true);
        authRepository.signInWithGoogle(idToken, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoading.postValue(false);
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);

                        // Logic giống hệt signIn thường
                        JSONObject userObj = jsonObject.getJSONObject("user");
                        String userId = userObj.getString("id");
                        String accessToken = jsonObject.getString("access_token");
                        String refreshToken = jsonObject.getString("refresh_token");

                        preferenceManager.saveUserSession(userId, accessToken, refreshToken);
                        loginSuccess.postValue(true);
                    } catch (JSONException e) {
                        errorMessage.postValue("Lỗi parse Google login");
                    }
                } else {
                    errorMessage.postValue("Google Login thất bại");
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                isLoading.postValue(false);
                errorMessage.postValue(e.getMessage());
            }
        });
    }
}