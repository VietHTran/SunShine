package com.example.android.sunshine.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * Created by Viet on 11/17/2016.
 */
public class MyView extends View {
    Canvas canvas;

    void sendAccessibility(Context context) {
        AccessibilityManager accessibilityManager =
                (AccessibilityManager) context.getSystemService(
                        Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager.isEnabled()) {
            sendAccessibilityEvent(
                    AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        }
    }
    public MyView(Context context) {
        super(context);
        sendAccessibility(context);
    }
    public MyView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        sendAccessibility(context);
    }
    public MyView(Context context, AttributeSet attributeSet, int defaultStyle) {
        super(context,attributeSet,defaultStyle);
        sendAccessibility(context);
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
        super.onDraw(canvas);
        if (this.canvas==null) {
            this.canvas= new Canvas();
            this.canvas.drawCircle(50.0f,50.0f,10.0f,new Paint(Paint.ANTI_ALIAS_FLAG));
        }
        canvas=this.canvas;
    }
    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        //Action
        return true;
    }
}
