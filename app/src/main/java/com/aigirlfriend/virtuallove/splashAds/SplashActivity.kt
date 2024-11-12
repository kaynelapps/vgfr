package com.aigirlfriend.virtuallove.splashAds
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aigirlfriend.virtuallove.R
import com.aigirlfriend.virtuallove.config.BuildConfig
import com.aigirlfriend.virtuallove.splashAds.LanguageActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import android.provider.Settings
import android.util.Log
import com.aigirlfriend.virtuallove.utils.MD5


class SplashActivity : AppCompatActivity() {
    private companion object {
        private const val TAG = "SplashActivity"
        private const val SPLASH_TIMEOUT = 5000L
        private const val LOADING_TIMEOUT = 2000L
        private const val PERMISSION_REQUEST_CODE = 123
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
        )
    }

    private var handler: Handler? = null
    private var isAdShown = false
    private var isTimeout = false
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (checkPermissions()) {
            initSplash()
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                initSplash()
            } else {
                finish()
            }
        }
    }

    private fun initSplash() {
        handler = Handler(Looper.getMainLooper())
        setupStatusBar()
        initSplashTimeout()
        loadAds()
    }

    private fun setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                statusBarColor = Color.TRANSPARENT
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
    }

    private fun initSplashTimeout() {
        handler?.postDelayed({
            isTimeout = true
            navigateNext()
        }, SPLASH_TIMEOUT)
    }
    private fun getTestDeviceId() {
        val android_id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val deviceId = MD5.calculate(android_id).toUpperCase()
        Log.d("TEST_DEVICE_ID", "Device ID: $deviceId")
    }


    private fun loadAds() {
        try {
            if (BuildConfig.DEBUG) {
                isAdShown = true
                navigateNext()
                return
            }

            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    loadBannerAd()
                    loadInterstitialAd()
                } catch (e: Exception) {
                    isAdShown = true
                    navigateNext()
                }
            }, 500)
        } catch (e: Exception) {
            isAdShown = true
            navigateNext()
        }
    }

    private fun loadBannerAd() {
        val bannerContainer = findViewById<FrameLayout>(R.id.banner_container)
        val adView = AdView(this)
        adView.adUnitId = BuildConfig.ads_banner_home
        adView.setAdSize(AdSize.BANNER)
        bannerContainer.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun loadInterstitialAd(retryAttempt: Int = 0) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, BuildConfig.ads_inter_splash, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Interstitial ad loaded successfully")
                    mInterstitialAd = interstitialAd
                    setupInterstitialCallback()
                    showInterstitialAd()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e(TAG, "Interstitial ad failed to load: ${loadAdError.message}")
                    Handler(Looper.getMainLooper()).postDelayed({
                        loadInterstitialAd(retryAttempt + 1)
                    }, 1000)
                }
            })
    }


    private fun setupInterstitialCallback() {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                mInterstitialAd = null
                isAdShown = true
                navigateNext()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                mInterstitialAd = null
                isAdShown = true
                navigateNext()
            }
        }
    }

    private fun showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            isAdShown = true
            navigateNext()
        }
    }

    private fun navigateNext() {
        if (isAdShown && isTimeout) {
            handler?.removeCallbacksAndMessages(null)
            startActivity(Intent(this, LanguageActivity::class.java))
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun onDestroy() {
        handler?.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
