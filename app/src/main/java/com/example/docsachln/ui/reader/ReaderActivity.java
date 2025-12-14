package com.example.docsachln.ui.reader;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        // Setup Toolbar
        ((Toolbar) toolbar).setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel);
        ((Toolbar) toolbar).setNavigationOnClickListener(v -> finish());

        // Toggle controls on tap
        tvContent.setOnClickListener(v -> toggleControls());

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

                                // Save History
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