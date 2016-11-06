package com.kiducar.kiducation.kiducar.blockcoding;

// 프로그램의 시작을 알리는 블록

public class StartBlock extends Block {
    // 생성자
    public StartBlock(int inPageNum, int inBlockNum){
        super(inPageNum, inBlockNum, STARTBLOCK);
    }

    // 추상 메소드 구현. 중간 코드 생성
    public int[] makeIntermediateCode(){
        int[] data = {m_inPageNum, m_inBlockNum, m_blockType};

        return data;
    }
}
