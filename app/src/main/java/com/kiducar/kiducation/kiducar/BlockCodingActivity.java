package com.kiducar.kiducation.kiducar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.kiducar.kiducation.kiducar.blockcoding.Block;
import com.kiducar.kiducation.kiducar.blockcoding.ConditionBlock;
import com.kiducar.kiducation.kiducar.blockcoding.DistanceCheckBlock;
import com.kiducar.kiducation.kiducar.blockcoding.MoveBlock;
import com.kiducar.kiducation.kiducar.blockcoding.PageBlock;
import com.kiducar.kiducation.kiducar.blockcoding.RepeatBlock;
import com.kiducar.kiducation.kiducar.blockcoding.RotateBlock;
import com.kiducar.kiducation.kiducar.blockinterface.BlockItem;
import com.kiducar.kiducation.kiducar.blockinterface.BlockView;
import com.kiducar.kiducation.kiducar.blockinterface.BlockViewListAdapter;
import com.kiducar.kiducation.kiducar.design.DesignView;
import com.kiducar.kiducation.kiducar.design.ResourceManager;

// 블록코딩을 진행하는 액티비티
public class BlockCodingActivity extends AppCompatActivity {

    // 블록을 담고있는 리스트뷰
    private ListView blockListView1;
    private ListView blockListView2;
    private BlockViewListAdapter blockAdapter1;
    private BlockViewListAdapter blockAdapter2;

    // 블록을 넣을 소켓 레이아웃
    private FrameLayout blockSocket[];
    // 현재 넣을수 있는 소켓 번호(0~17);
    private int curBlockSocketNum;

    // Block 객체를 담을 페이지 블록
    private PageBlock pageBlock;

    // 반복 블록의 반복횟수를 임시로 저장할 변수
    private int tempRepeatNum;

    // PageBlock에서 위치를 임시로 저장할 변수
    private int tempPageIndex;

    // 인텐트 request code
    public static final int REQUEST_CODE_REPEATBLOCK = 1001;

    // 블록소켓, 화살표 디자인뷰
    DesignView blockSocketView[];
    DesignView downArrowView[];
    DesignView leftArrowView[];
    DesignView rightArrowView[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_coding);

        showDesign();
        createBlockList1();
        createBlockList2();
        curBlockSocketNum = 0;
        createBlockSocket();
    }

    // 화살표, 블록 소켓을 화면에 나타내는 함수
    public void showDesign(){
        blockSocketView = new DesignView[18];
        for(int i = 0; i < blockSocketView.length; i++) {
            blockSocketView[i] = (DesignView) findViewById(getResources().getIdentifier("blockSocketView"+(i+1), "id", "com.kiducar.kiducation.kiducar"));
            blockSocketView[i].setBitmap(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.BLOCKSOCKET));
        }
        downArrowView = new DesignView[2];
        for(int i = 0;i < downArrowView.length;i++){
            downArrowView[i] = (DesignView)findViewById(getResources().getIdentifier("downArrowView"+(i+1), "id", "com.kiducar.kiducation.kiducar"));
            downArrowView[i].setBitmap(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.DOWNARROW));
        }
        leftArrowView = new DesignView[5];
        for(int i = 0;i < leftArrowView.length;i++){
            leftArrowView[i] = (DesignView)findViewById(getResources().getIdentifier("leftArrowView"+(i+1), "id", "com.kiducar.kiducation.kiducar"));
            leftArrowView[i].setBitmap(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.LEFTARROW));
        }
        rightArrowView = new DesignView[10];
        for(int i = 0;i < rightArrowView.length;i++){
            rightArrowView[i] = (DesignView)findViewById(getResources().getIdentifier("rightArrowView"+(i+1), "id", "com.kiducar.kiducation.kiducar"));
            rightArrowView[i].setBitmap(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.RIGHTARROW));
        }
    }

    // 페이지에 블록 추가
    public void addBlockToPage(int blockType, int direction, int blockIndex){
        Block block = null;
        switch(blockType){
            case Block.MOVEBLOCK:
                block = new MoveBlock(direction);
                pageBlock.insertBlock(block);
                break;

            case Block.ROTATEBLOCK:
                block = new RotateBlock(direction);
                pageBlock.insertBlock(block);
                break;

            case Block.REPEATBLOCK: {
                block = new RepeatBlock(tempRepeatNum);
                pageBlock.insertBlock(block);
                repeatBlockDialogBox(blockIndex);
                break;
            }

            case Block.CONDITIONBLOCK:{
                break;
            }

            case Block.PAGEBLOCK: {
                break;
            }
        }
    }

    // 블록 타입에 대한 대화상자 생성 및 블록 반환
    public void repeatBlockDialogBox(int blockIndex){
        // 현재 반복 블록에 접근하기 위한 위치
        final int index = blockIndex;

        // 반복 블록 레이아웃 뷰 생성
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_repeatblock, null);

        // 반복블록 대화상자 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("반복 블록");
        builder.setIcon(R.drawable.img9);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setPositiveButton("확인", null);

        // 대화상자 생성 및 실행
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final AlertDialog alertDialog = (AlertDialog)dialog;

                // 대화상자에서 확인버튼을 눌렀을때 처리
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String repeatNumText = ((EditText)dialogView.findViewById(R.id.repeatNumEdit)).getText().toString();
                        // 반복 횟수를 입력안했으면 안내 메세지 출력
                        if(repeatNumText.isEmpty()){
                            Toast.makeText(getApplicationContext(), "반복 횟수를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // 반복횟수 설정 및 대화상자 종료
                            tempRepeatNum = Integer.parseInt(repeatNumText);
                            alertDialog.dismiss();
                        }
                    }
                });

                // 반복 페이지 버튼 클릭 처리, 새로운 블록코딩 생성
                Button repeatPageBtn = (Button)dialogView.findViewById(R.id.repeatPageBtn);

                repeatPageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pageIntent = new Intent(getApplicationContext(), BlockCodingActivity.class);
                        // 현재 반복 블록 페이지를 전송
                        tempPageIndex = index;
                        RepeatBlock repeatBlock = (RepeatBlock)pageBlock.getBlock(index);
                        if(repeatBlock != null)
                            pageIntent.putExtra("page", repeatBlock.getRepeatPage());
                        startActivityForResult(pageIntent, REQUEST_CODE_REPEATBLOCK);
                    }
                });
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 반복 블록의 페이지를 받아와 저장한다.
        if(requestCode == REQUEST_CODE_REPEATBLOCK){
            if(resultCode == RESULT_OK){
                PageBlock tempPageBlock = (PageBlock)data.getSerializableExtra("pageResult");
                ((RepeatBlock)pageBlock.getBlock(tempPageIndex)).setRepeatPage(tempPageBlock);
            }
        }
    }

    public Bitmap getBlockBitmap(int blockType, int direction){

        switch(blockType){
            case Block.MOVEBLOCK: {
                if (direction == MoveBlock.FORWARD)
                    return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.FORWARDBLOCK);
                else if (direction == MoveBlock.BACKWARD)
                    return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.BACKWORDBLOCK);
                break;
            }

            case Block.ROTATEBLOCK:{
                if(direction == RotateBlock.LEFT)
                    return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.LEFTROTATEBLOCK);
                else if(direction == RotateBlock.RIGHT)
                    return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.RIGHTROTATEBLOCK);
                break;
            }

            case Block.REPEATBLOCK:
                return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.REPEATBLOCK);

            case Block.CONDITIONBLOCK:
                return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.CONDITIONBLOCK);

            case Block.DISTANCECHECKBLOCK:
                return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.DISTANCECHECKBLOCK);

            case Block.PAGEBLOCK:
                return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.PAGEBLOCK);
        }

        return null;
    }

    // 블록 소켓을 생성
    public void createBlockSocket(){
        blockSocket = new FrameLayout[18];

        // PageBlock을 외부 액티비티에서 받아옴
        Intent intent = getIntent();
        pageBlock = (PageBlock)intent.getSerializableExtra("page");
        if(pageBlock == null)
            pageBlock = new PageBlock();
        else{
            for(int i = 0;i < pageBlock.getCurBlockNum();i++,curBlockSocketNum++){
                // 드래그 앤 드롭으로 넘어온 블록 객체와 같은 객체를 생성
                BlockView newBlock = new BlockView(getApplicationContext());

                int blockType = pageBlock.getBlock(i).getBlockType();
                int direction = -1;
                if(blockType == Block.MOVEBLOCK)
                    direction = ((MoveBlock)pageBlock.getBlock(i)).getDirection();
                else if(blockType == Block.ROTATEBLOCK)
                    direction = ((RotateBlock)pageBlock.getBlock(i)).getRotateDirection();

                newBlock.setBitmap(getBlockBitmap(blockType, direction));

                if(blockType == Block.MOVEBLOCK)
                    newBlock.setBlockType(blockType, direction);
                else if(blockType == Block.ROTATEBLOCK)
                    newBlock.setBlockType(blockType, direction);
                else
                    newBlock.setBlockType(blockType, direction);

                newBlock.setIsTouchanble(false);

                blockSocket[i] = (FrameLayout)findViewById(getResources().getIdentifier("blockSocket"+(curBlockSocketNum+1), "id", "com.kiducar.kiducation.kiducar"));
                // 소켓 레이아웃에 추가
                blockSocket[i].addView(newBlock);
            }
        }

        for(int i = 0;i < blockSocket.length;i++){
            // 드래그앤 드롭 리스너로 인덱스 번호를 넘겨줌
            final int blockSocketIndex = i;
            // 각 소켓의 프레임 레이아웃을 가져옴
            blockSocket[i] = (FrameLayout)findViewById(getResources().getIdentifier("blockSocket"+(i+1), "id", "com.kiducar.kiducation.kiducar"));
            // 드래그 앤 드롭 리스너를 설정
            blockSocket[i].setOnDragListener(new View.OnDragListener() {

                // 드래그 앤 드롭이 발생했을때 이벤트 처리
                public void eventHandle(FrameLayout frameLayout, BlockView blockView){

                    if(blockView != null) {
                        // 드래그 앤 드롭으로 넘어온 블록 객체와 같은 객체를 생성
                        BlockView newBlock = new BlockView(getApplicationContext());
                        newBlock.setBitmap(blockView.getBitmap());
                        newBlock.setBlockType(blockView.getBlockType(), blockView.getDirection());
                        newBlock.setIsTouchanble(false);

                        // 소켓 레이아웃에 추가
                        frameLayout.addView(newBlock);

                        // 페이지에 해당 블록 추가
                        addBlockToPage(blockView.getBlockType(), blockView.getDirection(), blockSocketIndex);

                        curBlockSocketNum++;
                    }
                }

                @Override
                public boolean onDrag(View v, DragEvent event) {
                    // 현재 넣을수 있는 소켓이면 이벤트 체크
                    if(blockSocketIndex == curBlockSocketNum) {
                        FrameLayout frameLayout;

                        // 뷰가 BlockView인지 확인
                        if (v instanceof FrameLayout)
                            frameLayout = (FrameLayout) v;
                        else
                            return false;

                        // 소켓 위에서 드롭되면 이벤트 처리
                        if(event.getAction() == DragEvent.ACTION_DROP){
                            eventHandle(frameLayout, (BlockView)event.getLocalState());
                        }
                    }

                    return true;
                }
            });
        }
    }

    // 블록 리스트를 생성
    protected void createBlockList1(){
        blockListView1 = (ListView)findViewById(R.id.blockListView1);

        blockAdapter1 = new BlockViewListAdapter(this);

        // 블록 아이템을 리스트에 추가함
        blockAdapter1.addItem(new BlockItem(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.FORWARDBLOCK), MoveBlock.MOVEBLOCK, MoveBlock.FORWARD));
        blockAdapter1.addItem(new BlockItem(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.BACKWORDBLOCK), MoveBlock.MOVEBLOCK, MoveBlock.BACKWARD));
        blockAdapter1.addItem(new BlockItem(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.LEFTROTATEBLOCK), RotateBlock.ROTATEBLOCK, RotateBlock.LEFT));
        blockAdapter1.addItem(new BlockItem(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.RIGHTROTATEBLOCK), RotateBlock.ROTATEBLOCK, RotateBlock.RIGHT));

        blockListView1.setAdapter(blockAdapter1);
    }

    // 블록 리스트를 생성
    protected void createBlockList2(){
        blockListView2 = (ListView)findViewById(R.id.blockListView2);

        blockAdapter2 = new BlockViewListAdapter(this);

        // 블록 아이템을 리스트에 추가함
        blockAdapter2.addItem(new BlockItem(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.REPEATBLOCK), RepeatBlock.REPEATBLOCK, -1));
        blockAdapter2.addItem(new BlockItem(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.CONDITIONBLOCK), ConditionBlock.CONDITIONBLOCK, -1));
        blockAdapter2.addItem(new BlockItem(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.DISTANCECHECKBLOCK), DistanceCheckBlock.DISTANCECHECKBLOCK, -1));
        blockAdapter2.addItem(new BlockItem(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.PAGEBLOCK), PageBlock.PAGEBLOCK, -1));

        blockListView2.setAdapter(blockAdapter2);
    }

    @Override
    public void onBackPressed() {
        // 결과를 호출한 액티비티에 전송
        Intent resultIntent = new Intent();
        resultIntent.putExtra("pageResult", pageBlock);
        setResult(RESULT_OK, resultIntent);
        finish();
        super.onBackPressed();
    }
}
