package com.example.docsachln.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.docsachln.R;
import com.example.docsachln.adapters.BookAdapter;
import com.example.docsachln.ui.book.BookDetailActivity;
import com.example.docsachln.viewmodels.SearchViewModel;
import java.util.Timer;
import java.util.TimerTask;

public class ExploreFragment extends Fragment {
    private SearchViewModel searchViewModel;
    private BookAdapter bookAdapter;
    private EditText etSearch;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private Timer timer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        etSearch = view.findViewById(R.id.et_search);
        progressBar = view.findViewById(R.id.progress_bar);
        tvEmptyState = view.findViewById(R.id.tv_empty_state);
        RecyclerView rvResults = view.findViewById(R.id.rv_search_results);

        bookAdapter = new BookAdapter(requireContext(), book -> {
            Intent intent = new Intent(requireContext(), BookDetailActivity.class);
            intent.putExtra("BOOK_ID", book.getId());
            startActivity(intent);
        });

        rvResults.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvResults.setAdapter(bookAdapter);

        setupSearchListener();
        setupObservers();
    }

    private void setupSearchListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) timer.cancel();
            }
            @Override
            public void afterTextChanged(Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        requireActivity().runOnUiThread(() ->
                                searchViewModel.searchBooks(s.toString().trim())
                        );
                    }
                }, 600); // Delay 600ms để tránh spam API
            }
        });
    }

    private void setupObservers() {
        searchViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading ->
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE)
        );

        searchViewModel.getSearchResults().observe(getViewLifecycleOwner(), books -> {
            bookAdapter.setBooks(books);
            tvEmptyState.setVisibility(books.isEmpty() ? View.VISIBLE : View.GONE);
            if (!books.isEmpty()) tvEmptyState.setVisibility(View.GONE);
            else if (etSearch.getText().length() > 0) tvEmptyState.setText("Không tìm thấy kết quả");
        });
    }
}