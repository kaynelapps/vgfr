package com.aigirlfriend.virtuallove.ads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;
import com.ads.control.ads.AperoAd;
import com.ads.control.admob.Admob;
import com.ads.control.admob.AppOpenManager;
import com.ads.control.config.AperoAdConfig;
import com.aigirlfriend.virtuallove.config.AdsConfig;
import com.aigirlfriend.virtuallove.constants.SPKeys;
import com.aigirlfriend.virtuallove.utils.SPUtils;
import com.aigirlfriend.virtuallove.config.BuildConfig;
import com.onesignal.OneSignal;
import org.litepal.LitePal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import com.ads.control.config.AdjustConfig;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.aigirlfriend.virtuallove.splashAds.SplashActivity;
import com.aigirlfriend.virtuallove.utils.LocaleHelper;

public class MyApplication extends Application implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String ONESIGNAL_APP_ID = "48c23e6b-e68a-4707-a0e6-45076e141fa9";
    private static final String ADJUST_TOKEN = "jvnmcca1t1xc";
    private static final String EVENT_AD_IMPRESSION_ADJUST = "gpo6yz";
    private static final String EVENT_PURCHASE_ADJUST = "gvztpa";
    public static SharedPreferences sharedPreferencesInApp;
    public static SharedPreferences.Editor editorInApp;
    private Activity currentActivity;
    public static String MoreApps = "AppSonics+Studio";
    public static String PrivacyPolicy = "https://dworarena.blogspot.com/2020/08/privacy-dwor-arena.html";
    public static int checkInAppUpdate = 0;
    public static Context context1;
    public static Context mContext;
    private static final String FACEBOOK_APP_ID = "1254280805815684";
    private static final String FACEBOOK_CLIENT_TOKEN = "05f995e279202d0bb21476377723d4ad";

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
        String languageCode = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
                .getString("language", "en_US");
        LocaleHelper.setLocale(this, languageCode);
        String hashKey = getHashKey();
        Log.d("HashKey", "Hash Key: " + hashKey);
        registerActivityLifecycleCallbacks(this);
        sharedPreferencesInApp = getSharedPreferences("my", MODE_PRIVATE);
        editorInApp = sharedPreferencesInApp.edit();
        context1 = getApplicationContext();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        mContext = getApplicationContext();
        LitePal.initialize(getApplicationContext());
        initUseApp();
        initAds();
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        FacebookSdk.setApplicationId(FACEBOOK_APP_ID);
        FacebookSdk.setClientToken(FACEBOOK_CLIENT_TOKEN);
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
    }

    private String getHashKey() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initAds() {
        String environment = BuildConfig.DEBUG ? AperoAdConfig.ENVIRONMENT_DEVELOP : AperoAdConfig.ENVIRONMENT_PRODUCTION;
        AperoAdConfig config = new AperoAdConfig(this, BuildConfig.API_KEY_ADS, AperoAdConfig.PROVIDER_ADMOB, environment);
        config.setListDeviceTest(Arrays.asList(BuildConfig.DEVICE_ID_TEST, BuildConfig.EMULATOR_ID_TEST));
        AdjustConfig adjustConfig = new AdjustConfig(ADJUST_TOKEN);
        adjustConfig.setEventAdImpression(EVENT_AD_IMPRESSION_ADJUST);
        adjustConfig.setEventNamePurchase(EVENT_PURCHASE_ADJUST);
        config.setAdjustConfig(adjustConfig);
        config.setIdAdResume(BuildConfig.ads_open_app);
        config.setIdAdResumeMedium(BuildConfig.ads_open_app_medium);
        config.setIdAdResumeHigh(BuildConfig.ads_open_app_high);
        config.setListDeviceTest(Arrays.asList(BuildConfig.DEVICE_ID_TEST));
        config.setMediationProvider(AperoAdConfig.PROVIDER_ADMOB);
        config.setIntervalInterstitialAd(30000);
        AperoAd.getInstance().init(this, config, BuildConfig.DEBUG);
        AperoAd.getInstance().initAdsNetwork();
        Log.d("AdsInitialization", "Ads initialized with config: " + config.toString());
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
        AppOpenManager.getInstance().setSplashAdId(BuildConfig.ads_open_app);
        AppOpenManager.getInstance().setEnableScreenContentCallback(true);
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {}

    @Override
    public void onActivityPaused(@NonNull Activity activity) {}

    @Override
    public void onActivityStopped(@NonNull Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {}

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    public static void setuser_balance(Integer user_balance) {
        editorInApp.putInt("user_balance", user_balance).commit();
    }

    public static Integer getuser_balance() {
        return sharedPreferencesInApp.getInt("user_balance", 0);
    }

    public static void setuser_onetime(Integer user_onetime) {
        editorInApp.putInt("user_onetime", user_onetime).commit();
    }

    public static Integer getuser_onetime() {
        return sharedPreferencesInApp.getInt("user_onetime", 0);
    }

    public static void setuser_permission(Integer user_permission) {
        editorInApp.putInt("user_permission", user_permission).commit();
    }

    public static Integer getuser_permission() {
        return sharedPreferencesInApp.getInt("user_permission", 0);
    }

    public static void setuser_guideline(Integer user_guideline) {
        editorInApp.putInt("user_guideline", user_guideline).commit();
    }

    public static Integer getuser_guideline() {
        return sharedPreferencesInApp.getInt("user_guideline", 0);
    }

    private void initUseApp() {
        String todayDate = getTodayDate();
        if (!SPUtils.get(mContext, SPKeys.LAST_DATE, "").equals(todayDate)) {
            SPUtils.put(mContext, SPKeys.VIEW_REWARDED, 0);
            SPUtils.put(mContext, SPKeys.LAST_DATE, todayDate);
        }
    }

    private String getTodayDate() {
        Calendar instance2 = Calendar.getInstance();
        int i = instance2.get(1);
        int i2 = instance2.get(2);
        return String.valueOf(i) + i2 + instance2.get(5);
    }
}
