package com.cnst.wisdom.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnst.wisdom.R;

/**
 * Created by taoy3 on 16/8/7.
 */
public class TitleView extends FrameLayout {
    private ImageButton startBt;
    private ImageButton endBt;
    private TextView titleTv;
    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.view_head_title, null, false);
        startBt = (ImageButton) contentView.findViewById(R.id.start_view);
        titleTv = (TextView) contentView.findViewById(R.id.title_view);
        endBt = (ImageButton) contentView.findViewById(R.id.end_view);
        if(attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.TitleView);

            int endImg = a.getResourceId(R.styleable.TitleView_end_img,0);
            String name = a.getString(R.styleable.TitleView_name);
            a.recycle();

            titleTv.setText(name);
            endBt.setBackgroundResource(endImg);
        }
        startBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof Activity){
                    ((Activity)context).finish();
                }
            }
        });
    }
    public void setTitle(CharSequence title){
        titleTv.setText(title);
    }
    public void setEndImgRes(int res){
        endBt.setBackgroundResource(res);
    }
    public void setEndClick(OnClickListener listener){
        if (listener != null&&endBt!=null) {
            endBt.setOnClickListener(listener);
        }
    }
}
