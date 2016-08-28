package com.taoy3.freight.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.CusMangerAdapter;
import com.taoy3.freight.bean.SearchBean;
import com.taoy3.freight.util.GetFirstLetter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by taoy2 on 15-11-23.
 */
public class IndexListView extends LinearLayout implements View.OnTouchListener {
    private ListView listView;
    private ListView indexView;
    private HashMap<Integer,Integer> indexPositions = new HashMap<>();
    private final String[] firstLetters = {"A", "B", "C", "D", "E", "F", "G", "H", "I","J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S","T","U","V", "W", "X", "Y", "Z"};
//    private CusMangerAdapter.OnItemChangeListener listener;
    private float mDownY;
    private Context mContext;
    private int position;
    private int indexItemHeight;
    private int color;

    public IndexListView(Context context) {
        super(context);
        init(context);
    }

    public IndexListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IndexListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_index_listview, this);
        listView = (ListView) findViewById(R.id.index_list);
        indexView = (ListView) findViewById(R.id.index_index);
    }

    public void addHeaderView(View view) {
        listView.addHeaderView(view);
    }

    public void addFooterView(View view) {
        listView.addFooterView(view);
    }

    public CusMangerAdapter setAdapter(List<? extends SearchBean> list, Context context, TextView headView) {
        this.mContext = context;
        for (int j = 0; j < firstLetters.length; j++) {
            for (int i = 0; i < list.size(); i++) {
                if(GetFirstLetter.getFirstLetter(list.get(i).getId()).toUpperCase().equals(firstLetters[j])){
                    indexPositions.put(j,i);
                    break;
                }
            }
        }
        ArrayAdapter indexAdapter = new ArrayAdapter(context,R.layout.adapter_index , firstLetters);
        indexView.setAdapter(indexAdapter);
        CusMangerAdapter adapter = new CusMangerAdapter(context,list,indexPositions,firstLetters,headView);
        listView.setAdapter(adapter);
        indexItemHeight = getItemViewHeight(indexView);
        indexView.setOnTouchListener(this);
        return adapter;
    }

    public void onItemSelection(int position) {
        if(indexPositions.get(position)!=null){
            listView.setSelection(indexPositions.get(position));
        }else {
            position++;
            if(position<firstLetters.length){
                onItemSelection(position);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                color = v.getDrawingCacheBackgroundColor();
                v.setBackgroundColor(R.color.gray);
                break;
            case MotionEvent.ACTION_MOVE:
                position = (int) Math.floor(event.getY()/ indexItemHeight);
                onItemSelection(position);
                break;
            case MotionEvent.ACTION_UP:
                v.setBackgroundColor(color);
                if(mDownY==event.getY()){
                    onItemSelection(position);
                    Toast toast = Toast.makeText(mContext,firstLetters[position],Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
        }
        return true;
    }
    public static int getItemViewHeight(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
            View itemView = mAdapter.getView(0, null, listView);
        itemView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            return itemView.getMeasuredHeight();
    }
}
