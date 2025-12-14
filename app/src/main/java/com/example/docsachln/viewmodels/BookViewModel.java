package com.example.docsachln.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.docsachln.models.Book;
import com.example.docsachln.repositories.BookRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends AndroidViewModel {
    private final BookRepository bookRepository;
    private final Gson gson;

    private final MutableLiveData<List<Book>> books = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public BookViewModel(@NonNull Application application) {
        super(application);
        this.bookRepository = new BookRepository(application);
        this.gson = new Gson();
    }

    public MutableLiveData<List<Book>> getBooks() {
        return books;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadBooks(int offset, int limit) {
        isLoading.setValue(true);

        bookRepository.getBooks(offset, limit, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoading.postValue(false);

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Type listType = new TypeToken<List<Book>>(){}.getType();
                    List<Book> bookList = gson.fromJson(responseBody, listType);
                    books.postValue(bookList != null ? bookList : new ArrayList<>());
                } else {
                    errorMessage.postValue("Không thể tải sách");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi kết nối: " + e.getMessage());
            }
        });
    }

    public void searchBooks(String query) {
        isLoading.setValue(true);

        bookRepository.searchBooks(query, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoading.postValue(false);

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Type listType = new TypeToken<List<Book>>(){}.getType();
                    List<Book> bookList = gson.fromJson(responseBody, listType);
                    books.postValue(bookList != null ? bookList : new ArrayList<>());
                } else {
                    errorMessage.postValue("Không tìm thấy sách");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi kết nối: " + e.getMessage());
            }
        });
    }

    public void loadBooksByCategory(String categoryId, int offset, int limit) {
        isLoading.setValue(true);

        bookRepository.getBooksByCategory(categoryId, offset, limit, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoading.postValue(false);

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Type listType = new TypeToken<List<Book>>(){}.getType();
                    List<Book> bookList = gson.fromJson(responseBody, listType);
                    books.postValue(bookList != null ? bookList : new ArrayList<>());
                } else {
                    errorMessage.postValue("Không thể tải sách");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                isLoading.postValue(false);
                errorMessage.postValue("Lỗi kết nối: " + e.getMessage());
            }
        });
    }

    public void loadPopularBooks(int limit) {
        isLoading.setValue(true);

        bookRepository.getPopularBooks(limit, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoading.postValue(false);

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Type listType = new TypeToken<List<Book>>(){}.getType();
                    List<Book> bookList = gson.fromJson(responseBody, listType);
                    books.postValue(bookList != null ? bookList : new ArrayList<>());
                } else {
                    errorMessage.postValue("Không thể tải sách phổ biến");
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