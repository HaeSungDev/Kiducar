package com.kiducar.kiducation.kiducar.blockcoding;

// 블록의 조건을 주기 위한 블록

public class ConditionBlock extends Block {

    // 조건이 맞으면 실행하는 페이지
    protected PageBlock m_okPage;
    // 조건이 틀리면 실행하는 페이지
    protected PageBlock m_noPage;

    // 조건 체크 블록
    protected Block m_checkBlock;

    // 생성자
    public ConditionBlock(PageBlock okPage, PageBlock noPage) {
        super(CONDITIONBLOCK);

        // ok, no 페이지 초기화
        m_okPage = okPage;
        m_noPage = noPage;

        // 조건 체크 블록 초기화
        m_checkBlock = null;
    }

    // ok 페이지 반환
    PageBlock getOkPage() {
        return m_okPage;
    }

    // no 페이지 반환
    PageBlock getNoPage() {
        return m_noPage;
    }

    // 체크 블록 설정
    public void setBlock(Block checkBlock){
        m_checkBlock = checkBlock;
    }

    // 추상 메소드 구현. 중간 코드 생성
    public int[] makeIntermediateCode(){
        int okPageNum = m_okPage.getCurBlockNum();
        int noPageNum = m_noPage.getCurBlockNum();

        int[] conditionData = {m_blockType, okPageNum, noPageNum};
        int[] checkBlockData = m_checkBlock.makeIntermediateCode();

        int data[] = new int[conditionData.length+checkBlockData.length];

        for(int i = 0;i < conditionData.length;i++){
            data[i] = conditionData[i];
        }
        for(int i = conditionData.length;i < data.length;i++){
            data[i] = checkBlockData[i-conditionData.length];
        }

        return data;
    }
}
