package com.example.docsachln.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.docsachln.models.Book;
import com.example.docsachln.repositories.FavoriteRepository;
import com.example.docsachln.utils.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {
    private final FavoriteRepository favoriteRepository;
    private final PreferenceManager preferenceManager;
    private final Gson gson;

    private final MutableLiveData<List<Book>> favoriteBooks = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFavorited = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        this.favoriteRepository = new FavoriteRepository(application);
        this.preferenceManager = PreferenceManager.getInstance(application);
        this.gson = new Gson();
    }

    public MutableLiveData<List<Book>> getFavoriteBooks() { return favoriteBooks; }
    public MutableLiveData<Boolean> getIsFavorited() { return isFavorited; }
    public MutableLiveData<String> getErrorMessage() { return errorMessage; }

    public void loadFavorites() {
        String userId = preferenceManager.getUserId();
        if (userId == null) return;

        favoriteRepository.getUserFavorites(userId, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    // Parse response and extract books
                    // Response format: [{ id, user_id, book_id, book: {...} }]
                    List<Book> books = new ArrayList<>();
                    // TODO: Parse JSON properly
                    favoriteBooks.postValue(books);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                errorMessage.postValue("Không thể tải danh sách yêu thích");
            }
        });
    }

    public void checkFavorite(String bookId) {
        String userId = preferenceManager.getUserId();
        if (userId == null) return;

        favoriteRepository.isFavorited(userId, bookId, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    // If returns empty array [], not favorited
                    boolean favorited = !responseBody.equals("[]");
                    isFavorited.postValue(favorited);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Ignore
            }
        });
    }

    public void toggleFavorite(String bookId) {
        String userId = preferenceManager.getUserId();
        if (userId == null) return;

        Boolean currentState = isFavorited.getValue();
        if (currentState != null && currentState) {
            // Remove favorite
            favoriteRepository.removeFavorite(userId, bookId, new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.isSuccessful()) {
                        isFavorited.postValue(false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    errorMessage.postValue("Không thể bỏ yêu thích");
                }
            });
        } else {
            // Add favorite
            favoriteRepository.addFavorite(userId, bookId, new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.isSuccessful()) {
                        isFavorited.postValue(true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    errorMessage.postValue("Không thể thêm yêu thích");
                }
            });
        }
    }
}