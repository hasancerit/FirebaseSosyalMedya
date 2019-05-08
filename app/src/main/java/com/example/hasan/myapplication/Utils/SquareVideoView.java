package com.example.hasan.myapplication.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.VideoView;

public class SquareVideoView extends VideoView {
    public SquareVideoView(final Context context) {
        super(context);
    }

    public SquareVideoView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareVideoView(final Context context, final AttributeSet attrs,
                           final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int measuredWidth = getScreenWidth();

        setMeasuredDimension(measuredWidth,measuredWidth);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


}
