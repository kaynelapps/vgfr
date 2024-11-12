package com.aigirlfriend.virtuallove.gallery;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import androidx.viewpager.widget.ViewPager;

public class GalleryViewPager extends ViewPager {
    private DisplayMetrics displayMetrics;

    public GalleryViewPager(Context context) {
        super(context);
        init();
    }

    public GalleryViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        this.displayMetrics = getContext().getResources().getDisplayMetrics();
    }

    @Override
    public void onMeasure(int i, int i2) {
        Log.d("TAG", "onMeasure: " + this.displayMetrics.widthPixels);
        Log.d("TAG", "onMeasure: -2147483648");
        Log.d("TAG", "onMeasure: " + (getPageMargin() * 8));
        super.onMeasure(MeasureSpec.makeMeasureSpec(this.displayMetrics.widthPixels - (getPageMargin() * 8), Integer.MIN_VALUE), i2);
    }
}
