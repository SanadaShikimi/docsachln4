package com.example.docsachln.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.docsachln.R;
import com.example.docsachln.ui.auth.LoginActivity;
import com.example.docsachln.utils.PreferenceManager;
import com.example.docsachln.utils.ThemeManager;
import com.example.docsachln.viewmodels.AuthViewModel;
import com.example.docsachln.viewmodels.ProfileViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private AuthViewModel authViewModel;
    private PreferenceManager preferenceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        preferenceManager = PreferenceManager.getInstance(requireContext());

        // Views
        CircleImageView imgAvatar = view.findViewById(R.id.img_avatar);
        TextView tvUsername = view.findViewById(R.id.tv_username);
        TextView tvEmail = view.findViewById(R.id.tv_email);
        TextView btnMyBooks = view.findViewById(R.id.btn_my_books);
        TextView btnSettings = view.findViewById(R.id.btn_settings);
        TextView btnDarkMode = view.findViewById(R.id.btn_dark_mode);
        TextView btnLogout = view.findViewById(R.id.btn_logout);

        // Load profile
        String userId = preferenceManager.getUserId();
        if (userId != null) {
            profileViewModel.loadProfile(userId);
        }

        // Observe profile
        profileViewModel.getProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                tvUsername.setText(profile.getUsername());
                tvEmail.setText(profile.getEmail());
                // TODO: Load avatar with Glide
            }
        });

        // Listeners
        btnMyBooks.setOnClickListener(v -> {
            // TODO: Navigate to MyBooksActivity
            Toast.makeText(requireContext(), "Sách của tôi", Toast.LENGTH_SHORT).show();
        });

        btnSettings.setOnClickListener(v -> {
            // TODO: Navigate to SettingsActivity
            Toast.makeText(requireContext(), "Cài đặt", Toast.LENGTH_SHORT).show();
        });

        btnDarkMode.setOnClickListener(v -> {
            ThemeManager themeManager = ThemeManager.getInstance(requireContext());
            int currentMode = themeManager.getThemeMode();
            // Toggle between light and dark
            int newMode = (currentMode == ThemeManager.THEME_LIGHT) ?
                    ThemeManager.THEME_DARK : ThemeManager.THEME_LIGHT;
            themeManager.setThemeMode(newMode);
            requireActivity().recreate();
        });

        btnLogout.setOnClickListener(v -> {
            authViewModel.signOut();
            preferenceManager.clearUserSession();
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        });
    }
}