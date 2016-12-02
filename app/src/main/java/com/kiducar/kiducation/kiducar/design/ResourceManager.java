package com.kiducar.kiducation.kiducar.design;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kiducar.kiducation.kiducar.R;

// 리소스를 관리하는 객체, 싱글톤 패턴 사용
public class ResourceManager {

    // 순서대로 비트맵 상수 지정
    public interface BITMAP {
        int BLOCKSOCKET = 0, DOWNARROW = 1, LEFTARROW = 2, RIGHTARROW = 3, FORWARDBLOCK = 4, BACKWORDBLOCK = 5, LEFTROTATEBLOCK = 6;
        int RIGHTROTATEBLOCK = 7, STOPBLOCK = 8, REPEATBLOCK = 9, CONDITIONBLOCK = 10, DISTANCECHECKBLOCK = 11, PAGEBLOCK = 12, TITLE = 13;
    }

    // ResourceManager 클래스의 단 하나의 객체, 함수를 통해 접근가능
    private static ResourceManager m_resourceManager;

    public static ResourceManager getInstance(Resources resources){
        if(m_resourceManager == null)
            m_resourceManager = new ResourceManager(resources);

        return m_resourceManager;
    }

    // 소켓 및 화살표 이미지
    private Bitmap m_bitmap[];
    // 리소스
    private Resources m_resources;

    private ResourceManager(Resources resources){
        m_resources = resources;
        m_bitmap = new Bitmap[14];

        for(int i = 0;i < m_bitmap.length;i++){
            m_bitmap[i] = BitmapFactory.decodeResource(m_resources, m_resources.getIdentifier("img"+(i+1), "drawable", "com.kiducar.kiducation.kiducar"));
        }
    }

    // 비트맵 객체를 얻음
    public Bitmap getBitmap(int bitmapKind){
        if(bitmapKind < 0 || bitmapKind > m_bitmap.length)
            return null;

        return m_bitmap[bitmapKind];
    }
}
