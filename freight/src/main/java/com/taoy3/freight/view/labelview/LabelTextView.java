package com.taoy3.freight.view.labelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taoy3.freight.R;


/**
 * Created by Administrator on 2015/11/22.
 */
public class LabelTextView extends RelativeLayout {
    private TextView valueText;

    public String getText() {
        return valueText.getText().toString();
    }

    public void setText(String value) {
        valueText.setText(value);
    }

    public LabelTextView(Context context) {
        super(context);
        init(context,null);
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs) {
        if(attrs!=null){
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LabelTextView, 0, 0);
            int labelColor = a.getColor(R.styleable.LabelTextView_label_textColor, Color.BLACK);
            int valueColor = a.getColor(R.styleable.LabelTextView_value_textColor, Color.BLACK);
            float labelSize = a.getDimensionPixelSize(R.styleable.LabelTextView_label_textSize, R.dimen.text_20);
            float valueSize =  a.getDimensionPixelSize(R.styleable.LabelTextView_value_textSize, R.dimen.text_15);
            String label = a.getString(R.styleable.LabelTextView_label_text);
            String value = a.getString(R.styleable.LabelTextView_value_text);
            Drawable drawableLeft = a.getDrawable(R.styleable.LabelTextView_drawable_right);
            TextView labelText = (TextView) findViewById(R.id.editView_label);
            valueText = (TextView) findViewById(R.id.editView_value);
            labelText.setTextColor(labelColor);
            labelText.setTextSize(labelSize);
            labelText.setText(label);
            valueText.setTextSize(valueSize);
            valueText.setTextColor(valueColor);
            valueText.setText(value);
            valueText.setCompoundDrawables(drawableLeft, null, null, null);
            a.recycle();
        }
    }
}
