package com.taoy3.freight.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.taoy3.freight.R;

/**
 * Created by taoy2 on 15-11-19.
 */
public class AddCutButton extends LinearLayout implements View.OnClickListener, TextWatcher {
    private EditText qtyTv;
    private int qty;
    private QtyChangeListener listener;

    public void setQtyChangeListener(QtyChangeListener listener) {
        this.listener = listener;
    }

    public AddCutButton(Context context) {
        super(context);
        initView(context);
    }
    public void setQty(int qty){
        qtyTv.setText(qty+"");
        this.qty = qty;
    }
    public int getQty() {
        return qty;
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.add_cut_button, this);
        findViewById(R.id.cut).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
        qtyTv = (EditText)findViewById(R.id.qty);
        qtyTv.addTextChangedListener(this);
    }

    public AddCutButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AddCutButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.cut){
            if(qty>0){
                qty--;
            }
        }else {
            qty++;
        }
        qtyTv.setText(qty + "");
//        if(listener!=null)
//        listener.onQtyChange(this,qty);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(listener!=null){
            listener.onQtyChange(this,Integer.parseInt(s.toString()));
        }
//        qty = Integer.parseInt(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public interface QtyChangeListener{
        void onQtyChange(View view, int qty);
    }
}
