package com.aigirlfriend.virtuallove.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import java.security.MessageDigest;

public class CornerTransform implements Transformation<Bitmap> {
    private boolean exceptLeftBottom;
    private boolean exceptLeftTop;
    private boolean exceptRightBotoom;
    private boolean exceptRightTop;
    private BitmapPool mBitmapPool;
    private float radius;

    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }

    public void setExceptCorner(boolean z, boolean z2, boolean z3, boolean z4) {
        this.exceptLeftTop = z;
        this.exceptRightTop = z2;
        this.exceptLeftBottom = z3;
        this.exceptRightBotoom = z4;
    }

    public CornerTransform(Context context, float f) {
        this.mBitmapPool = Glide.get(context).getBitmapPool();
        this.radius = f;
    }

    @Override
    public Resource<Bitmap> transform(Context context, Resource<Bitmap> resource, int i, int i2) {
        int i3;
        int i4;
        Bitmap bitmap = resource.get();
        if (i > i2) {
            float f = (float) i2;
            float f2 = (float) i;
            i4 = bitmap.getWidth();
            i3 = (int) (((float) bitmap.getWidth()) * (f / f2));
            if (i3 > bitmap.getHeight()) {
                i3 = bitmap.getHeight();
                i4 = (int) (((float) bitmap.getHeight()) * (f2 / f));
            }
        } else if (i < i2) {
            float f3 = (float) i;
            float f4 = (float) i2;
            int height = bitmap.getHeight();
            int height2 = (int) (((float) bitmap.getHeight()) * (f3 / f4));
            if (height2 > bitmap.getWidth()) {
                i4 = bitmap.getWidth();
                i3 = (int) (((float) bitmap.getWidth()) * (f4 / f3));
            } else {
                int i5 = height2;
                i3 = height;
                i4 = i5;
            }
        } else {
            i4 = bitmap.getHeight();
            i3 = i4;
        }
        this.radius *= (float) (i3 / i2);
        Bitmap bitmap2 = this.mBitmapPool.get(i4, i3, Bitmap.Config.ARGB_8888);
        if (bitmap2 == null) {
            bitmap2 = Bitmap.createBitmap(i4, i3, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint();
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        int width = (bitmap.getWidth() - i4) / 2;
        int height3 = (bitmap.getHeight() - i3) / 2;
        if (!(width == 0 && height3 == 0)) {
            Matrix matrix = new Matrix();
            matrix.setTranslate((float) (-width), (float) (-height3));
            bitmapShader.setLocalMatrix(matrix);
        }
        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight());
        float f5 = this.radius;
        canvas.drawRoundRect(rectF, f5, f5, paint);
        if (this.exceptLeftTop) {
            float f6 = this.radius;
            canvas.drawRect(0.0f, 0.0f, f6, f6, paint);
        }
        if (this.exceptRightTop) {
            float f7 = this.radius;
            canvas.drawRect(((float) canvas.getWidth()) - f7, 0.0f, f7, f7, paint);
        }
        if (this.exceptLeftBottom) {
            float f8 = this.radius;
            canvas.drawRect(0.0f, ((float) canvas.getHeight()) - f8, f8, (float) canvas.getHeight(), paint);
        }
        if (this.exceptRightBotoom) {
            canvas.drawRect(((float) canvas.getWidth()) - this.radius, ((float) canvas.getHeight()) - this.radius, (float) canvas.getWidth(), (float) canvas.getHeight(), paint);
        }
        return BitmapResource.obtain(bitmap2, this.mBitmapPool);
    }
}
