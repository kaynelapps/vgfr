package com.aigirlfriend.virtuallove.onboarding

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.nativead.NativeAdView
import com.aigirlfriend.virtuallove.R
import com.aigirlfriend.virtuallove.adapter.ImageCarouselAdapter
import com.aigirlfriend.virtuallove.config.BuildConfig
import pl.droidsonroids.gif.GifImageView
import kotlin.math.abs
import android.widget.FrameLayout





class OnboardingScreen3Activity : AppCompatActivity() {
    private lateinit var imageViewPager: ViewPager2
    private lateinit var nextButton: TextView
    private lateinit var imageAdapter: ImageCarouselAdapter
    private lateinit var dotIndicators: List<View>
    private lateinit var gifImageView: GifImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_screen_3)

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

        // Rest of your existing onCreate code
        gifImageView = findViewById(R.id.swipeGif)
        imageViewPager = findViewById(R.id.imageViewPager)
        nextButton = findViewById(R.id.nextButton)

        findViewById<View>(R.id.dot_1).alpha = 0.4f
        findViewById<View>(R.id.dot_2).apply {
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.rectangle_width)
            background = getDrawable(R.drawable.rectangle_on)
            alpha = 1f
        }
        findViewById<View>(R.id.dot_3).alpha = 0.4f
        findViewById<View>(R.id.dot_4).alpha = 0.4f

        setupImageCarousel()
        setupNextButton()
        loadNativeAd()
        setupSwipeNavigation()
    }





    private fun loadNativeAd() {
        val adContainer = findViewById<FrameLayout>(R.id.nativeAdContainer)
        val shimmerContainer = findViewById<ShimmerFrameLayout>(R.id.shimmerContainer)

        AperoAd.getInstance().loadNativeAd(
            this,
            BuildConfig.ads_native_onboard3,
            R.layout.native_ad_template,
            adContainer,
            shimmerContainer,
            object : AperoAdCallback() {}
        )
    }



    private fun setupNextButton() {
        nextButton.setOnClickListener {
            val intent = Intent(this, NativeFullscreenActivity::class.java)
            startActivity(intent)
            finish()
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

                    if (abs(diffX) > 100) {
                        if (diffX > 0) {
                            val intent = Intent(this@OnboardingScreen3Activity, NativeFullscreenActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }





    private fun setupImageCarousel() {
        imageAdapter = ImageCarouselAdapter(listOf(
            R.drawable.onboarding_image_2,
            R.drawable.onboarding_image_9,
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
