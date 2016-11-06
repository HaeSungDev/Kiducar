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
    public ConditionBlock(int inPageNum, int inBlockNum, PageBlock okPage, PageBlock noPage) {
        super(inPageNum, inBlockNum, CONDITIONBLOCK);

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
        int[] conditionData = {m_inPageNum, m_inBlockNum, m_blockType};
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
