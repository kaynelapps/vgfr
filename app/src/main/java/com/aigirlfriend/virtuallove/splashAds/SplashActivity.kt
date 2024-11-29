package com.aigirlfriend.virtuallove.splashAds

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.ImageView
import android.widget.FrameLayout
import android.view.View
import com.aigirlfriend.virtuallove.R
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.ads.control.ads.wrapper.ApAdError
import com.ads.control.admob.Admob
import com.aigirlfriend.virtuallove.config.BuildConfig
import com.aigirlfriend.virtuallove.ads.MyApplication

class SplashActivity : AppCompatActivity() {
    private var check = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = getColor(R.color.colorlight)
        }


        setupTestModeDetection()
        MyApplication.setuser_balance(0)
        refreshItem()
    }

    private fun setupTestModeDetection() {
        val rootView = findViewById<View>(android.R.id.content)
        rootView.setOnClickListener {
            if ((application as MyApplication).incrementSplashTapCount()) {
                android.widget.Toast.makeText(this, "Test Mode Activated", android.widget.Toast.LENGTH_SHORT).show()
                refreshItem()
            }
        }
    }




    private fun refreshItem() {
        if (isNetworkConnected()) {
            Handler(Looper.getMainLooper()).postDelayed({
                AperoAd.getInstance().loadSplashInterstitialAds(
                    this,
                    BuildConfig.ads_inter_splash,
                    60000L,
                    30000L,
                    object : AperoAdCallback() {
                        override fun onNextAction() {
                            goNext(1)
                        }
                    }
                )
            }, 2000)
        } else {
            showNoInternetDialog()
        }
    }


    private fun goNext(i: Int) {
        check += 1
        if (check == 1) {
            loadOpenApp()
        }
    }

    private fun loadOpenApp() {
        val intent = Intent(this@SplashActivity, LanguageActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnected == true
    }

    private fun showNoInternetDialog() {
        val dialog = Dialog(this, R.style.DialogTheme).apply {
            setContentView(R.layout.no_internet)
            setCancelable(false)
        }

        val imgRefresh = dialog.findViewById<CardView>(R.id.refresh)
        val imgExit = dialog.findViewById<CardView>(R.id.exit)
        val imgNoInternet = dialog.findViewById<ImageView>(R.id.img)

        val animShake = AnimationUtils.loadAnimation(this, R.anim.shake)
        imgNoInternet.startAnimation(animShake)

        imgNoInternet.setOnClickListener {
            imgNoInternet.startAnimation(animShake)
        }

        imgRefresh.setOnClickListener {
            dialog.dismiss()
            refreshItem()
        }

        imgExit.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }
}
