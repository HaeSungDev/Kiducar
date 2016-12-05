package com.kiducar.kiducation.kiducar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.kiducar.kiducation.kiducar.blockcoding.StopBlock;
import com.kiducar.kiducation.kiducar.blockinterface.BlockItem;
import com.kiducar.kiducation.kiducar.blockinterface.BlockView;
import com.kiducar.kiducation.kiducar.blockinterface.BlockViewListAdapter;
import com.kiducar.kiducation.kiducar.blockinterface.ExecuteModule;
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

    // PageBlock에서 위치를 임시로 저장할 변수
    private int tempPageIndex;

    // 인텐트 request code
    public static final int REQUEST_CODE_REPEATBLOCK = 1001;
    public static final int REQUEST_CODE_CONDITIONBLOCK_OK = 1002;
    public static final int REQUEST_CODE_CONDITIONBLOCK_NO = 1003;
    public static final int REQUEST_CODE_PAGEBLOCK = 1004;

    // 블록소켓, 화살표, 제목 디자인뷰
    DesignView blockSocketView[];
    DesignView downArrowView[];
    DesignView leftArrowView[];
    DesignView rightArrowView[];
    DesignView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_coding);

        showDesign();
        createBlockList1();
        createBlockList2();
        curBlockSocketNum = 0;
        createBlockSocket();

        // 해당 액티비티가 첫 페이지라면 실행, 중지 버튼을 보이게 한다.
        if(!ExecuteModule.getInstance().getIsFirstPageOpen()){
            findViewById(R.id.startExecuteBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.startExecuteBtn).setClickable(true);
            findViewById(R.id.stopExecuteBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.startExecuteBtn).setClickable(true);

            // 해당 액티비티가 첫 블록코딩 페이지라면 실행 모듈의 메인페이지에 페이지블록을 설정함
            ExecuteModule.getInstance().setMainPageBlock(pageBlock);

            ExecuteModule.getInstance().setIsFirstPageOpen(true);
        }
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
        titleView = (DesignView)findViewById(R.id.titleView);
        titleView.setBitmap(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.TITLE));
    }

    // 블록 소켓에 들어간 블록 클릭 이벤트에서 실행할 함수
    public void blockClickEvent(int blockType, int blockIndex){
        switch(blockType){
            case Block.MOVEBLOCK:
            case Block.ROTATEBLOCK:
            case Block.STOPBLOCK:
                blockDialogBox(blockIndex);
                break;

            case Block.REPEATBLOCK:
                repeatBlockDialogBox(blockIndex);
                break;

            case Block.CONDITIONBLOCK:
                conditionBlockDialogBox(blockIndex);
                break;

            case Block.PAGEBLOCK:
                pageBlockDialogBox(blockIndex);
                break;
        }
    }

    // 페이지에 새로운 블록 추가
    public void addBlockToPage(int blockType, int direction, int blockIndex, boolean isChange){
        Block block = null;
        switch(blockType){
            case Block.MOVEBLOCK:
                block = new MoveBlock(direction);
                if(isChange)
                    pageBlock.setBlock(block, blockIndex);
                else
                    pageBlock.insertBlock(block);
                break;

            case Block.ROTATEBLOCK:
                block = new RotateBlock(direction);
                if(isChange)
                    pageBlock.setBlock(block, blockIndex);
                else
                    pageBlock.insertBlock(block);
                break;

            case Block.STOPBLOCK:
                block = new StopBlock();
                if(isChange)
                    pageBlock.setBlock(block, blockIndex);
                else
                    pageBlock.insertBlock(block);
                break;

            case Block.REPEATBLOCK:
                block = new RepeatBlock();
                if(isChange)
                    pageBlock.setBlock(block, blockIndex);
                else
                    pageBlock.insertBlock(block);
                repeatBlockDialogBox(blockIndex);
                break;

            case Block.CONDITIONBLOCK:
                block = new ConditionBlock();
                if(isChange)
                    pageBlock.setBlock(block, blockIndex);
                else
                    pageBlock.insertBlock(block);
                conditionBlockDialogBox(blockIndex);
                break;

            case Block.PAGEBLOCK:
                block = new PageBlock();
                if(isChange)
                    pageBlock.setBlock(block, blockIndex);
                else
                    pageBlock.insertBlock(block);
                pageBlockDialogBox(blockIndex);
                break;
        }
    }

    // 위치에 해당하는 블록 제거
    public void deleteBlock(int blockIndex){
        // 현재 위치의 소켓에서 블록뷰를 제거

        blockSocket[blockIndex].removeViewAt(1);
        // 다음 위치 소켓 블록에서 앞으로 하나씩 옮겨줌
        BlockView moveBlockView;
        curBlockSocketNum--;
        for(int i = blockIndex;i < curBlockSocketNum;i++) {
            moveBlockView = (BlockView) blockSocket[i + 1].getChildAt(1);
            // index 번호가 바뀌었으므로 클릭 이벤트 재설정
            final int blockType = moveBlockView.getBlockType();
            final int newIndex = i;
            moveBlockView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blockClickEvent(blockType, newIndex);
                }
            });
            blockSocket[i+1].removeView(moveBlockView);
            blockSocket[i].addView(moveBlockView);
        }
        // 현재 블록 위치를 지우고 뒤에있는 블록들을 끌어넣음
        pageBlock.deleteBlock(blockIndex);
    }

    // 일반 블록에 대한 처리를 해줄 대화상자
    public void blockDialogBox(int blockIndex){
        // 현재 반복 블록에 접근하기 위한 위치
        final int index = blockIndex;

        // 반복 블록 레이아웃 뷰 생성
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_repeatblock, null);

        // 반복블록 대화상자 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블록을 삭제하시겠습니까?");
        builder.setCancelable(false);
        // 확인 버튼 등록 및 클릭 이벤트 생성
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBlock(index);
            }
        });
        builder.setNegativeButton("취소", null);
        AlertDialog dialog = builder.create();

        // 대화상자를 화면에 표시
        dialog.show();
    }

    // 반복 블록에 대한 처리를 해줄 대화상자
    public void repeatBlockDialogBox(int blockIndex){
        // 현재 반복 블록에 접근하기 위한 위치
        final int index = blockIndex;

        // 반복 블록 레이아웃 뷰 생성
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_repeatblock, null);

        // 반복블록 대화상자 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("반복 블록");
        builder.setIcon(R.drawable.img10);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setPositiveButton("확인", null);
        builder.setNegativeButton("삭제", null);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            // 반복 횟수 에디트텍스트
            private EditText repeatNumEdit;
            // 확인 버튼
            private Button positiveButton;
            // 삭제 버튼
            private Button negativeButton;
            // 반복 페이지 버튼
            private Button repeatPageBtn;
            // 현재 반복 블록
            private RepeatBlock repeatBlock;
            // 현재 대화상자
            private AlertDialog alertDialog;

            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog = (AlertDialog)dialog;

                repeatBlock = ((RepeatBlock)pageBlock.getBlock(index));

                repeatNumEdit = (EditText)dialogView.findViewById(R.id.repeatNumEdit);
                repeatNumEdit.setText(Integer.toString(repeatBlock.getRepeatNum()));

                // 대화상자에서 확인버튼을 눌렀을때 처리
                positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int repeatNum = Integer.parseInt(repeatNumEdit.getText().toString());
                        // 반복 횟수를 입력안했으면 안내 메세지 출력
                        if(repeatNum <= 0){
                            Toast.makeText(getApplicationContext(), "반복 횟수를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // 반복횟수 설정 및 대화상자 종료
                            repeatBlock.setRepeatNum(repeatNum);
                            alertDialog.dismiss();
                        }
                    }
                });

                // 대화상자에서 삭제버튼을 눌렀을때 처리
                negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteBlock(index);
                        // 대화상자종료
                        alertDialog.dismiss();
                    }
                });

                // 반복 페이지 버튼 클릭 처리, 새로운 블록코딩 생성
                repeatPageBtn = (Button)dialogView.findViewById(R.id.repeatPageBtn);

                repeatPageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pageIntent = new Intent(getApplicationContext(), BlockCodingActivity.class);
                        // 현재 반복 블록 페이지를 전송
                        tempPageIndex = index;
                        if(repeatBlock != null)
                            pageIntent.putExtra("page", repeatBlock.getRepeatPage());
                        startActivityForResult(pageIntent, REQUEST_CODE_REPEATBLOCK);
                    }
                });
            }
        });
        // 대화상자 실행
        dialog.show();
    }

    public void conditionBlockDialogBox(int blockIndex){
        // 현재 반복 블록에 접근하기 위한 위치
        final int index = blockIndex;

        // 반복 블록 레이아웃 뷰 생성
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_conditionblock, null);

        // 반복블록 대화상자 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("조건 블록");
        builder.setIcon(R.drawable.img11);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setPositiveButton("확인", null);
        builder.setNegativeButton("삭제", null);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            // 확인 버튼
            private Button positiveButton;
            // 삭제 버튼
            private Button negativeButton;
            // ok 페이지 버튼
            private Button okPageBtn;
            // no 페이지 버튼
            private Button noPageBtn;
            // 현재 조건 블록
            private ConditionBlock conditionBlock;
            // 현재 대화상자
            private AlertDialog alertDialog;
            // 체크 블록 소켓
            private FrameLayout checkBlockSocket;
            // 블록 소켓뷰
            private DesignView checkBlockSocketView;

            // 리스트뷰 및 리스트뷰 어댑터
            private ListView checkBlockListView;
            private BlockViewListAdapter checkBlockAdapter;

            // 체크 블록 리스트 생성
            public void createCheckBlockList(){
                checkBlockListView = (ListView)dialogView.findViewById(R.id.checkBlockListView);

                checkBlockAdapter = new BlockViewListAdapter(alertDialog.getContext());

                // 블록 아이템을 리스트에 추가함
                checkBlockAdapter.addItem(new BlockItem(getBlockBitmap(DistanceCheckBlock.DISTANCECHECKBLOCK, -1), DistanceCheckBlock.DISTANCECHECKBLOCK, -1));

                checkBlockListView.setAdapter(checkBlockAdapter);
            }

            public void  distanceCheckBlockDialogBox(){
                // 반복 블록 레이아웃 뷰 생성
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_distancecheckblock, null);

                // 반복블록 대화상자 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(alertDialog.getContext());
                builder.setTitle("거리 체크 블록");
                builder.setIcon(R.drawable.img12);
                builder.setView(dialogView);
                builder.setCancelable(false);
                builder.setPositiveButton("확인", null);
                builder.setNegativeButton("삭제", null);

                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener(){

                    // 현재 대화상자
                    private AlertDialog alertDialog;
                    // 확인 버튼
                    private Button positiveButton;
                    // 삭제 버튼
                    private Button negativeButton;
                    // 거리 에디트 텍스트
                    EditText distanceEdit;
                    // 거리 체크 블록
                    DistanceCheckBlock distanceCheckBlock;

                    @Override
                    public void onShow(DialogInterface dialog) {
                        alertDialog = (AlertDialog)dialog;

                        distanceCheckBlock = (DistanceCheckBlock)conditionBlock.getCheckBlock();

                        distanceEdit = (EditText)alertDialog.findViewById(R.id.distanceEdit);
                        distanceEdit.setText(Integer.toString(distanceCheckBlock.getDistance()));

                        // 대화상자에서 확인버튼을 눌렀을때 처리
                        positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int distance = Integer.parseInt(distanceEdit.getText().toString());
                                // 거리를 채웠는지 확인
                                if(distance <= 0){
                                    Toast.makeText(getApplicationContext(), "거리를 입력해주세요", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    // 블록에 거리를 설정
                                    distanceCheckBlock.setDistance(distance);
                                    alertDialog.dismiss();
                                }
                            }
                        });

                        // 대화상자에서 삭제버튼을 눌렀을때 처리
                        negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        negativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // distanceCheckBlock을 뷰에서 제거
                                checkBlockSocket.removeViewAt(1);
                                // conditionBlock에서 distanceCheckBlock을 제거
                                conditionBlock.setCheckBlock(null);
                                alertDialog.dismiss();
                            }
                        });
                    }
                });

                dialog.show();
            }

            // 체크 블록 소켓 생성
            public void createCheckBlockSocket(){
                // 블록 소켓 생성
                checkBlockSocketView = (DesignView) dialogView.findViewById(R.id.checkBlockSocketView);
                checkBlockSocketView.setBitmap(ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.BLOCKSOCKET));

                // 프레임 레이아웃 생성 및 드래그 이벤트 생성
                checkBlockSocket = (FrameLayout) dialogView.findViewById(R.id.checkBlockSocket);
                checkBlockSocket.setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        // 현재 넣을수 있는 소켓이면 이벤트 체크
                        FrameLayout frameLayout;

                        // 뷰가 FrameLayout인지 확인
                        if (v instanceof FrameLayout)
                            frameLayout = (FrameLayout) v;
                        else
                            return false;

                        // 소켓 위에서 드롭되면 이벤트 처리
                        if(event.getAction() == DragEvent.ACTION_DROP){
                            BlockView blockView = (BlockView)event.getLocalState();
                            if(blockView != null) {
                                final int blockType = blockView.getBlockType();
                                final int direction = blockView.getDirection();

                                // 드래그 앤 드롭으로 넘어온 블록 객체와 같은 객체를 생성
                                BlockView newBlockView = new BlockView(getApplicationContext());
                                newBlockView.setBitmap(blockView.getBitmap());
                                newBlockView.setBlockType(blockType, direction);
                                newBlockView.setIsTouchable(false);
                                // 블록이 소켓안에 들어가면 리스너 등록
                                newBlockView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        distanceCheckBlockDialogBox();
                                    }
                                });

                                // 이미 블록이 들어있다면 제거
                                if(frameLayout.getChildCount() > 1){
                                    frameLayout.removeViewAt(1);
                                }
                                // 소켓 레이아웃에 추가
                                frameLayout.addView(newBlockView);

                                Block checkBlock = null;
                                // 조건 블록에 해당 블록 추가
                                if(blockType == Block.DISTANCECHECKBLOCK) {
                                    checkBlock = new DistanceCheckBlock();
                                    conditionBlock.setCheckBlock(checkBlock);
                                    distanceCheckBlockDialogBox();
                                }
                            }
                        }
                        return true;
                    }
                });

                // 기존에 체크 블록이 들어가있었으면 소켓에 넣어줌
                Block checkBlock = conditionBlock.getCheckBlock();
                if(checkBlock != null){
                    BlockView newBlockView = new BlockView(getApplicationContext());
                    newBlockView.setBitmap(getBlockBitmap(checkBlock.getBlockType(), -1));
                    newBlockView.setBlockType(checkBlock.getBlockType(), -1);
                    newBlockView.setIsTouchable(false);
                    // 블록이 소켓안에 들어가면 리스너 등록
                    newBlockView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            distanceCheckBlockDialogBox();
                        }
                    });

                    checkBlockSocket.addView(newBlockView);
                }
            }

            @Override
            public void onShow(DialogInterface dialog) {
                alertDialog = (AlertDialog)dialog;

                conditionBlock = ((ConditionBlock)pageBlock.getBlock(index));

                createCheckBlockSocket();
                createCheckBlockList();

                // 대화상자에서 확인버튼을 눌렀을때 처리
                positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 체크블록이 들어갔는지 확인
                        if(conditionBlock.getCheckBlock() == null)
                            Toast.makeText(getApplicationContext(), "조건 체크 블록을 넣어주세요!", Toast.LENGTH_SHORT).show();
                        else
                            alertDialog.dismiss();
                    }
                });

                // 대화상자에서 삭제버튼을 눌렀을때 처리
                negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteBlock(index);
                        // 대화상자종료
                        alertDialog.dismiss();
                    }
                });

                // ok 페이지 버튼 클릭 처리, 새로운 블록코딩 생성
                okPageBtn = (Button)dialogView.findViewById(R.id.okPageBtn);

                okPageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pageIntent = new Intent(getApplicationContext(), BlockCodingActivity.class);
                        // 현재 ok조건 블록 페이지를 전송
                        tempPageIndex = index;
                        if(conditionBlock != null)
                            pageIntent.putExtra("page", conditionBlock.getOkPage());
                        startActivityForResult(pageIntent, REQUEST_CODE_CONDITIONBLOCK_OK);
                    }
                });
                // no 페이지 버튼 클릭 처리, 새로운 블록코딩 생성
                noPageBtn = (Button)dialogView.findViewById(R.id.noPageBtn);

                noPageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pageIntent = new Intent(getApplicationContext(), BlockCodingActivity.class);
                        // 현재 ok조건 블록 페이지를 전송
                        tempPageIndex = index;
                        if(conditionBlock != null)
                            pageIntent.putExtra("page", conditionBlock.getNoPage());
                        startActivityForResult(pageIntent, REQUEST_CODE_CONDITIONBLOCK_NO);
                    }
                });
            }
        });

        // 대화상자 실행
        dialog.show();
    }

    public void pageBlockDialogBox(int blockIndex){
        // 현재 반복 블록에 접근하기 위한 위치
        final int index = blockIndex;

        // 반복 블록 레이아웃 뷰 생성
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_pageblock, null);

        // 반복블록 대화상자 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("페이지 블록");
        builder.setIcon(R.drawable.img13);
        builder.setView(dialogView);
        builder.setCancelable(false);
        builder.setPositiveButton("확인", null);
        builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteBlock(index);
            }
        });

        Button pageBtn = (Button)dialogView.findViewById(R.id.pageBtn);
        pageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pageIntent = new Intent(getApplicationContext(), BlockCodingActivity.class);
                // 현재 ok조건 블록 페이지를 전송
                tempPageIndex = index;
                pageIntent.putExtra("page", (PageBlock)pageBlock.getBlock(index));
                startActivityForResult(pageIntent, REQUEST_CODE_PAGEBLOCK);
            }
        });

        AlertDialog dialog = builder.create();

        // 대화상자 실행
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
        // 조건 블록의 ok페이지를 받아와 저장한다.
        else if(requestCode == REQUEST_CODE_CONDITIONBLOCK_OK){
            if(resultCode == RESULT_OK){
                PageBlock tempPageBlock = (PageBlock)data.getSerializableExtra("pageResult");
                ((ConditionBlock)pageBlock.getBlock(tempPageIndex)).setOKPage(tempPageBlock);
            }
        }
        // 조건 블록의 no페이지를 받아와 저장한다.
        else if(requestCode == REQUEST_CODE_CONDITIONBLOCK_NO){
            if(resultCode == RESULT_OK){
                PageBlock tempPageBlock = (PageBlock)data.getSerializableExtra("pageResult");
                ((ConditionBlock)pageBlock.getBlock(tempPageIndex)).setNOPage(tempPageBlock);
            }
        }
        // 페이지 블록의 페이지를 받아서 저장한다.
        else if(requestCode == REQUEST_CODE_PAGEBLOCK){
            if(resultCode == RESULT_OK){
                PageBlock tempPageBlock = (PageBlock)data.getSerializableExtra("pageResult");
                pageBlock.setBlock(tempPageBlock, tempPageIndex);
            }
        }
    }

    // 블록 종류에 따른 비트맵 반환
    public Bitmap getBlockBitmap(int blockType, int direction){

        switch(blockType){
            case Block.MOVEBLOCK:
                if (direction == MoveBlock.FORWARD)
                    return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.FORWARDBLOCK);
                else if (direction == MoveBlock.BACKWARD)
                    return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.BACKWORDBLOCK);
                break;

            case Block.ROTATEBLOCK:
                if(direction == RotateBlock.LEFT)
                    return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.LEFTROTATEBLOCK);
                else if(direction == RotateBlock.RIGHT)
                    return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.RIGHTROTATEBLOCK);
                break;

            case Block.STOPBLOCK:
                return ResourceManager.getInstance(getResources()).getBitmap(ResourceManager.BITMAP.STOPBLOCK);

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

        for(int i = 0;i < blockSocket.length;i++){
            // 드래그앤 드롭 리스너로 인덱스 번호를 넘겨줌
            final int index = i;
            // 각 소켓의 프레임 레이아웃을 가져옴
            blockSocket[i] = (FrameLayout)findViewById(getResources().getIdentifier("blockSocket"+(i+1), "id", "com.kiducar.kiducation.kiducar"));
            // 드래그 앤 드롭 리스너를 설정
            blockSocket[i].setOnDragListener(new View.OnDragListener() {

                // 드래그 앤 드롭이 발생했을때 이벤트 처리
                public void eventHandle(FrameLayout frameLayout, BlockView blockView){

                    if(blockView != null) {
                        final int blockType = blockView.getBlockType();
                        final int direction = blockView.getDirection();

                        // 드래그 앤 드롭으로 넘어온 블록 객체와 같은 객체를 생성
                        BlockView newBlockView = new BlockView(getApplicationContext());
                        newBlockView.setBitmap(blockView.getBitmap());
                        newBlockView.setBlockType(blockType, direction);
                        newBlockView.setIsTouchable(false);
                        // 블록이 소켓안에 들어가면 리스너 등록
                        newBlockView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                blockClickEvent(blockType, index);
                            }
                        });

                        if(index == curBlockSocketNum) {
                            // 소켓 레이아웃에 추가
                            frameLayout.addView(newBlockView);

                            // 페이지에 해당 블록 추가
                            addBlockToPage(blockView.getBlockType(), blockView.getDirection(), index, false);

                            curBlockSocketNum++;
                        }
                        else if(index < curBlockSocketNum) {
                            // 소켓 레이아웃에 들어있던 블록 제거
                            frameLayout.removeViewAt(1);

                            // 소켓 레이아웃에 새 블록 추가
                            frameLayout.addView(newBlockView);

                            // PageBlock에서 해당 위치에 블록 변경
                            // 페이지에 해당 블록 추가
                            addBlockToPage(blockView.getBlockType(), blockView.getDirection(), index, true);
                        }
                    }
                }

                @Override
                public boolean onDrag(View v, DragEvent event) {
                    // 현재 넣을수 있는 소켓이면 이벤트 체크
                    if(index <= curBlockSocketNum) {
                        FrameLayout frameLayout;

                        // 뷰가 FrameLayout인지 확인
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

        // PageBlock을 외부 액티비티에서 받아옴
        Intent intent = getIntent();
        pageBlock = (PageBlock)intent.getSerializableExtra("page");
        // PageBlock이 안 넘어왔다면 새로운 PageBlock 생성
        if(pageBlock == null)
            pageBlock = new PageBlock();
            // PageBlock이 넘어왔다면 PageBlock에 들어있는 블록을 blocksocket에 넣어줌
        else{
            for(int i = 0;i < pageBlock.getCurBlockNum();i++,curBlockSocketNum++){
                // 드래그 앤 드롭으로 넘어온 블록 객체와 같은 객체를 생성
                BlockView newBlockView = new BlockView(getApplicationContext());

                final int blockType = pageBlock.getBlock(i).getBlockType();
                int direction = -1;
                if(blockType == Block.MOVEBLOCK)
                    direction = ((MoveBlock)pageBlock.getBlock(i)).getDirection();
                else if(blockType == Block.ROTATEBLOCK)
                    direction = ((RotateBlock)pageBlock.getBlock(i)).getRotateDirection();

                newBlockView.setBitmap(getBlockBitmap(blockType, direction));

                if(blockType == Block.MOVEBLOCK)
                    newBlockView.setBlockType(blockType, direction);
                else if(blockType == Block.ROTATEBLOCK)
                    newBlockView.setBlockType(blockType, direction);
                else
                    newBlockView.setBlockType(blockType, direction);

                newBlockView.setIsTouchable(false);
                // 블록이 소켓안에 들어가면 리스너 등록
                final int index = i;
                newBlockView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        blockClickEvent(blockType, index);
                    }
                });

                // 소켓 레이아웃에 추가
                blockSocket[i].addView(newBlockView);
            }
        }
    }

    // 블록 리스트를 생성
    protected void createBlockList1(){
        blockListView1 = (ListView)findViewById(R.id.blockListView1);

        blockAdapter1 = new BlockViewListAdapter(this);

        // 블록 아이템을 리스트에 추가함
        blockAdapter1.addItem(new BlockItem(getBlockBitmap(MoveBlock.MOVEBLOCK, MoveBlock.FORWARD), MoveBlock.MOVEBLOCK, MoveBlock.FORWARD));
        blockAdapter1.addItem(new BlockItem(getBlockBitmap(MoveBlock.MOVEBLOCK, MoveBlock.BACKWARD), MoveBlock.MOVEBLOCK, MoveBlock.BACKWARD));
        blockAdapter1.addItem(new BlockItem(getBlockBitmap(RotateBlock.ROTATEBLOCK, RotateBlock.LEFT), RotateBlock.ROTATEBLOCK, RotateBlock.LEFT));
        blockAdapter1.addItem(new BlockItem(getBlockBitmap(RotateBlock.ROTATEBLOCK, RotateBlock.RIGHT), RotateBlock.ROTATEBLOCK, RotateBlock.RIGHT));

        blockListView1.setAdapter(blockAdapter1);
    }

    // 블록 리스트를 생성
    protected void createBlockList2(){
        blockListView2 = (ListView)findViewById(R.id.blockListView2);

        blockAdapter2 = new BlockViewListAdapter(this);

        // 블록 아이템을 리스트에 추가함
        blockAdapter2.addItem(new BlockItem(getBlockBitmap(StopBlock.STOPBLOCK, -1), StopBlock.STOPBLOCK, -1));
        blockAdapter2.addItem(new BlockItem(getBlockBitmap(RepeatBlock.REPEATBLOCK, -1), RepeatBlock.REPEATBLOCK, -1));
        blockAdapter2.addItem(new BlockItem(getBlockBitmap(ConditionBlock.CONDITIONBLOCK, -1), ConditionBlock.CONDITIONBLOCK, -1));
        blockAdapter2.addItem(new BlockItem(getBlockBitmap(PageBlock.PAGEBLOCK, -1), PageBlock.PAGEBLOCK, -1));

        blockListView2.setAdapter(blockAdapter2);
    }

    public void onClickStartExecute(View v){
        ExecuteModule.getInstance().startExecute();
    }

    public void onClickStopExecute(View v){
        ExecuteModule.getInstance().stopExecute();
    }

    // 물음표 버튼에 적용
    public void onClickQuestion(View v){

        // 도움말 레이아웃 뷰 생성
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_question, null);

        // 도움말 대화상자 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // quit 버튼에 적용
    public void onClickQuit(View v){
        // 결과를 호출한 액티비티에 전송
        Intent resultIntent = new Intent();
        resultIntent.putExtra("pageResult", pageBlock);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        onClickQuit(null);
    }
}
