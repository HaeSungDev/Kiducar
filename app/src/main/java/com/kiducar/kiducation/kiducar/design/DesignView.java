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

public class DesignView extends View {

    private Bitmap m_bitmap;

    public DesignView(Context context) {
        super(context);
    }

    public DesignView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DesignView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DesignView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setBitmap(Bitmap bitmap){
        m_bitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(m_bitmap != null) {
            int w = m_bitmap.getWidth();
            int h = m_bitmap.getHeight();

            Rect src = new Rect(0, 0, w, h);
            Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());

            canvas.drawBitmap(m_bitmap, src, dst, null);
        }
    }
}
