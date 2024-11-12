package com.aigirlfriend.virtuallove.splashAds;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.aigirlfriend.virtuallove.MainActivity;
import com.aigirlfriend.virtuallove.R;
import com.aigirlfriend.virtuallove.config.AdsConfig;
import com.ads.control.ads.AperoAd;
import com.ads.control.ads.AperoAdCallback;


public class PrivacyTermsActivity extends AppCompatActivity {
    private CheckBox firstCheck, secondCheck;
    private Button acceptButton;

    public class ApInterstitialAd {
        private final AppCompatActivity activity;
        private final String adId;

        public ApInterstitialAd(AppCompatActivity activity, String adId) {
            this.activity = activity;
            this.adId = adId;
        }

        public void showAds(AppCompatActivity activity, AperoAdCallback callback) {
            callback.onNextAction();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_terms);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorlight));
        }

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        firstCheck = findViewById(R.id.first_check);
        secondCheck = findViewById(R.id.second_check);
        acceptButton = findViewById(R.id.accept_button);
    }

    private void setupClickListeners() {
        acceptButton.setOnClickListener(v -> {
            if (!firstCheck.isChecked() || !secondCheck.isChecked()) {
                Toast.makeText(this, "Check above options to continue", Toast.LENGTH_SHORT).show();
                return;
            }
            showInterstitialAndProceed();
        });
    }


    private void showInterstitialAndProceed() {
        ApInterstitialAd interstitialAd = new ApInterstitialAd(this, AdsConfig.INTER_TURN_ON);
        interstitialAd.showAds(this, new AperoAdCallback() {
            @Override
            public void onNextAction() {
                super.onNextAction();
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
