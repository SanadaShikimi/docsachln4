package com.example.docsachln.repositories;

import android.content.Context;
import com.example.docsachln.api.ReadingHistoryService;
import okhttp3.Callback;

public class ReadingHistoryRepository {
    private final ReadingHistoryService readingHistoryService;

    public ReadingHistoryRepository(Context context) {
        this.readingHistoryService = new ReadingHistoryService(context);
    }

    public void getUserReadingHistory(String userId, Callback callback) {
        readingHistoryService.getUserReadingHistory(userId, callback);
    }

    public void updateReadingProgress(String userId, String bookId, String chapterId, int lastPosition, Callback callback) {
        readingHistoryService.updateReadingProgress(userId, bookId, chapterId, lastPosition, callback);
    }

    public void deleteReadingHistory(String userId, String bookId, Callback callback) {
        readingHistoryService.deleteReadingHistory(userId, bookId, callback);
    }
}