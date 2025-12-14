package com.example.docsachln.repositories;

import android.content.Context;
import com.example.docsachln.api.ChapterService;
import okhttp3.Callback;

public class ChapterRepository {
    private final ChapterService chapterService;

    public ChapterRepository(Context context) {
        this.chapterService = new ChapterService(context);
    }

    public void getChaptersByBookId(String bookId, Callback callback) {
        chapterService.getChaptersByBookId(bookId, callback);
    }

    public void getChapterById(String chapterId, Callback callback) {
        chapterService.getChapterById(chapterId, callback);
    }

    public void createChapter(String bookId, String title, int chapterNumber, String content, Callback callback) {
        chapterService.createChapter(bookId, title, chapterNumber, content, callback);
    }

    public void updateChapter(String chapterId, String title, String content, Callback callback) {
        chapterService.updateChapter(chapterId, title, content, callback);
    }

    public void deleteChapter(String chapterId, Callback callback) {
        chapterService.deleteChapter(chapterId, callback);
    }
}