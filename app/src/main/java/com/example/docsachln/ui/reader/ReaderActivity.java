package com.example.docsachln.ui.reader;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView; // Import ImageView
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import com.example.docsachln.R;
import com.example.docsachln.models.Chapter;
import com.example.docsachln.repositories.ChapterRepository;
import com.example.docsachln.utils.PreferenceManager;
import com.example.docsachln.viewmodels.ReadingHistoryViewModel;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONArray;
import java.io.IOException;

public class ReaderActivity extends AppCompatActivity implements ReaderSettingsDialog.SettingsListener {

    private ChapterRepository chapterRepository;
    private ReadingHistoryViewModel historyViewModel;
    private PreferenceManager preferenceManager;

    private String bookId, chapterId;
    private TextView tvTitle, tvContent;
    private RelativeLayout rootLayout;
    private View toolbar, bottomMenu;
    private ProgressBar progressBar;
    private NestedScrollView scrollView;
    private ImageView btnBookmark; // Nút Bookmark

    private boolean isControlsVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        // Get Intent Extras
        bookId = getIntent().getStringExtra("BOOK_ID");
        chapterId = getIntent().getStringExtra("CHAPTER_ID");

        initViews();
        setupRepository();

        if (chapterId != null) {
            loadChapterContent(chapterId);
        }
    }

    private void initViews() {
        rootLayout = findViewById(R.id.layout_reader_root);
        tvTitle = findViewById(R.id.tv_chapter_title);
        tvContent = findViewById(R.id.tv_content);
        toolbar = findViewById(R.id.toolbar);
        bottomMenu = findViewById(R.id.layout_bottom_menu);
        progressBar = findViewById(R.id.progress_bar);
        scrollView = findViewById(R.id.scroll_view);

        // Tìm nút bookmark trong toolbar
        btnBookmark = findViewById(R.id.btn_bookmark);

        // Setup Toolbar
        Toolbar toolbarView = (Toolbar) toolbar;
        toolbarView.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel);
        toolbarView.setNavigationOnClickListener(v -> finish());

        // Toggle controls on tap
        tvContent.setOnClickListener(v -> toggleControls());

        // Xử lý sự kiện bấm nút Bookmark
        btnBookmark.setOnClickListener(v -> toggleBookmark());

        // Open Settings
        findViewById(R.id.btn_settings).setOnClickListener(v -> {
            ReaderSettingsDialog dialog = new ReaderSettingsDialog(this, this);
            dialog.show();
        });

        // Save history on scroll change (Simple version)
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // Có thể thêm logic lưu vị trí cuộn ở đây
        });
    }

    private void setupRepository() {
        chapterRepository = new ChapterRepository(getApplication());
        historyViewModel = new ViewModelProvider(this).get(ReadingHistoryViewModel.class);
        preferenceManager = PreferenceManager.getInstance(this);
    }

    private void loadChapterContent(String id) {
        progressBar.setVisibility(View.VISIBLE);
        chapterRepository.getChapterById(id, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                if (response.isSuccessful()) {
                    try {
                        String body = response.body().string();
                        JSONArray array = new JSONArray(body);
                        if (array.length() > 0) {
                            Chapter chapter = new Gson().fromJson(array.getJSONObject(0).toString(), Chapter.class);

                            runOnUiThread(() -> {
                                tvTitle.setText(chapter.getTitle());
                                tvContent.setText(chapter.getContent());

                                // 1. Lưu trạng thái đã đọc (để tô màu xám ở danh sách chương)
                                preferenceManager.markChapterAsRead(chapterId);

                                // 2. Cập nhật icon Bookmark (kiểm tra xem chương này có phải bookmark không)
                                checkBookmarkStatus();

                                // Save History (Backend)
                                historyViewModel.updateProgress(
                                        preferenceManager.getUserId(),
                                        bookId,
                                        chapterId,
                                        0 // Position start
                                );
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> progressBar.setVisibility(View.GONE));
            }
        });
    }

    // Logic kiểm tra bookmark để hiển thị icon đúng
    private void checkBookmarkStatus() {
        String savedBookmarkChapterId = preferenceManager.getBookmark(bookId);
        boolean isBookmarked = chapterId.equals(savedBookmarkChapterId);
        updateBookmarkIcon(isBookmarked);
    }

    // Logic bấm nút bookmark
    private void toggleBookmark() {
        String savedBookmarkChapterId = preferenceManager.getBookmark(bookId);

        if (chapterId.equals(savedBookmarkChapterId)) {
            // Nếu đang bookmark chương này -> Bấm cái nữa là Xóa bookmark
            preferenceManager.removeBookmark(bookId);
            updateBookmarkIcon(false);
            Toast.makeText(this, "Đã bỏ dấu trang", Toast.LENGTH_SHORT).show();
        } else {
            // Nếu chưa bookmark hoặc đang bookmark chương khác -> Lưu chương này làm bookmark mới
            preferenceManager.saveBookmark(bookId, chapterId);
            updateBookmarkIcon(true);
            Toast.makeText(this, "Đã lưu dấu trang", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBookmarkIcon(boolean isBookmarked) {
        if (isBookmarked) {
            // Icon đã lưu (đậm/đầy) - Bạn cần có drawable này
            btnBookmark.setImageResource(R.drawable.ic_bookmark_filled);
            // Nếu muốn đổi màu icon sang màu xanh như docln (ví dụ)
            // btnBookmark.setColorFilter(Color.parseColor("#00AAFF"));
        } else {
            // Icon chưa lưu (viền) - Bạn cần có drawable này
            btnBookmark.setImageResource(R.drawable.ic_bookmark_border);
            // btnBookmark.clearColorFilter();
        }
    }

    private void toggleControls() {
        isControlsVisible = !isControlsVisible;
        toolbar.setVisibility(isControlsVisible ? View.VISIBLE : View.GONE);
        bottomMenu.setVisibility(isControlsVisible ? View.VISIBLE : View.GONE);
    }

    // --- Settings Impl ---
    @Override
    public void onBackgroundColorChanged(int bgColor, int textColor) {
        rootLayout.setBackgroundColor(bgColor);
        tvContent.setTextColor(textColor);
        tvTitle.setTextColor(textColor);
    }

    @Override
    public void onFontSizeChanged(int size) {
        tvContent.setTextSize(size);
    }
}