package com.example.docsachln.ui.reader;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.docsachln.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ReaderSettingsDialog extends BottomSheetDialog {

    public interface SettingsListener {
        void onBackgroundColorChanged(int color, int textColor);
        void onFontSizeChanged(int size);
    }

    private final SettingsListener listener;
    private int currentFontSize = 18;

    public ReaderSettingsDialog(Context context, SettingsListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reader_settings);

        // Màu nền
        findViewById(R.id.bg_white).setOnClickListener(v ->
                listener.onBackgroundColorChanged(0xFFFFFFFF, 0xFF000000)); // Nền Trắng, Chữ Đen

        findViewById(R.id.bg_sepia).setOnClickListener(v ->
                listener.onBackgroundColorChanged(0xFFF4ECD8, 0xFF5D4037)); // Nền Vàng nhạt, Chữ Nâu

        findViewById(R.id.bg_black).setOnClickListener(v ->
                listener.onBackgroundColorChanged(0xFF121212, 0xFFB0B0B0)); // Nền Đen, Chữ Xám

        // Cỡ chữ
        TextView tvFontSize = findViewById(R.id.tv_font_size);
        findViewById(R.id.btn_decrease_font).setOnClickListener(v -> {
            if (currentFontSize > 12) {
                currentFontSize -= 2;
                tvFontSize.setText(String.valueOf(currentFontSize));
                listener.onFontSizeChanged(currentFontSize);
            }
        });

        findViewById(R.id.btn_increase_font).setOnClickListener(v -> {
            if (currentFontSize < 32) {
                currentFontSize += 2;
                tvFontSize.setText(String.valueOf(currentFontSize));
                listener.onFontSizeChanged(currentFontSize);
            }
        });
    }
}