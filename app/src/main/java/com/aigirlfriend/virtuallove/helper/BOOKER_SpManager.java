package com.aigirlfriend.virtuallove.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class BOOKER_SpManager {
    public static String GUIDE_COMPLETED = "guide_completed";
    public static String LANGUAGE_CODE = "language_code";
    public static String LANGUAGE_CODE_SNIP = "language_code_snip";
    public static String LANGUAGE_SELECTED = "language_selected";
    static SharedPreferences.Editor myEdit;
    static SharedPreferences sharedPreferences;

    public enum ASPECT_RATIO {
        AR_3_4,
        AR_1_1,
        AR_9_16
    }

    public enum POSITION {
        TOP_RIGHT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        BOTTOM_LEFT,
        CENTER,
        RIGHT,
        LEFT
    }

    public enum TIMER {
        TIMER_0,
        TIMER_3,
        TIMER_5,
        TIMER_10
    }

    public static void initializingSharedPreference(Context context) {
        SharedPreferences sharedPreferences2 = context.getSharedPreferences("MySharedPref123", 0);
        sharedPreferences = sharedPreferences2;
        myEdit = sharedPreferences2.edit();
    }

    public static boolean getLanguageSelected() {
        return sharedPreferences.getBoolean(LANGUAGE_SELECTED, false);
    }

    public static void setLanguageSelected(boolean z) {
        sharedPreferences.edit().putBoolean(LANGUAGE_SELECTED, z).apply();
    }

    public static boolean getGuideCompleted() {
        return sharedPreferences.getBoolean(GUIDE_COMPLETED, false);
    }

    public static void setGuideCompleted(boolean z) {
        sharedPreferences.edit().putBoolean(GUIDE_COMPLETED, z).apply();
    }

    public static String getLanguageCode() {
        return sharedPreferences.getString(LANGUAGE_CODE, "English");
    }

    public static void setLanguageCode(String str) {
        sharedPreferences.edit().putString(LANGUAGE_CODE, str).apply();
    }

    public static String getLanguageCodeSnip() {
        return sharedPreferences.getString(LANGUAGE_CODE_SNIP, "en");
    }

    public static void setLanguageCodeSnip(String str) {
        sharedPreferences.edit().putString(LANGUAGE_CODE_SNIP, str).apply();
    }
}
