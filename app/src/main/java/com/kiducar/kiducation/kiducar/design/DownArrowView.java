package com.kiducar.kiducation.kiducar.design;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.kiducar.kiducation.kiducar.R;

public class DownArrowView extends View {
    public DownArrowView(Context context) {
        super(context);
        invalidate();
    }

    public DownArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        invalidate();
    }

    public DownArrowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        invalidate();
    }

    public DownArrowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap socketBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.downarrow);

        int w = socketBitmap.getWidth();
        int h = socketBitmap.getHeight();

        int[] pixels = new int[w*h];
        socketBitmap.getPixels(pixels, 0, w, 0, 0, w, h);
        for(int i = 0;i < pixels.length;i++)
        {
            if(pixels[i] == Color.RED)
            {
                pixels[i] = Color.TRANSPARENT;
            }
        }

        Bitmap printBitmap = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_8888);

        Rect src = new Rect(0, 0, w, h);
        Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());

        canvas.drawBitmap(printBitmap, src, dst, null);
    }
}
