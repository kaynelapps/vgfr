package com.aigirlfriend.virtuallove.splashAds

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aigirlfriend.virtuallove.R
import com.aigirlfriend.virtuallove.model.LanguageModel
import com.aigirlfriend.virtuallove.utils.LocaleHelper
import com.aigirlfriend.virtuallove.onboarding.OnboardingScreen1Activity
import com.facebook.shimmer.ShimmerFrameLayout
import java.util.Locale
import com.aigirlfriend.virtuallove.adapter.LanguageAdapter
import com.aigirlfriend.virtuallove.config.AdsConfig
import com.aigirlfriend.virtuallove.config.BuildConfig
import com.aigirlfriend.virtuallove.BaseApp
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import android.content.SharedPreferences
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity


class LanguageActivity : AppCompatActivity() {
    private lateinit var rvLanguages: RecyclerView
    private lateinit var languageAdapter: LanguageAdapter
    private lateinit var btnContinue: Button
    private lateinit var nativeAdContainer: FrameLayout
    private lateinit var shimmerLayout: ShimmerFrameLayout
    private lateinit var shimmerNative: ShimmerFrameLayout
    private var adLoader: AdLoader? = null

    override fun attachBaseContext(newBase: Context) {
        val languageCode = newBase.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
            .getString("language", "en_US") ?: "en_US"
        LocaleHelper.setLocale(newBase, languageCode)
        super.attachBaseContext(newBase)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(this@LanguageActivity, R.string.please_select_language, Toast.LENGTH_SHORT).show()
            }
        })

        initViews()
        setupLanguageList()
        loadShimmerEffect()
        loadNativeAd()
    }

    private fun initViews() {
        rvLanguages = findViewById(R.id.rvLanguages)
        btnContinue = findViewById(R.id.btnContinue)
        shimmerLayout = findViewById(R.id.shimmerLayout)
        nativeAdContainer = findViewById(R.id.nativeAdContainer)
        shimmerNative = findViewById(R.id.shimmerNative)

        btnContinue.setOnClickListener {
            languageAdapter.getSelectedLanguage()?.let { language ->
                saveLanguagePreference(language)
                startOnboardingScreen1Fragment()
            } ?: run {
                Toast.makeText(this, R.string.please_select_language, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupLanguageList() {
        languageAdapter = LanguageAdapter(getLanguageList(), object : LanguageAdapter.OnLanguageSelectedListener {
            override fun onLanguageSelected(position: Int) {
                btnContinue.isEnabled = true
                btnContinue.alpha = 1.0f
            }
        })

        rvLanguages.apply {
            layoutManager = LinearLayoutManager(this@LanguageActivity)
            setHasFixedSize(true)
            adapter = languageAdapter
        }
    }

    private fun getLanguageList(): List<LanguageModel> {
        val languages = mutableListOf<LanguageModel>()
        val currentLanguage = Locale.getDefault().language

        languages.apply {
            add(LanguageModel("Français", "fr", R.drawable.ic_french))
            add(LanguageModel("English (US)", "en_US", R.drawable.ic_usa))
            add(LanguageModel("हिंदी", "hi", R.drawable.ic_india))
            add(LanguageModel("Español", "es", R.drawable.ic_spanish))
            add(LanguageModel("中文", "zh", R.drawable.ic_china))
            add(LanguageModel("Português", "pt", R.drawable.ic_portugal))
            add(LanguageModel("Русский", "ru", R.drawable.ic_russia))
            add(LanguageModel("Bahasa Indonesia", "in", R.drawable.ic_indonesia))
            add(LanguageModel("Filipino", "fil", R.drawable.ic_philippines))
            add(LanguageModel("বাংলা", "bn", R.drawable.ic_bangladesh))
            add(LanguageModel("Português (BR)", "pt_BR", R.drawable.ic_brazil))
            add(LanguageModel("Afrikaans", "af", R.drawable.ic_south_africa))
            add(LanguageModel("Deutsch", "de", R.drawable.ic_german))
            add(LanguageModel("English (CA)", "en_CA", R.drawable.ic_canada))
            add(LanguageModel("English (UK)", "en_GB", R.drawable.ic_english))
            add(LanguageModel("한국어", "ko", R.drawable.ic_korean))
            add(LanguageModel("Nederlands", "nl", R.drawable.ic_netherlands))
        }

        languages.find { it.code == currentLanguage }?.isSelected = true
        return languages
    }

    private fun loadShimmerEffect() {
        shimmerLayout.visibility = View.VISIBLE
        rvLanguages.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            shimmerLayout.visibility = View.GONE
            rvLanguages.visibility = View.VISIBLE
        }, 1500)
    }

    private fun loadNativeAd() {
        if (BuildConfig.DEBUG) {
            shimmerNative.visibility = View.GONE
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

                shimmerNative.visibility = View.GONE
                nativeAdContainer.removeAllViews()
                nativeAdContainer.addView(adView)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    shimmerNative.visibility = View.GONE
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

    private fun saveLanguagePreference(languageCode: String) {
        getSharedPreferences("AppSettings", MODE_PRIVATE).edit().apply {
            putString("language", languageCode)
            putBoolean("language_selected", true)
            apply()
        }

        // Update locale and recreate activity to apply changes
        LocaleHelper.setLocale(this, languageCode)
        updateConfiguration(languageCode)
    }

    private fun recreateActivity() {
        val newIntent = getIntent()
        finish()
        startActivity(newIntent)
        overridePendingTransition(0, 0)
    }

    private fun startOnboardingScreen1Fragment() {
        Intent(this, OnboardingScreen1Activity::class.java).also { intent ->
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun updateConfiguration(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Recreate activity to apply changes
        recreate()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, R.string.please_select_language, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        adLoader = null
        super.onDestroy()
    }
}