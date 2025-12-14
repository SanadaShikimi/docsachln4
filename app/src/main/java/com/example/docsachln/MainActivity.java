package com.example.docsachln;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.docsachln.R;
import com.example.docsachln.ui.home.ExploreFragment;
import com.example.docsachln.ui.home.HomeFragment;
import com.example.docsachln.ui.home.LibraryFragment;
import com.example.docsachln.ui.home.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Bottom navigation listener
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.nav_explore) {
                fragment = new ExploreFragment(); // Tạo sau
            } else if (itemId == R.id.nav_library) {
                fragment = new LibraryFragment();
            } else if (itemId == R.id.nav_profile) {
                fragment = new ProfileFragment();
            }
            return loadFragment(fragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}