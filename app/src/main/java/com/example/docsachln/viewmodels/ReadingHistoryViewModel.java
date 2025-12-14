package com.example.docsachln.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.docsachln.repositories.ReadingHistoryRepository;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;

public class ReadingHistoryViewModel extends AndroidViewModel {
    private final ReadingHistoryRepository repository;

    public ReadingHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new ReadingHistoryRepository(application);
    }

    public void updateProgress(String userId, String bookId, String chapterId, int lastPosition) {
        if (userId == null) return;

        repository.updateReadingProgress(userId, bookId, chapterId, lastPosition, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                // Background update, không cần update UI
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Log error
            }
        });
    }
}