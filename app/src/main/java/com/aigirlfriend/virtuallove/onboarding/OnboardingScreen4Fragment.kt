package com.aigirlfriend.virtuallove.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.aigirlfriend.virtuallove.R
import com.aigirlfriend.virtuallove.adapter.ImageCarouselAdapter
import com.aigirlfriend.virtuallove.splashAds.PrivacyTermsActivity
import android.util.Log
import android.widget.FrameLayout
import com.aigirlfriend.virtuallove.config.AdsConfig
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.ads.wrapper.ApNativeAd
import com.facebook.shimmer.ShimmerFrameLayout
import com.aigirlfriend.virtuallove.config.BuildConfig
import android.view.View
import android.os.Handler
import android.os.Looper

class OnboardingScreen4Activity : AppCompatActivity() {
    private lateinit var imageViewPager: ViewPager2
    private lateinit var getStartedButton: TextView
    private lateinit var imageAdapter: ImageCarouselAdapter
    private lateinit var dotIndicators: List<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_screen_4)

        imageViewPager = findViewById(R.id.imageViewPager)
        getStartedButton = findViewById(R.id.getStartedButton)

        // Set first three dots grey
        findViewById<View>(R.id.dot_1).alpha = 0.4f
        findViewById<View>(R.id.dot_2).alpha = 0.4f
        findViewById<View>(R.id.dot_3).alpha = 0.4f

        // Set last dot as rectangle (pink)
        findViewById<View>(R.id.dot_4).apply {
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.rectangle_width)
            background = getDrawable(R.drawable.rectangle_on)
            alpha = 1f
        }

        setupImageCarousel()
        setupGetStartedButton()
        loadNativeAd()
    }


    private fun loadNativeAd() {
        val adContainer = findViewById<FrameLayout>(R.id.nativeAdContainer)
        val shimmerContainer = findViewById<ShimmerFrameLayout>(R.id.shimmerContainer)

        if (BuildConfig.DEBUG) {
            shimmerContainer.visibility = View.GONE
            return
        }

        AperoAd.getInstance().loadNativeAd(
            this,
            AdsConfig.NATIVE_HOME,
            R.layout.native_ad_template,
            adContainer,
            shimmerContainer,
            object : AperoAdCallback() {
                override fun onNativeAdLoaded(nativeAd: ApNativeAd) {
                    AperoAd.getInstance().populateNativeAdView(
                        this@OnboardingScreen4Activity,
                        nativeAd,
                        adContainer,
                        shimmerContainer
                    )
                }

                override fun onAdFailedToLoad(error: ApAdError?) {
                    shimmerContainer.visibility = View.GONE
                }
            }
        )
    }

    private fun setupGetStartedButton() {
        getStartedButton.isClickable = true
        getStartedButton.isFocusable = true
        getStartedButton.setOnClickListener {
            startActivity(Intent(this, PrivacyTermsActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun setupImageCarousel() {
        imageAdapter = ImageCarouselAdapter(listOf(
            R.drawable.onboarding_image_7,
            R.drawable.onboarding_image_6,
        ))
        imageViewPager.adapter = imageAdapter
        startAutoSlide()
    }

    private fun startAutoSlide() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                val currentItem = imageViewPager.currentItem
                val nextItem = if (currentItem == imageAdapter.itemCount - 1) 0 else currentItem + 1
                imageViewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000)
            }
        }
        handler.postDelayed(runnable, 3000)
    }
}
