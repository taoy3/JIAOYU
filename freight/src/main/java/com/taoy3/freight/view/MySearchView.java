package com.taoy3.freight.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.taoy3.freight.R;


/**
 * Created by yetwish on 2015-05-11
 */

public class MySearchView extends LinearLayout implements View.OnClickListener, TextWatcher {
    private EditText etInput;
    private ImageView ivDelete;
    private OnTextChanged onTextChanged;
    public void setHint(int tip){
        etInput.setHint(tip);
    }
    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_search_layout, this);
        initViews();
    }
    public void setOnTextContentChange(OnTextChanged onTextChanged){
        this.onTextChanged = onTextChanged;
    }
    private void initViews() {
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        etInput = (EditText) findViewById(R.id.search_et_input);
        etInput.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        etInput.addTextChangedListener(this);
    }
    public void setEditAble(boolean flag){
        etInput.setEnabled(flag);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_iv_delete:
                etInput.setText("");
                view.setVisibility(GONE);
                if (onTextChanged!= null){
                    onTextChanged.onSearchCancel();
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(ivDelete.getVisibility()==GONE){
            ivDelete.setVisibility(VISIBLE);
        }
        if (charSequence.toString().trim().length()>0) {
            if (onTextChanged!= null){
                onTextChanged.onTextChangedListener(charSequence, i, i1, i2);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {}
    public interface OnTextChanged{
        void onTextChangedListener(CharSequence charSequence, int i, int i1, int i2);
        void onSearchCancel();
    }
}