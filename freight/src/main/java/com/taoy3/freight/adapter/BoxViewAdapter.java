package com.taoy3.freight.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taoy3.freight.constant.MyColor;

import java.util.List;

/**
 * Created by taoy2 on 15-12-22.
 */
public class BoxViewAdapter extends AppBaseAdapter<String>{
    public BoxViewAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public View getItemView(int i, View textView, ViewGroup viewGroup) {
        if(textView==null){
            textView = new TextView(context);
        }
        textView.setPadding(10,10,10,10);
        if(i>list.size()/2-1){
            textView.setBackgroundColor(MyColor.WHITE);
        }else {
            textView.setBackgroundColor(MyColor.PINK);
        }
        ((TextView)textView).setText(list.get(i));
        return textView;
    }
}
