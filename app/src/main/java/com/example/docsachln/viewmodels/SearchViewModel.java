package com.example.docsachln.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.docsachln.models.Book;
import com.example.docsachln.models.Category;
import com.example.docsachln.repositories.BookRepository;
import com.example.docsachln.repositories.CategoryRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends AndroidViewModel {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final Gson gson;

    private final MutableLiveData<List<Book>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public SearchViewModel(@NonNull Application application) {
        super(application);
        bookRepository = new BookRepository(application);
        categoryRepository = new CategoryRepository(application);
        gson = new Gson();
    }

    public MutableLiveData<List<Book>> getSearchResults() { return searchResults; }
    public MutableLiveData<List<Category>> getCategories() { return categories; }
    public MutableLiveData<Boolean> getIsLoading() { return isLoading; }

    public void searchBooks(String query) {
        if (query == null || query.isEmpty()) {
            searchResults.postValue(new ArrayList<>());
            return;
        }

        isLoading.setValue(true);
        bookRepository.searchBooks(query, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                isLoading.postValue(false);
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Type listType = new TypeToken<List<Book>>(){}.getType();
                    List<Book> books = gson.fromJson(responseBody, listType);
                    searchResults.postValue(books != null ? books : new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                isLoading.postValue(false);
            }
        });
    }

    public void loadCategories() {
        categoryRepository.getAllCategories(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Type listType = new TypeToken<List<Category>>(){}.getType();
                    List<Category> categoryList = gson.fromJson(responseBody, listType);
                    categories.postValue(categoryList != null ? categoryList : new ArrayList<>());
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {}
        });
    }
}