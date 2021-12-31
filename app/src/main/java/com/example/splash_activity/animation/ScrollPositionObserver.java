package com.example.splash_activity.animation;

import android.content.Context;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.core.widget.NestedScrollView;

import com.example.splash_activity.R;

public class ScrollPositionObserver implements ViewTreeObserver.OnScrollChangedListener  {
    private int mImageViewHeight;
    NestedScrollView nestedScrollView;
    ImageView imageView;
    Context context;

    public ScrollPositionObserver(Context context, NestedScrollView nestedScrollView, ImageView imageView) {
        this.context = context;
        this.nestedScrollView = nestedScrollView;
        this.imageView = imageView;
        mImageViewHeight = context.getResources().getDimensionPixelSize(R.dimen.contact_photo_height);
    }

    @Override
    public void onScrollChanged() {
        int scrollY = Math.min(Math.max(nestedScrollView.getScrollY(), 0), mImageViewHeight);

        // changing position of ImageView
        imageView.setTranslationY(scrollY / 2);

        // alpha you could set to ActionBar background
        float alpha = scrollY / (float) mImageViewHeight;
    }
}
