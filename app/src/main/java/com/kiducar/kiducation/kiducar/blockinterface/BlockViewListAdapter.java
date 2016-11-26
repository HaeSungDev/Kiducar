package com.kiducar.kiducation.kiducar.blockinterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kiducar.kiducation.kiducar.R;

import java.util.ArrayList;
import java.util.List;

// 블록뷰들을 담는 어댑터, 리스트뷰에 표시하기위해 사용
public class BlockViewListAdapter extends BaseAdapter {

    // 앱 컨텍스트 객체를 가져와서 사용
    private Context m_context;

    // 아이템을 담을 리스트
    private List<BlockItem> m_items = new ArrayList<BlockItem>();

    public BlockViewListAdapter(Context context) {
        m_context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public BlockItem getItem(int position) {
        return m_items.get(position);
    }

    // 블록뷰 아이템을 추가한다.
    public void addItem(BlockItem item){
        m_items.add(item);
    }

    // 아이템 개수 반환
    public int getCount() {
        return m_items.size();
    }

    // 리스트에 출력할 뷰를 반환한다.
    public View getView(int position, View convertView, ViewGroup parent) {
        // 리스트에 출력된 뷰가 없으면 생성한다.
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)m_context.getSystemService(m_context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.blockview_layout, parent, false);
        }

        BlockView blockView = (BlockView)convertView.findViewById(R.id.blockView);
        blockView.setResDrawable(m_items.get(position).m_resDrawable);
        blockView.setBlockType(m_items.get(position).m_blockType, m_items.get(position).m_direction);

        return convertView;
    }
}
