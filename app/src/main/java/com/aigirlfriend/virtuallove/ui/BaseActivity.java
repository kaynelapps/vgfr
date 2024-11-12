package com.aigirlfriend.virtuallove.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import com.aigirlfriend.virtuallove.helper.BOOKER_SpManager;
import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void attachBaseContext(Context context) {
        super.attachBaseContext(setLocale(context, getSelectedLanguage(context)));
    }

    private void setupStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
    }

    public static Context setLocale(Context context, String str) {
        Locale locale = new Locale(str);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    private String getSelectedLanguage(Context context) {
        BOOKER_SpManager.initializingSharedPreference(context);
        return BOOKER_SpManager.getLanguageCodeSnip();
    }
}
