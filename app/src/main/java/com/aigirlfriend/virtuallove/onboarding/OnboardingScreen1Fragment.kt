package com.aigirlfriend.virtuallove.onboarding
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aigirlfriend.virtuallove.R
import com.aigirlfriend.virtuallove.config.AdsConfig
import com.aigirlfriend.virtuallove.config.BuildConfig
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.aigirlfriend.virtuallove.onboarding.OnboardingScreen2Activity
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import android.view.MotionEvent
import kotlin.math.abs
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApNativeAd
import com.ads.control.ads.wrapper.ApAdError




class OnboardingScreen1Activity : AppCompatActivity() {
    private lateinit var nextButton: ImageButton
    private lateinit var responseGroup: RadioGroup
    private var adLoader: AdLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_screen_1)

        nextButton = findViewById(R.id.nextButton)
        responseGroup = findViewById(R.id.responseGroup)

        setupButtons()
        loadNativeAd()
        setupSwipeNavigation()

        // Add tap detection for test mode
        val rootView = findViewById<View>(android.R.id.content)
        var tapCount = 0
        rootView.setOnClickListener {
            tapCount++
            if (tapCount >= 5) {
                AperoAd.getInstance().isShowMessageTester = true
                tapCount = 0
            }
        }
    }


    private fun loadNativeAd() {
        val adContainer = findViewById<FrameLayout>(R.id.nativeAdContainer)
        val shimmerContainer = findViewById<ShimmerFrameLayout>(R.id.shimmerContainer)

        AperoAd.getInstance().loadNativeAd(
            this,
            BuildConfig.ads_native_onboard1_high,
            R.layout.native_ad_template,
            adContainer,
            shimmerContainer,
            object : AperoAdCallback() {}
        )
    }





    private fun setupButtons() {
        responseGroup.setOnCheckedChangeListener { _, _ ->
            nextButton.alpha = 1.0f
        }

        nextButton.setOnClickListener {
            startActivity(Intent(this, OnboardingScreen2Activity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
    private fun setupSwipeNavigation() {
        val rootView = findViewById<View>(android.R.id.content)
        var startX = 0f

        rootView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    true
                }
                MotionEvent.ACTION_UP -> {
                    val endX = event.x
                    val diffX = startX - endX

                    if (abs(diffX) > 100) { // Minimum swipe distance
                        if (diffX > 0) { // Swipe left
                            startActivity(Intent(this, OnboardingScreen2Activity::class.java))
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }


    override fun onDestroy() {
        adLoader = null
        super.onDestroy()
    }
}
