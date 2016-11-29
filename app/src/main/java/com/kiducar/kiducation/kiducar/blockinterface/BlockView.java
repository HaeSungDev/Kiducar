package com.kiducar.kiducation.kiducar.blockinterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

// 화면에 표시할 블록 뷰
// 각 블록들이 상속받아서 사용
public class BlockView extends View {

    // 블록 그림
    private Bitmap m_bitmap;
    // 블록 종류
    private int m_blockType;
    // 방향있는 블록의 방향
    private int m_direction;
    // 터치 가능 상태인지 체크
    private boolean m_isTouchable;

    public BlockView(Context context) {
        super(context);
        m_blockType = -1;
        m_direction = -1;
        m_isTouchable = true;
    }

    public BlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_blockType = -1;
        m_direction = -1;
        m_isTouchable = true;
    }

    public BlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        m_blockType = -1;
        m_direction = -1;
        m_isTouchable = true;
    }

    public BlockView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        m_blockType = -1;
        m_direction = -1;
        m_isTouchable = true;
    }

    public void setBitmap(Bitmap resDrawable) {
        m_bitmap = resDrawable;
        invalidate();
    }

    public void setIsTouchanble(boolean isTouchable){
        m_isTouchable = isTouchable;
    }

    public Bitmap getBitmap(){
        return m_bitmap;
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

        if(m_bitmap != null) {

            int w = m_bitmap.getWidth();
            int h = m_bitmap.getHeight();

            Rect src = new Rect(0, 0, w, h);
            Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());

            canvas.drawBitmap(m_bitmap, src, dst, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(m_isTouchable) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                this.startDrag(null, new View.DragShadowBuilder(this), this, 0);
            }
        }

        return super.onTouchEvent(event);
    }
}
