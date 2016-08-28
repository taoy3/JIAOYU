package com.cnst.wisdom.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnst.wisdom.R;

/**
 * 带Label的TextView
 * 从左到右分别是图标、固定的文字、可编辑的文字、右箭头
 * 当设置为可编辑时，图标及文字变亮，不可编辑时，图标文字变暗
 * @author taoyuan
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class LabelView extends LinearLayout{
    private ImageView iconView;
    private TextView labelTv;
    private TextView textTv;
    private Drawable clickableDb;
    private Drawable unClickableDb;
    private int normalColor;
    private int tipColor;
    public LabelView(Context context) {
        this(context, null, 0);
    }

    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        normalColor = context.getResources().getColor(R.color.normal_text);
        tipColor = context.getResources().getColor(R.color.tip_text);
        LayoutInflater.from(context).inflate(R.layout.view_label, this);
        LinearLayout layout = (LinearLayout) ((LinearLayout) getChildAt(0)).getChildAt(0);
        iconView = (ImageView) layout.getChildAt(0);
        labelTv = (TextView) layout.getChildAt(1);
        textTv = (TextView) layout.getChildAt(2);
        if(attrs!=null){
            TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LabelView, 0, 0);
            clickableDb = array.getDrawable(R.styleable.LabelView_drawable_clickable);
            unClickableDb = array.getDrawable(R.styleable.LabelView_drawable_un_clickable);
            iconView.setImageDrawable(isClickable()?clickableDb:unClickableDb);
            labelTv.setText(array.getString(R.styleable.LabelView_label_text));
            labelTv.setTextColor(isClickable()?normalColor:tipColor);
            array.recycle();
        }
    }
    public void setSelectedAble(boolean flag){
        setClickable(flag);
        textTv.setText("");
        if(flag){
            iconView.setImageDrawable(clickableDb);
            labelTv.setTextColor(normalColor);
        }else {
            iconView.setImageDrawable(unClickableDb);
            labelTv.setTextColor(tipColor);
        }
    }
    public void setText(String text){
        textTv.setText(text);
    }
}
