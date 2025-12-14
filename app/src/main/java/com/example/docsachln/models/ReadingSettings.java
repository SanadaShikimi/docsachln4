package com.example.docsachln.models;

public class ReadingSettings {
    private String userId;
    private String fontFamily;
    private int fontSize;
    private float lineHeight;
    private String textAlign;
    private String textColor;
    private String backgroundColor;
    private int marginHorizontal;
    private int marginVertical;
    private String updatedAt;

    public ReadingSettings() {
        // Default values
        this.fontFamily = "default";
        this.fontSize = 16;
        this.lineHeight = 1.5f;
        this.textAlign = "left";
        this.textColor = "#000000";
        this.backgroundColor = "#FFFFFF";
        this.marginHorizontal = 16;
        this.marginVertical = 16;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }

    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }

    public float getLineHeight() { return lineHeight; }
    public void setLineHeight(float lineHeight) { this.lineHeight = lineHeight; }

    public String getTextAlign() { return textAlign; }
    public void setTextAlign(String textAlign) { this.textAlign = textAlign; }

    public String getTextColor() { return textColor; }
    public void setTextColor(String textColor) { this.textColor = textColor; }

    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

    public int getMarginHorizontal() { return marginHorizontal; }
    public void setMarginHorizontal(int marginHorizontal) { this.marginHorizontal = marginHorizontal; }

    public int getMarginVertical() { return marginVertical; }
    public void setMarginVertical(int marginVertical) { this.marginVertical = marginVertical; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}