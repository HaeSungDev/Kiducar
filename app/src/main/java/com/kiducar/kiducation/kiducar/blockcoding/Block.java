package com.kiducar.kiducation.kiducar.blockcoding;

// 블록 추상 클래스. 모든 블록들이 상속받는 클래스
abstract public class Block {

    // blockType 종류 상수 선언
    static final int MOVEBLOCK = 0;

    // 해당 블록이 위치하는 페이지 번호
    protected int m_pageNum;
    // 해당 블록이 위치하는 페이지 번호에서 블록의 위치
    protected int m_blockNum;
    // 해당 블록의 종류
    protected int m_blockType;

    // 생성자
    public Block(int pageNum, int blockNum, int blockType){
        m_pageNum = pageNum;
        m_blockNum = blockNum;
        m_blockType = blockType;
    }

    // 추상 메소드. 블록의 중간 코드를 생성. 자식 클래스에서 구현
    public abstract byte[] makeIntermediateCode();
}
