package com.aigirlfriend.virtuallove.gallery;

import android.view.View;
import androidx.viewpager.widget.ViewPager;

public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_ALPHA = 0.5f;
    private static final float MIN_SCALE = 0.8f;

    @Override
    public void transformPage(View view, float f) {
        view.setScaleY(1.0f);
        view.setAlpha(1.0f);
    }
}
