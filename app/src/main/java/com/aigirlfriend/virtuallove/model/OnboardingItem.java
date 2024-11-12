package com.aigirlfriend.virtuallove.model;

public class OnboardingItem {
    private int imageResource;
    private String title;
    private String description;
    private int backgroundColor;

    public OnboardingItem(int imageResource, String title, String description, int backgroundColor) {
        this.imageResource = imageResource;
        this.title = title;
        this.description = description;
        this.backgroundColor = backgroundColor;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
