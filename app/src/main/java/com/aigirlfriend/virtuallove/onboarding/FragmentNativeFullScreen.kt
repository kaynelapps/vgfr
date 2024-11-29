package com.aigirlfriend.virtuallove.onboarding

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApNativeAd
import com.aigirlfriend.virtuallove.R
import com.aigirlfriend.virtuallove.config.BuildConfig
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.nativead.NativeAdView

class NativeFullscreenActivity : AppCompatActivity() {

    private var tapCount = 0
    private val handler = Handler(Looper.getMainLooper())
    private val resetTapRunnable = Runnable { tapCount = 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.native_fullscreen)

        findViewById<View>(android.R.id.content).setOnClickListener {
            tapCount++
            handler.removeCallbacks(resetTapRunnable)
            if (tapCount >= 5) {
                AperoAd.getInstance().isShowMessageTester = true
                Toast.makeText(this, "Test mode enabled!", Toast.LENGTH_SHORT).show()
                tapCount = 0
            } else {
                handler.postDelayed(resetTapRunnable, 2000)
            }
        }

        findViewById<View>(R.id.icClose).setOnClickListener {
            startActivity(Intent(this, OnboardingScreen4Activity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        loadNativeAd()
    }

    private fun loadNativeAd() {
        val adContainer = findViewById<NativeAdView>(R.id.frAds)
        val shimmerContainer = findViewById<ShimmerFrameLayout>(R.id.shimmerContainerNative)

        shimmerContainer?.let { safeShimmer ->
            safeShimmer.startShimmer()

            AperoAd.getInstance().loadNativeAd(
                this,
                BuildConfig.ads_native_onboard3_high,
                R.layout.native_ad_template,
                adContainer,
                safeShimmer,
                object : AperoAdCallback() {
                    override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                        safeShimmer.stopShimmer()
                        safeShimmer.visibility = View.GONE
                        Log.d("NativeAd", "Ad loaded successfully")
                    }

                    override fun onAdFailedToLoad(error: ApAdError?) {
                        safeShimmer.stopShimmer()
                        safeShimmer.visibility = View.GONE
                        Log.d("NativeAd", "Failed to load: ${error?.message}")
                    }

                    override fun onNextAction() {
                        safeShimmer.stopShimmer()
                        safeShimmer.visibility = View.GONE
                    }
                }
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, OnboardingScreen3Activity::class.java))
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
