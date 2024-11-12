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
    }

    private fun loadNativeAd() {
        val adContainer = findViewById<FrameLayout>(R.id.nativeAdContainer)
        val shimmerContainer = findViewById<ShimmerFrameLayout>(R.id.shimmerContainer)

        if (BuildConfig.DEBUG) {
            shimmerContainer.visibility = View.GONE
            return
        }

        val adLoader = AdLoader.Builder(this, AdsConfig.NATIVE_HOME)
            .forNativeAd { nativeAd ->
                val adView = layoutInflater.inflate(R.layout.native_ad_template, null) as NativeAdView

                adView.headlineView = adView.findViewById(R.id.ad_headline)
                adView.mediaView = adView.findViewById(R.id.ad_media)
                adView.bodyView = adView.findViewById(R.id.ad_body)
                adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
                adView.iconView = adView.findViewById(R.id.ad_app_icon)
                adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

                (adView.headlineView as TextView).text = nativeAd.headline
                (adView.bodyView as TextView).text = nativeAd.body
                (adView.callToActionView as Button).text = nativeAd.callToAction

                nativeAd.mediaContent?.let { adView.mediaView?.setMediaContent(it) }

                nativeAd.icon?.let {
                    (adView.iconView as ImageView).setImageDrawable(it.drawable)
                    adView.iconView?.visibility = View.VISIBLE
                }

                nativeAd.advertiser?.let {
                    (adView.advertiserView as TextView).text = it
                    adView.advertiserView?.visibility = View.VISIBLE
                }

                adView.setNativeAd(nativeAd)

                shimmerContainer.visibility = View.GONE
                adContainer.removeAllViews()
                adContainer.addView(adView)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    shimmerContainer.visibility = View.GONE
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
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

    override fun onDestroy() {
        adLoader = null
        super.onDestroy()
    }
}
