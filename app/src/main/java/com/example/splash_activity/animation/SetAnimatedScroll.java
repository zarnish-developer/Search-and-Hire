package com.example.splash_activity.animation;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.ImageView;

import androidx.core.widget.NestedScrollView;

import com.example.splash_activity.AnimateScroll;

public class SetAnimatedScroll implements AnimateScroll {
    Context context;
    NestedScrollView nestedScrollView;
    ImageView imageView;

    public SetAnimatedScroll(Context context, NestedScrollView nestedScrollView, ImageView imageView) {
        this.context = context;
        this.nestedScrollView = nestedScrollView;
        this.imageView = imageView;
    }


    @Override
    public void ListenScroll() {
        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver(context, nestedScrollView, imageView));
        new CountDownTimer(2000, 20) {

            public void onTick(long millisUntilFinished) {
                nestedScrollView.scrollTo((int) (2000 - millisUntilFinished), 0);
            }

            public void onFinish() {

            }
        }.start();
    }

}
