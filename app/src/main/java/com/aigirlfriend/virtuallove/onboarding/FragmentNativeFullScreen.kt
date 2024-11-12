package com.aigirlfriend.virtuallove.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.aigirlfriend.virtuallove.R
import com.aigirlfriend.virtuallove.config.AdsConfig
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.facebook.shimmer.ShimmerFrameLayout

class FragmentNativeFullScreen : Fragment() {
    private var onAdClosedListener: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.native_fullscreen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adContainer = view.findViewById<FrameLayout>(R.id.frAds)
        val shimmerLayout = view.findViewById<ShimmerFrameLayout>(R.id.shimmerContainerNative)

        AperoAd.getInstance().loadNativeAd(
            requireActivity(),
            AdsConfig.NATIVE_HOME,
            R.layout.native_fullscreen,
            adContainer,
            shimmerLayout,
            object : AperoAdCallback() {
                override fun onAdClosed() {
                    onAdClosedListener?.invoke()
                }
            }
        )

        view.findViewById<ImageView>(R.id.icClose).setOnClickListener {
            onAdClosedListener?.invoke()
        }
    }

    fun setOnAdClosedListener(listener: () -> Unit) {
        onAdClosedListener = listener
    }
}
