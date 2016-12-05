package com.kiducar.kiducation.kiducar.blockinterface;

import com.kiducar.kiducation.kiducar.blockcoding.Block;
import com.kiducar.kiducation.kiducar.blockcoding.ConditionBlock;
import com.kiducar.kiducation.kiducar.blockcoding.PageBlock;
import com.kiducar.kiducation.kiducar.blockcoding.RepeatBlock;
import com.kiducar.kiducation.kiducar.bluetooth.BluetoothHandler;

// 키듀카 실행 모듈(싱글톤 패턴)
public class ExecuteModule {

    // 키듀카 시작 명령
    public static final int STARTCODE = 100;
    public static final int ENDCODE = 101;
    // 키듀카 중지 명령
    public static final int STOPCODE = 102;
    // 키듀카 종료 명령
    public static final int EXITCODE = 103;

    // 분석해서 실행할 메인 블록 페이지
    private PageBlock m_mainPageBlock;
    // 분석한 코드를 전송할 블루투스 핸들러
    private BluetoothHandler m_bluetoothHandler;
    private static ExecuteModule m_executeModule;

    // 블록코딩 액티비티의 첫 페이지가 열려있는지 체크하는 변수
    boolean m_isFirstPageOpen;

    private ExecuteModule(){
        m_mainPageBlock = null;
        m_bluetoothHandler = null;
        m_isFirstPageOpen = false;
    }

    // ExecuteMoudle 객체를 얻음
    public static ExecuteModule getInstance(){
        if(m_executeModule == null){
            m_executeModule = new ExecuteModule();
        }

        return m_executeModule;
    }

    public void setIsFirstPageOpen(boolean isFirstPageOpen){
        m_isFirstPageOpen = isFirstPageOpen;
    }

    public boolean getIsFirstPageOpen(){
        return m_isFirstPageOpen;
    }

    // 블루투스 핸들러 설정
    public void setBluetoothHandler(BluetoothHandler bluetoothHandler){
        m_bluetoothHandler = bluetoothHandler;
    }
    // 블루투스 핸들러 반환
    public BluetoothHandler getBluetoothHandler(){
        return m_bluetoothHandler;
    }

    // 메인 페이지 블록 설정
    public void setMainPageBlock(PageBlock mainPageBlock){
        m_mainPageBlock = mainPageBlock;
    }
    // 메인 페이지 블록 반환
    public PageBlock getMainPageBlock(){
        return m_mainPageBlock;
    }

    public byte[] makeByteData(int[] data){
        byte byteData[] = new byte[data.length*4];

        for(int i = 0;i < data.length;i++) {
            // int형 데이터를 byte에 나눠 넣음(자바는 빅 엔디안 방식으로 저장해서 역순으로 저장)
            byteData[i*4+3] = (byte) (data[i] >> 24);
            byteData[i*4+2] = (byte) (data[i] >> 16);
            byteData[i*4+1] = (byte) (data[i] >> 8);
            byteData[i*4] = (byte) (data[i]);
        }

        return byteData;
    }

    // PageBlock에 있는 코드들을 순서대로 전송함
    public void sendBlockCode(PageBlock pageBlock){
        if(pageBlock != null) {
            for (int i = 0; i < pageBlock.getCurBlockNum(); i++) {
                Block block = pageBlock.getBlock(i);
                switch (block.getBlockType()) {

                    case Block.MOVEBLOCK:
                    case Block.ROTATEBLOCK:
                    case Block.STOPBLOCK:
                        m_bluetoothHandler.sendData(makeByteData(block.makeIntermediateCode()));
                        break;

                    case Block.REPEATBLOCK:
                        m_bluetoothHandler.sendData(makeByteData(block.makeIntermediateCode()));
                        sendBlockCode(((RepeatBlock) block).getRepeatPage());
                        break;

                    case Block.CONDITIONBLOCK:
                        m_bluetoothHandler.sendData(makeByteData(block.makeIntermediateCode()));
                        m_bluetoothHandler.sendData(makeByteData(((ConditionBlock) block).getCheckBlock().makeIntermediateCode()));
                        sendBlockCode(((ConditionBlock) block).getOkPage());
                        sendBlockCode(((ConditionBlock) block).getNoPage());
                        break;

                    case Block.PAGEBLOCK:
                        sendBlockCode((PageBlock) block);
                        break;
                }
            }
        }
    }

    // 코드 실행
    public void startExecute(){
        if(m_bluetoothHandler != null){
            int[] startCode = {STARTCODE};
            m_bluetoothHandler.sendData(makeByteData(startCode));

            sendBlockCode(m_mainPageBlock);

            int[] endCode = {ENDCODE};
            m_bluetoothHandler.sendData(makeByteData(endCode));
        }
    }

    // 코드 중지
    public void stopExecute(){
        if(m_bluetoothHandler != null){
            int[] stopCode = {STOPCODE};
            m_bluetoothHandler.sendData(makeByteData(stopCode));
        }
    }

    // 키듀카 종료
    public void exitCar(){
        if(m_bluetoothHandler != null){
            int[] exitCode = {EXITCODE};
            m_bluetoothHandler.sendData(makeByteData(exitCode));
        }
    }
}