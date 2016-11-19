package com.kiducar.kiducation.kiducar.blockcoding;

public class DistanceCheckBlock extends Block {

    // 장애물과의 거리
    protected int m_distance;

    // 생성자
    public DistanceCheckBlock(){
        super(DISTANCECHECKBLOCK);

        // 거리 초기화
        m_distance = 0;
    }

    // 거리 설정
    public void setDistance(int distance){
        m_distance = distance;
    }

    // 추상 메소드 구현. 중간 코드 생성
    public int[] makeIntermediateCode(){
        int[] data = {m_blockType, m_distance};

        return data;
    }
}
