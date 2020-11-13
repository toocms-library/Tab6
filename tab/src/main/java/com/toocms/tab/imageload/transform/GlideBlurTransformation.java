package com.toocms.tab.imageload.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;

import java.security.MessageDigest;

/**
 * Author：Zero
 * Date：2018/8/30 17:58
 */
public class GlideBlurTransformation extends BitmapTransformation {

    private final String ID = getClass().getName();

    private static int MAX_RADIUS = 25;
    private static int DEFAULT_SAMPLING = 1;

    private Context context;
    private int radius; //模糊半径0～25
    private int sampling; //取样0～25

    public GlideBlurTransformation(Context context) {
        this(context, MAX_RADIUS, DEFAULT_SAMPLING);
    }

    public GlideBlurTransformation(Context context, int radius) {
        this(context, radius, DEFAULT_SAMPLING);
    }

    public GlideBlurTransformation(Context context, int radius, int sampling) {
        this.context = context;
        this.radius = radius > MAX_RADIUS ? MAX_RADIUS : radius;
        this.sampling = sampling > MAX_RADIUS ? MAX_RADIUS : sampling;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        int scaledWidth = width / sampling;
        int scaledHeight = height / sampling;

        Bitmap bitmap = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.scale(1 / (float) sampling, 1 / (float) sampling);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(toTransform, 0, 0, paint);
        return ImageUtils.fastBlur(bitmap, 0.1f, radius);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GlideBlurTransformation) {
            GlideBlurTransformation other = (GlideBlurTransformation) obj;
            return radius == other.radius && sampling == other.sampling;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(), Util.hashCode(radius, Util.hashCode(sampling)));
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + radius * 10 + sampling).getBytes(CHARSET));
    }
}
