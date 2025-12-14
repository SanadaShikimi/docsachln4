package com.example.docsachln.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.docsachln.R;
import com.example.docsachln.adapters.BookAdapter;
import com.example.docsachln.models.Book;
import com.example.docsachln.utils.Constants;
import com.example.docsachln.viewmodels.BookViewModel;

public class HomeFragment extends Fragment {

    private BookViewModel bookViewModel;
    private BookAdapter bookAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModel
        bookViewModel = new ViewModelProvider(this).get(BookViewModel.class);

        // Initialize Views
        RecyclerView rvPopularBooks = view.findViewById(R.id.rv_popular_books);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        progressBar = view.findViewById(R.id.progress_bar);

        // Setup RecyclerView
        bookAdapter = new BookAdapter(requireContext(), this::onBookClick);
        rvPopularBooks.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPopularBooks.setAdapter(bookAdapter);

        // Observe ViewModel
        setupObservers();

        // Load data
        bookViewModel.loadPopularBooks(20);

        // Swipe to refresh
        swipeRefresh.setOnRefreshListener(() -> {
            bookViewModel.loadPopularBooks(20);
        });
    }

    private void setupObservers() {
        bookViewModel.getBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                bookAdapter.setBooks(books);
            }
        });

        bookViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            swipeRefresh.setRefreshing(false);
        });

        bookViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onBookClick(Book book) {
        Intent intent = new Intent(requireContext(), com.example.docsachln.ui.book.BookDetailActivity.class);

        // Truyền ID sách sang màn hình chi tiết để nó biết cần tải sách nào
        intent.putExtra("BOOK_ID", book.getId());

        startActivity(intent);    }
}