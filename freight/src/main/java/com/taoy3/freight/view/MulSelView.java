package com.taoy3.freight.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.constant.MyColor;


/**
 * Created by taoy2 on 15-12-15.
 */
public class MulSelView extends GridView implements AdapterView.OnItemClickListener {
    private OnItemClickChange itemCliCKChange;

    public void setItemCliCKChange(OnItemClickChange itemCliCKChange) {
        this.itemCliCKChange = itemCliCKChange;
    }

    public boolean[] isSelects;
    public MulSelView(Context context) {
        super(context);
    }

    public MulSelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        isSelects = new boolean[adapter.getCount()];
        setOnItemClickListener(this);
        setVerticalSpacing(30);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        isSelects[position] = !isSelects[position];
        ((TextView)view).setTextColor(isSelects[position] ? MyColor.WHITE : MyColor.BLACK);
        view.setBackgroundResource(isSelects[position] ? R.drawable.background_dark_green : R.drawable.background_round_green);
        if(itemCliCKChange!=null){
            itemCliCKChange.itemClickChange(position,isSelects[position]);
        }
    }
    public interface OnItemClickChange {
        void itemClickChange(int position, boolean isSelect);
    }
}
