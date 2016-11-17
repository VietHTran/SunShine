package com.example.android.sunshine.app;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Viet on 11/17/2016.
 */
public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }
    public MyView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }
    public MyView(Context context, AttributeSet attributeSet, int defaultStyle) {
        super(context,attributeSet,defaultStyle);
    }
    @Override
    protected void onMeasure(int width, int height) {
        int hMode=MeasureSpec.getMode(height);
        int hSize=MeasureSpec.getSize(height);
        int wMode=MeasureSpec.getMode(width);
        int wSize=MeasureSpec.getSize(width);
        int myHeight=hSize;
        int myWidth=wSize;
        setMeasuredDimension(myWidth,myHeight);
    }
    @Override
    protected void onDraw(Canvas canvas) {

    }
}
