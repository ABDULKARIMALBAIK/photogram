package com.abdulkarimalbaik.dev.photogram.Utils;

import android.content.Context;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

class MyScroller extends Scroller{

    public MyScroller(Context context) {

        super(context , new DecelerateInterpolator());
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy , 400);
    }
}
