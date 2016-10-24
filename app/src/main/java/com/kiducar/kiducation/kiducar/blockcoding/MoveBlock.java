package com.kiducar.kiducation.kiducar.blockcoding;

public class MoveBlock extends Block{

    // m_direction의 종류 상수 선언
    static final int FORWARD = 0;
    static final int BACKWARD = 1;

    // 이동 방향
    private int m_direction;

    // 생성자
    public MoveBlock(int pageNum, int blockNum, int blockType, int direction){
        super(pageNum, blockNum, blockType);
        m_direction = direction;
    }

    // 추상 메소드 구현. 중간 코드 생성
    public byte[] makeIntermediateCode(){
        int[] data = {m_pageNum, m_blockNum, m_blockType, m_direction};
        byte[] intermediateCode = new byte[16];

        // 데이터들을 byte형 배열에 집어넣음
        for(int i = 0;i < intermediateCode.length;i++){
            intermediateCode[i] = (byte)(data[i/4] >> (3-(i%4))*8);
        }

        return intermediateCode;
    }
}
