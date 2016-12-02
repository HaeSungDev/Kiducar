package com.kiducar.kiducation.kiducar.blockcoding;

// 블록들을 반복해주기 위한 블록

public class RepeatBlock extends Block {

    // 반복할 페이지
    protected PageBlock m_repeatPage;
    // 반복 횟수
    protected int m_repeatNum;

    public RepeatBlock() {
        super(REPEATBLOCK);

        // 반복 페이지 초기화
        m_repeatPage = null;
        // 반복 횟수 초기화
        m_repeatNum = 0;
    }

    // 반복 횟수 설정
    public void setRepeatNum(int repeatNum) {
        m_repeatNum = repeatNum;
    }

    // 반복 횟수 반환
    public int getRepeatNum(){
        return m_repeatNum;
    }

    // 반복 페이지 설정
    public void setRepeatPage(PageBlock repeatPage){
        m_repeatPage = repeatPage;
    }

    // 반복 페이지 반환
    public PageBlock getRepeatPage(){
        return m_repeatPage;
    }

    // 추상 메소드 구현. 중간 코드 생성
    public int[] makeIntermediateCode(){
        int repeatPageBlockNum = m_repeatPage.getCurBlockNum();

        int[] data = {m_blockType, m_repeatNum, repeatPageBlockNum};

        return data;
    }
}