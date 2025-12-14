package com.example.docsachln.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.docsachln.R;
import com.example.docsachln.adapters.BookAdapter;
import com.example.docsachln.viewmodels.FavoriteViewModel;
import com.google.android.material.tabs.TabLayout;

public class LibraryFragment extends Fragment {

    private FavoriteViewModel favoriteViewModel;
    private BookAdapter bookAdapter;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        RecyclerView rvLibraryBooks = view.findViewById(R.id.rv_library_books);
        tabLayout = view.findViewById(R.id.tab_layout);

        bookAdapter = new BookAdapter(requireContext(), book -> {
            // TODO: Navigate to BookDetailActivity
        });

        rvLibraryBooks.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvLibraryBooks.setAdapter(bookAdapter);

        // Tab listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // Yêu thích
                        favoriteViewModel.loadFavorites();
                        break;
                    case 1: // Lịch sử
                        // TODO: Load reading history
                        break;
                    case 2: // Ghi chú
                        // TODO: Load notebooks
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Observe favorites
        favoriteViewModel.getFavoriteBooks().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                bookAdapter.setBooks(books);
            }
        });

        // Initial load
        favoriteViewModel.loadFavorites();
    }
}