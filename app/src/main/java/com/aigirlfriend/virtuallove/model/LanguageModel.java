package com.aigirlfriend.virtuallove.model;

public class LanguageModel {
    private String name;
    private String code;
    private int flagResource;
    private boolean isSelected;

    public LanguageModel(String name, String code, int flagResource) {
        this.name = name;
        this.code = code;
        this.flagResource = flagResource;
        this.isSelected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getFlagResource() {
        return flagResource;
    }

    public void setFlagResource(int flagResource) {
        this.flagResource = flagResource;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
