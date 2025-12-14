package com.example.docsachln.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.docsachln.models.Profile;
import com.example.docsachln.repositories.ProfileRepository;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;

public class ProfileViewModel extends AndroidViewModel {
    private final ProfileRepository profileRepository;
    private final Gson gson;

    private final MutableLiveData<Profile> profile = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.profileRepository = new ProfileRepository(application);
        this.gson = new Gson();
    }

    public MutableLiveData<Profile> getProfile() {
        return profile;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<Boolean> getUpdateSuccess() {
        return updateSuccess;
    }

    public void loadProfile(String userId) {
        isLoading.setValue(true);

        profileRepository.getProfile(userId, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoading.postValue(false);

                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseBody);

                        if (jsonArray.length() > 0) {
                            Profile userProfile = gson.fromJson(jsonArray.getJSONObject(0).toString(), Profile.class);
                            profile.postValue(userProfile);
                        }
                    } catch (JSONException e) {
                        errorMessage.postValue("Lỗi parse dữ liệu");
                    }
                } else {
                    errorMessage.postValue("Không thể tải profile");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi kết nối: " + e.getMessage());
            }
        });
    }

    public void updateProfile(String userId, String username, String fullName, String avatarUrl) {
        isLoading.setValue(true);

        profileRepository.updateProfile(userId, username, fullName, avatarUrl, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                isLoading.postValue(false);

                if (response.isSuccessful()) {
                    updateSuccess.postValue(true);
                    loadProfile(userId); // Reload profile
                } else {
                    errorMessage.postValue("Cập nhật thất bại");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi kết nối: " + e.getMessage());
            }
        });
    }
}