package com.aigirlfriend.virtuallove.ui;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aigirlfriend.virtuallove.HelperResizer;
import com.aigirlfriend.virtuallove.R;
import com.aigirlfriend.virtuallove.ads.MyApplication;
import com.aigirlfriend.virtuallove.databinding.ActivitySettingBinding;
import java.util.ArrayList;

public class SettingActivity extends BaseActivity {
    ActivitySettingBinding binding;
    Dialog dialog;

    
    public long mLastClickTime = 0;
    int star = 0;
    ArrayList<View> starImagesArray = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        ActivitySettingBinding inflate = ActivitySettingBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        setContentView((View) inflate.getRoot());


        //Reguler Banner Ads
        RelativeLayout admob_banner = (RelativeLayout) binding.regulerBannerAd.AdmobBannerFrame;
        LinearLayout adContainer = (LinearLayout) binding.regulerBannerAd.bannerContainer;
        FrameLayout qureka = (FrameLayout) binding.regulerBannerAd.qureka;



        resize();
        this.binding.conRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String rateapp = getPackageName();
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + rateapp));
                startActivity(intent1);
            }

        });
        this.binding.conShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - SettingActivity.this.mLastClickTime >= 1000) {
                    long unused = SettingActivity.this.mLastClickTime = SystemClock.elapsedRealtime();
                    SettingActivity.this.shareApp();
                }
            }
        });
        this.binding.conPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - SettingActivity.this.mLastClickTime >= 1000) {
                    long unused = SettingActivity.this.mLastClickTime = SystemClock.elapsedRealtime();
                    //privacy
                    Intent intentPrivacy = new Intent(Intent.ACTION_VIEW, Uri.parse(MyApplication.PrivacyPolicy));
                    intentPrivacy.setPackage("com.android.chrome");
                    startActivity(intentPrivacy);
                }
            }

        });
        this.binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - SettingActivity.this.mLastClickTime >= 1000) {
                    long unused = SettingActivity.this.mLastClickTime = SystemClock.elapsedRealtime();
                    SettingActivity.this.onBackPressed();
                }
            }
        });

    }

    
    public void setStar(int i) {
        for (int i2 = 0; i2 < i; i2++) {
            ((ImageView) this.starImagesArray.get(i2)).setImageResource(R.drawable.starpress);
        }
        for (int i3 = 4; i3 > i - 1; i3--) {
            ((ImageView) this.starImagesArray.get(i3)).setImageResource(R.drawable.star_unpress);
        }
    }

    public void resize() {
        HelperResizer.getheightandwidth(this);
        HelperResizer.setSize(this.binding.conTopBar, 1080, 150);
        HelperResizer.setSize(this.binding.btnBack, 108, 108, true);
        HelperResizer.setSize(this.binding.conShare, 920, 185, true);
        HelperResizer.setSize(this.binding.conRate, 920, 185, true);
        HelperResizer.setSize(this.binding.conPrivacy, 920, 185, true);
        HelperResizer.setSize(this.binding.conButtonBg, 920, 396, true);
        HelperResizer.setSize(this.binding.btnUnlock, 500, 130, true);
    }

    
    public void shareApp() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + getPackageName());
        startActivity(Intent.createChooser(intent, "Share via..."));
    }
}
