package com.aigirlfriend.virtuallove;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import com.aigirlfriend.virtuallove.constants.SPKeys;
import com.aigirlfriend.virtuallove.utils.SPUtils;
import com.aigirlfriend.virtuallove.utils.LocaleHelper;
import java.util.Calendar;
import org.litepal.LitePal;

public class BaseApp extends Application {
    public static Context mContext;

    @Override
    public void attachBaseContext(Context context) {
        String languageCode = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                .getString("language", "en_US");
        LocaleHelper.setLocale(context, languageCode);
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callBaseApp(this);
    }

    public void callBaseApp(BaseApp baseApp) {
        super.onCreate();
        mContext = baseApp;
        LitePal.initialize(baseApp);
        baseApp.initUseApp();
        String languageCode = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                .getString("language", "en_US");
        LocaleHelper.setLocale(this, languageCode);
    }

    private void initUseApp() {
        String todayDate = getTodayDate();
        if (!SPUtils.get(mContext, SPKeys.LAST_DATE, "").equals(todayDate)) {
            SPUtils.put(mContext, SPKeys.VIEW_REWARDED, 0);
            SPUtils.put(mContext, SPKeys.LAST_DATE, todayDate);
        }
    }

    private String getTodayDate() {
        Calendar instance = Calendar.getInstance();
        int i = instance.get(1);
        int i2 = instance.get(2);
        return String.valueOf(i) + i2 + instance.get(5);
    }

    public static Context getContext() {
        return mContext;
    }
}
