package com.example.docsachln.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.docsachln.models.Book;
import com.example.docsachln.models.Chapter;
import com.example.docsachln.repositories.BookRepository;
import com.example.docsachln.repositories.ChapterRepository;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookDetailViewModel extends AndroidViewModel {
    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;
    private final Gson gson;

    private final MutableLiveData<Book> book = new MutableLiveData<>();
    private final MutableLiveData<List<Chapter>> chapters = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public BookDetailViewModel(@NonNull Application application) {
        super(application);
        this.bookRepository = new BookRepository(application);
        this.chapterRepository = new ChapterRepository(application);
        this.gson = new Gson();
    }

    public MutableLiveData<Book> getBook() { return book; }
    public MutableLiveData<List<Chapter>> getChapters() { return chapters; }
    public MutableLiveData<Boolean> getIsLoading() { return isLoading; }
    public MutableLiveData<String> getErrorMessage() { return errorMessage; }

    public void loadBookDetail(String bookId) {
        isLoading.setValue(true);

        bookRepository.getBookById(bookId, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoading.postValue(false);

                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseBody);

                        if (jsonArray.length() > 0) {
                            Book bookDetail = gson.fromJson(jsonArray.getJSONObject(0).toString(), Book.class);
                            book.postValue(bookDetail);
                        }
                    } catch (JSONException e) {
                        errorMessage.postValue("Lỗi tải thông tin sách");
                    }
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

    public void loadChapters(String bookId) {
        chapterRepository.getChaptersByBookId(bookId, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Chapter[] chapterArray = gson.fromJson(responseBody, Chapter[].class);
                    chapters.postValue(chapterArray != null ? Arrays.asList(chapterArray) : new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                errorMessage.postValue("Không thể tải danh sách chương");
            }
        });
    }
}