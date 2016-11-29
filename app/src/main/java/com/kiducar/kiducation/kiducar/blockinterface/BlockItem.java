package com.kiducar.kiducation.kiducar.blockinterface;

import android.graphics.Bitmap;

/**
 * Created by HaeSung on 2016-11-26.
 */

// 블록 리스트의 블록뷰에 전달할 데이터를 저장하는 클래스
public class BlockItem {
    // 블록 그림
    public Bitmap m_bitmap;
    // 블록 종류
    public int m_blockType;
    // 방향있는 블록의 방향
    public int m_direction;

    public BlockItem(Bitmap bitmap, int blockType, int direction){
        m_bitmap = bitmap;
        m_blockType = blockType;
        m_direction = direction;
    }
}
