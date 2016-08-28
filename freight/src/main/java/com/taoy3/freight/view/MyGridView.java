package com.taoy3.freight.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taoy3.freight.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taoy2 on 15-12-9.
 */
public class MyGridView extends LinearLayout implements View.OnDragListener {
    private float x;
    private float y;
    public MyGridView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }
    public void setData(List<List<String>> lists,int itemWidth){
//        setBackgroundColor(MyColor.GRAYLIGHT);
        int maxRow = 0;
        for (int i = 0; i < lists.size(); i++) {
            maxRow = maxRow>lists.get(i).size()?maxRow:lists.get(i).size();
        }
        List<List<String>> temp = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            if(lists.get(i).size()<maxRow){
                temp.add(lists.get(i));
                lists.remove(i);
                i--;
            }
        }
        lists.addAll(temp);
        for (int i = 0; i < lists.size(); i++) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setMinimumHeight(getMinimumHeight()/lists.size());
            layout.setWeightSum(itemWidth * maxRow);
            for (int j = 0; j < lists.get(i).size(); j++) {
                TextView itemView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.adapter_array,null);
//                itemView.setBackgroundColor(MyColor.WHITE);
                LayoutParams params;
                itemView.setGravity(Gravity.CENTER);
                itemView.setText(lists.get(i).get(j));
                if(lists.get(i).size()<maxRow&&j==lists.get(i).size()-1){
//                    +2*(maxRow-lists.get(i).size())
                    params = new LayoutParams((maxRow-lists.get(i).size()+1)*itemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
//                    params.setMargins(1,1,0,1);
                }else {
                    params = new LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
//                    params.setMargins(1,1,1,1);
                }
                layout.addView(itemView, params);
            }
            addView(layout);
        }
        setOnDragListener(this);
    }

    private void setMove(float x, float y) {

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = v.getX();
                y = v.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                setMove(v.getX(),v.getY());
                break;
        }
        return false;
    }
}
