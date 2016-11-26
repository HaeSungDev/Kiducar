package com.kiducar.kiducation.kiducar.blockcoding;

// 자동차가 앞뒤 이동 블록 코드
public class MoveBlock extends Block{

    // m_direction의 종류 상수 선언
    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;

    // 이동 방향
    protected int m_direction;

    // 생성자
    public MoveBlock(int direction){
        super(MOVEBLOCK);
        m_direction = direction;
    }

    // 추상 메소드 구현. 중간 코드 생성
    public int[] makeIntermediateCode(){
        int[] data = {m_blockType, m_direction};

        return data;
    }
}
