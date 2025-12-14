package com.example.docsachln.ui.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.docsachln.R;
import com.example.docsachln.adapters.ChapterAdapter;
import com.example.docsachln.ui.reader.ReaderActivity;
import com.example.docsachln.viewmodels.BookDetailViewModel;
import com.example.docsachln.viewmodels.FavoriteViewModel;

public class BookDetailActivity extends AppCompatActivity {

    private BookDetailViewModel bookDetailViewModel;
    private FavoriteViewModel favoriteViewModel;
    private ChapterAdapter chapterAdapter;
    private String bookId;

    private ImageView imgCover;
    private TextView tvTitle, tvAuthor, tvDescription;
    private Button btnReadNow;
    private ImageButton btnFavorite;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Get Book ID from Intent
        bookId = getIntent().getStringExtra("BOOK_ID");
        if (bookId == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID sách", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupViewModels();
        setupObservers();

        // Load data
        bookDetailViewModel.loadBookDetail(bookId);
        bookDetailViewModel.loadChapters(bookId);
        favoriteViewModel.checkFavorite(bookId);
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        imgCover = findViewById(R.id.img_cover);
        tvTitle = findViewById(R.id.tv_title);
        tvAuthor = findViewById(R.id.tv_author);
        tvDescription = findViewById(R.id.tv_description);
        btnReadNow = findViewById(R.id.btn_read_now);
        btnFavorite = findViewById(R.id.btn_favorite);
        progressBar = findViewById(R.id.progress_bar);

        RecyclerView rvChapters = findViewById(R.id.rv_chapters);
        chapterAdapter = new ChapterAdapter(chapter -> {
            Intent intent = new Intent(this, ReaderActivity.class);
            intent.putExtra("BOOK_ID", bookId);
            intent.putExtra("CHAPTER_ID", chapter.getId());
            startActivity(intent);
        });
        rvChapters.setLayoutManager(new LinearLayoutManager(this));
        rvChapters.setAdapter(chapterAdapter);

        btnReadNow.setOnClickListener(v -> {
            // Mở chương đầu tiên nếu có
            if (chapterAdapter.getItemCount() > 0) {
                // Logic lấy chương 1 sẽ xử lý sau, tạm thời thông báo
                Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sách chưa có chương nào", Toast.LENGTH_SHORT).show();
            }
        });

        btnFavorite.setOnClickListener(v -> favoriteViewModel.toggleFavorite(bookId));
    }

    private void setupViewModels() {
        bookDetailViewModel = new ViewModelProvider(this).get(BookDetailViewModel.class);
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
    }

    private void setupObservers() {
        // Book Details
        bookDetailViewModel.getBook().observe(this, book -> {
            if (book != null) {
                tvTitle.setText(book.getTitle());
                tvAuthor.setText(book.getAuthor());
                tvDescription.setText(book.getDescription());

                if (book.getCoverImageUrl() != null && !book.getCoverImageUrl().isEmpty()) {
                    Glide.with(this).load(book.getCoverImageUrl()).into(imgCover);
                }
            }
        });

        // Chapters
        bookDetailViewModel.getChapters().observe(this, chapters -> {
            chapterAdapter.setChapters(chapters);
        });

        // Favorite Status
        favoriteViewModel.getIsFavorited().observe(this, isFav -> {
            if (isFav) {
                btnFavorite.setImageResource(android.R.drawable.star_big_on);
            } else {
                btnFavorite.setImageResource(android.R.drawable.star_big_off);
            }
        });

        // Loading & Error
        bookDetailViewModel.getIsLoading().observe(this, isLoading ->
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE)
        );
    }
}