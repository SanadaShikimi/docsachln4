package com.example.docsachln.repositories;

import android.content.Context;
import com.example.docsachln.api.CommentService;
import okhttp3.Callback;

public class CommentRepository {
    private final CommentService commentService;

    public CommentRepository(Context context) {
        this.commentService = new CommentService(context);
    }

    public void getCommentsByBookId(String bookId, Callback callback) {
        commentService.getCommentsByBookId(bookId, callback);
    }

    public void createComment(String bookId, String userId, String content, String parentId, Callback callback) {
        commentService.createComment(bookId, userId, content, parentId, callback);
    }

    public void updateComment(String commentId, String content, Callback callback) {
        commentService.updateComment(commentId, content, callback);
    }

    public void deleteComment(String commentId, Callback callback) {
        commentService.deleteComment(commentId, callback);
    }
}