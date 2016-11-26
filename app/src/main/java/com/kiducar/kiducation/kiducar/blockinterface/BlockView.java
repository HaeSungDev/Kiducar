package com.kiducar.kiducation.kiducar.blockinterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

// 화면에 표시할 블록 뷰
// 각 블록들이 상속받아서 사용
public class BlockView extends View {

    // 블록 그림
    private int m_resDrawable;
    // 블록 종류
    private int m_blockType;
    // 방향있는 블록의 방향
    private int m_direction;

    public BlockView(Context context) {
        super(context);
        m_resDrawable = 0;
        m_blockType = -1;
        m_direction = -1;
    }

    public BlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_resDrawable = 0;
        m_blockType = -1;
        m_direction = -1;
    }

    public BlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_resDrawable = 0;
        m_blockType = -1;
        m_direction = -1;
    }

    public BlockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        m_resDrawable = 0;
        m_blockType = -1;
        m_direction = -1;
    }

    public void setResDrawable(int resDrawable) {
        m_resDrawable = resDrawable;
        invalidate();
    }

    public int getResDrawable(){
        return m_resDrawable;
    }

    public void setBlockType(int blockType, int direction){
        m_blockType = blockType;
        m_direction = direction;
    }

    public int getBlockType(){
        return m_blockType;
    }
    public int getDirection(){
        return m_direction;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(m_resDrawable != 0) {
            Bitmap socketBitmap = BitmapFactory.decodeResource(getContext().getResources(), m_resDrawable);

            int w = socketBitmap.getWidth();
            int h = socketBitmap.getHeight();

            int[] pixels = new int[w * h];
            socketBitmap.getPixels(pixels, 0, w, 0, 0, w, h);
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] == Color.RED) {
                    pixels[i] = Color.TRANSPARENT;
                }
            }

            Bitmap printBitmap = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_8888);

            Rect src = new Rect(0, 0, w, h);
            Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());

            canvas.drawBitmap(printBitmap, src, dst, null);
        }
    }
}
