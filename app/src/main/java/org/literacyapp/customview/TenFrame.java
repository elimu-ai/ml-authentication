package org.literacyapp.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import org.literacyapp.R;

/**
 * Created by adrian on 27.10.16.
 */

public class TenFrame extends View {

    private final String picturePath;
    private boolean interactive;
    private int count;
    private Paint paint;
    private Drawable drawable;

    public TenFrame(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TenFrame,
                0, 0);

        try {
            interactive = a.getBoolean(R.styleable.TenFrame_interactive, false);
            count = a.getInteger(R.styleable.TenFrame_count, 0);
            picturePath = a.getString(R.styleable.TenFrame_picture);
        } finally {
            a.recycle();
        }

        init();
    }


    private void init() {
        paint = new Paint();
        drawable = new Coin();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(getWidth()/2, 0, getWidth()/2, getHeight(), paint);
        canvas.drawLine(0, (float) (getHeight()*0.2), getWidth(), (float) (getHeight()*0.2), paint);
        canvas.drawLine(0, (float) (getHeight()*0.4), getWidth(), (float) (getHeight()*0.4), paint);
        canvas.drawLine(0, (float) (getHeight()*0.6), getWidth(), (float) (getHeight()*0.6), paint);
        canvas.drawLine(0, (float) (getHeight()*0.8), getWidth(), (float) (getHeight()*0.8), paint);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        drawable.setBounds(0, 0, getWidth()/2, (int) (getHeight()*0.2));
        drawable.draw(canvas);
    }
}

