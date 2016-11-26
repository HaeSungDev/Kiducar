package com.kiducar.kiducation.kiducar.blockcoding;

// 블록 추상 클래스. 모든 블록들이 상속받는 클래스
abstract public class Block {

    // blockType 종류 상수 선언
    public static final int MOVEBLOCK = 0;
    public static final int ROTATEBLOCK = 1;
    public static final int REPEATBLOCK = 2;
    public static final int CONDITIONBLOCK = 3;
    public static final int DISTANCECHECKBLOCK = 4;
    public static final int PAGEBLOCK = 5;

    // 해당 블록의 종류
    protected int m_blockType;

    // 생성자
    public Block(int blockType){
        m_blockType = blockType;
    }

    // 추상 메소드. 블록의 중간 코드를 생성. 자식 클래스에서 구현
    public abstract int[] makeIntermediateCode();
}