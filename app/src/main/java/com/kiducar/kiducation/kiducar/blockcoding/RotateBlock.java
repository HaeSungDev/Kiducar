package com.kiducar.kiducation.kiducar.blockcoding;

public class RotateBlock extends Block {
    // m_rotateDirection의 종류 상수 선언
    static final int FORWARD = 0;
    static final int BACKWARD = 1;

    // 회전 방향
    private int m_rotateDirection;

    // 생성자
    public RotateBlock(int pageNum, int blockNum, int blockType, int rotateDirection){
        super(pageNum, blockNum, blockType);
        m_rotateDirection = rotateDirection;
    }

    // 추상 메소드 구현. 중간 코드 생성
    public byte[] makeIntermediateCode(){
        int[] data = {m_pageNum, m_blockNum, m_blockType, m_rotateDirection};
        byte[] intermediateCode = new byte[16];

        // 데이터들을 byte형 배열에 집어넣음
        for(int i = 0;i < intermediateCode.length;i++){
            intermediateCode[i] = (byte)(data[i/4] >> (3-(i%4))*8);
        }

        return intermediateCode;
    }
}
