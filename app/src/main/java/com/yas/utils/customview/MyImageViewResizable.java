package com.yas.utils.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.support.v7.widget.AppCompatImageView;


public class MyImageViewResizable extends AppCompatImageView{
    public MyImageViewResizable(Context context) {
        super(context);
    }

    public MyImageViewResizable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {


        int widthMode = MeasureSpec.getMode(widthSpec);
        int widthSize = MeasureSpec.getSize(widthSpec);
        int heightMode = MeasureSpec.getMode(heightSpec);
        int heightSize = MeasureSpec.getSize(heightSpec);

        int width=widthSize;
        int height=(widthSize*9)/16;


        setMeasuredDimension(width, height);
        heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, heightSpec);
    }
}
