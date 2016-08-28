package com.taoy3.freight.view.labelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taoy3.freight.R;


/**
 * Created by Administrator on 2015/11/22.
 */
public class LabelEditView extends RelativeLayout{
    private EditText valueText;

    public String getText() {
        return valueText.getText().toString();
    }
    public LabelEditView(Context context) {
        super(context);
        init(context,null);
    }

    public LabelEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public LabelEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }
    public String getValue(){
        return valueText.getText().toString();
    }

    private void init(Context context,AttributeSet attrs) {
        TextView labelText = new TextView(context);
        valueText = new EditText(context);
        addViewInLayout(labelText,0,new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RIGHT_OF,labelText.getId());
        addViewInLayout(valueText, 1, params);
        TextView line = new TextView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(BELOW,labelText.getId());
        addViewInLayout(line,2,layoutParams);

        if(attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);
            int labelColor = a.getColor(R.styleable.LabelTextView_label_textColor, Color.BLACK);
            int valueColor = a.getColor(R.styleable.LabelTextView_value_textColor, Color.BLACK);
            int hintColor = a.getColor(R.styleable.LabelTextView_hint_color,Color.GRAY);
            float labelSize = a.getDimensionPixelSize(R.styleable.LabelTextView_label_textSize, R.dimen.text_20)/2;
            float valueSize = a.getDimensionPixelOffset(R.styleable.LabelTextView_value_textSize, R.dimen.text_15)/2;
            String label = a.getString(R.styleable.LabelTextView_label_text);
            String value = a.getString(R.styleable.LabelTextView_value_text);
            String hint = a.getString(R.styleable.LabelTextView_hint_text);
            a.recycle();

            labelText.setTextColor(labelColor);
            labelText.setTextSize(labelSize);
            labelText.setText(label);
            valueText.setTextSize(valueSize);
            valueText.setHintTextColor(hintColor);
            valueText.setTextColor(valueColor);
            valueText.setHint(hint);
            valueText.setText(value);
        }
    }
}
