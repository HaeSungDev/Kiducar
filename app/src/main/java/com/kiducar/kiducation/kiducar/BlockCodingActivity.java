package com.kiducar.kiducation.kiducar;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kiducar.kiducation.kiducar.blockcoding.ConditionBlock;
import com.kiducar.kiducation.kiducar.blockcoding.DistanceCheckBlock;
import com.kiducar.kiducation.kiducar.blockcoding.MoveBlock;
import com.kiducar.kiducation.kiducar.blockcoding.PageBlock;
import com.kiducar.kiducation.kiducar.blockcoding.RepeatBlock;
import com.kiducar.kiducation.kiducar.blockcoding.RotateBlock;
import com.kiducar.kiducation.kiducar.blockinterface.BlockItem;
import com.kiducar.kiducation.kiducar.blockinterface.BlockView;
import com.kiducar.kiducation.kiducar.blockinterface.BlockViewListAdapter;

// 블록코딩을 진행하는 액티비티
public class BlockCodingActivity extends AppCompatActivity {

    // 블록을 담고있는 리스트뷰
    ListView blockListView1;
    ListView blockListView2;
    BlockViewListAdapter blockAdapter1;
    BlockViewListAdapter blockAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_coding);

        createBlockList1();
        createBlockList2();
    }

    // 블록 리스트를 생성
    protected void createBlockList1(){
        blockListView1 = (ListView)findViewById(R.id.blockListView1);

        blockAdapter1 = new BlockViewListAdapter(this);

        // 블록 아이템을 리스트에 추가함
        blockAdapter1.addItem(new BlockItem(R.drawable.forwardblock, MoveBlock.MOVEBLOCK, MoveBlock.FORWARD));
        blockAdapter1.addItem(new BlockItem(R.drawable.backwardblock, MoveBlock.MOVEBLOCK, MoveBlock.BACKWARD));
        blockAdapter1.addItem(new BlockItem(R.drawable.leftrotateblock, RotateBlock.ROTATEBLOCK, RotateBlock.LEFT));
        blockAdapter1.addItem(new BlockItem(R.drawable.rightrotateblock, RotateBlock.ROTATEBLOCK, RotateBlock.RIGHT));

        blockListView1.setAdapter(blockAdapter1);
        blockListView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                view.findViewById(R.id.blockView).startDrag(null, new View.DragShadowBuilder(view.findViewById(R.id.blockView)), view.findViewById(R.id.blockView), 0);
                return false;
            }
        });
    }

    // 블록 리스트를 생성
    protected void createBlockList2(){
        blockListView2 = (ListView)findViewById(R.id.blockListView2);

        blockAdapter2 = new BlockViewListAdapter(this);

        // 블록 아이템을 리스트에 추가함
        blockAdapter2.addItem(new BlockItem(R.drawable.repeatblock, RepeatBlock.REPEATBLOCK, -1));
        blockAdapter2.addItem(new BlockItem(R.drawable.conditionblock, ConditionBlock.CONDITIONBLOCK, -1));
        blockAdapter2.addItem(new BlockItem(R.drawable.distancecheckblock, DistanceCheckBlock.DISTANCECHECKBLOCK, -1));
        blockAdapter2.addItem(new BlockItem(R.drawable.pageblock, PageBlock.PAGEBLOCK, -1));

        blockListView2.setAdapter(blockAdapter2);
        blockListView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                view.findViewById(R.id.blockView).startDrag(null, new View.DragShadowBuilder(view.findViewById(R.id.blockView)), view.findViewById(R.id.blockView), 0);
                return false;
            }
        });
    }
}
