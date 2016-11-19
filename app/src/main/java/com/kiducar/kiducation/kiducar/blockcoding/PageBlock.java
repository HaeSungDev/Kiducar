package com.kiducar.kiducation.kiducar.blockcoding;

// 블록들을 담기 위한 PageBlock. 논리적인 연결 구조를 위한 블록
public class PageBlock extends Block {

    // Block 최대 개수
    final int BLOCKMAXNUM = 18;

    // Page에 Block들을 담을 배열
    protected Block[] m_blockInPage;
    // 현재 Page에 담고 있는 블럭 개수
    protected int m_curBlockNum;

    // 생성자
    public PageBlock(){
        super(PAGEBLOCK);

        // Block 배열 생성
        m_blockInPage = new Block[BLOCKMAXNUM];
        // 초기화
        m_curBlockNum = 0;
    }

    // 해당 위치에 해당하는 블록 반환
    public Block getBlock(int blockNum){
        if(blockNum < 0 || blockNum >= m_curBlockNum){
            return null;
        }

        return m_blockInPage[blockNum];
    }

    // 페이지에 블록을 집어 넣음
    public boolean insertBlock(Block block){
        if(m_curBlockNum >= BLOCKMAXNUM)
            return false;

        m_blockInPage[m_curBlockNum] = block;
        m_curBlockNum++;

        return true;
    }

    // 해당 위치에 해당하는 블록을 제거함
    public boolean deleteBlock(int blockNum){
        if(m_curBlockNum <= 0 || m_curBlockNum <= blockNum)
            return false;

        // 현재 블록 개수를 줄임
        m_curBlockNum--;
        // 지우는 블록의 뒤에 있는 블록들을 앞으로 땡겨서 지워줌.
        for(int i = blockNum;i < m_curBlockNum;i++) {
            m_blockInPage[i] = m_blockInPage[i+1];
        }
        m_blockInPage[m_curBlockNum] = null;

        return true;
    }

    // 현재 블록 개수를 반환
    public int getCurBlockNum() {
        return m_curBlockNum;
    }

    // PageBlock은 중간 코드를 생성하지 않음. 블록들의 논리적인 연결 구조를 위한 블록
    public int[] makeIntermediateCode(){
        return null;
    }
}