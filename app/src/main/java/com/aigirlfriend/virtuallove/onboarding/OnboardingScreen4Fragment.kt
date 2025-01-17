package com.aigirlfriend.virtuallove.onboarding

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.facebook.shimmer.ShimmerFrameLayout
import com.aigirlfriend.virtuallove.R
import com.aigirlfriend.virtuallove.adapter.ImageCarouselAdapter
import com.aigirlfriend.virtuallove.config.BuildConfig
import com.aigirlfriend.virtuallove.splashAds.PrivacyTermsActivity
import kotlin.math.abs

class OnboardingScreen4Activity : AppCompatActivity() {
    private lateinit var imageViewPager: ViewPager2
    private lateinit var getStartedButton: TextView
    private lateinit var imageAdapter: ImageCarouselAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_screen_4)

        val rootView = findViewById<View>(android.R.id.content)
        var tapCount = 0
        rootView.setOnClickListener {
            tapCount++
            if (tapCount >= 5) {
                AperoAd.getInstance().isShowMessageTester = true
                tapCount = 0
            }
        }

        imageViewPager = findViewById(R.id.imageViewPager)
        getStartedButton = findViewById(R.id.getStartedButton)

        findViewById<View>(R.id.dot_1).alpha = 0.4f
        findViewById<View>(R.id.dot_2).alpha = 0.4f
        findViewById<View>(R.id.dot_3).alpha = 0.4f
        findViewById<View>(R.id.dot_4).apply {
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.rectangle_width)
            background = getDrawable(R.drawable.rectangle_on)
            alpha = 1f
        }

        setupImageCarousel()
        setupGetStartedButton()
        loadNativeAd()
        setupSwipeNavigation()
    }
    private fun loadNativeAd() {
        val adContainer = findViewById<FrameLayout>(R.id.nativeAdContainer)
        val shimmerContainer = findViewById<ShimmerFrameLayout>(R.id.shimmerContainer)

        AperoAd.getInstance().loadNativeAd(
            this,
            BuildConfig.ads_native_onboard4,
            R.layout.native_ad_template,
            adContainer,
            shimmerContainer,
            object : AperoAdCallback() {}
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
            R.drawable.onboarding_image_6
        ))
        imageViewPager.adapter = imageAdapter
        startAutoSlide()
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

                    if (abs(diffX) > 100) {
                        if (diffX > 0) {
                            startActivity(Intent(this, PrivacyTermsActivity::class.java))
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                    }
                    true
                }
                else -> false
            }
        }
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
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, OnboardingScreen3Activity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }


}