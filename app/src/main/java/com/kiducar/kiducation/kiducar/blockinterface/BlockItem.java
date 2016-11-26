package com.kiducar.kiducation.kiducar.blockinterface;

/**
 * Created by HaeSung on 2016-11-26.
 */

public class BlockItem {
    // 블록 그림
    public int m_resDrawable;
    // 블록 종류
    public int m_blockType;
    // 방향있는 블록의 방향
    public int m_direction;

    public BlockItem(int resDrawable, int blockType, int direction){
        m_resDrawable = resDrawable;
        m_blockType = blockType;
        m_direction = direction;
    }
}
