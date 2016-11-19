package com.kiducar.kiducation.kiducar.blockcoding;

// 자동차 좌우 회전 블록 코드
public class RotateBlock extends Block {
    // m_rotateDirection의 종류 상수 선언
    static final int RIGHT = 0;
    static final int LEFT = 1;

    // 회전 방향
    protected int m_rotateDirection;

    // 생성자
    public RotateBlock(int rotateDirection){
        super(ROTATEBLOCK);
        m_rotateDirection = rotateDirection;
    }

    // 추상 메소드 구현. 중간 코드 생성
    public int[] makeIntermediateCode(){
        int[] data = {m_blockType, m_rotateDirection};

        return data;
    }
}
