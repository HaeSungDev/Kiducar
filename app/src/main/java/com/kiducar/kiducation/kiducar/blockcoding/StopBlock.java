package com.kiducar.kiducation.kiducar.blockcoding;

// 자동차 일정 시간 정지 블록
public class StopBlock extends Block {

    // 생성자
    public StopBlock(){
        super(STOPBLOCK);
    }

    // 추상 메소드 구현. 중간 코드 생성
    public int[] makeIntermediateCode(){
        int[] data = {m_blockType};

        return data;
    }
}
