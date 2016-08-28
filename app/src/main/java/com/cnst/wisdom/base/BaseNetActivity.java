package com.cnst.wisdom.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.view.TitleView;

public abstract class BaseNetActivity extends AppCompatActivity {
private TitleView titleView;
    protected VolleyManager volleyManager = VolleyManager.getInstance();
    private Dialog mDialog;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
        initBaseView();
        initView();
        initData();
    }

    private void initBaseView() {
        titleView = (TitleView) findViewById(R.id.layout_title);
        if (titleView != null) {
            titleView.setTitle(getTitle());
        }
    }

    protected abstract void setLayout();

    protected abstract void initData();

    protected abstract void initView();

    public void noData(String info){
        mDialog = new Dialog(this, R.style.no_title_dialog);
        mDialog.setContentView(R.layout.dialog_sel_subject_state);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        TextView dialogTitle = (TextView) mDialog.findViewById(R.id.dialog_title);
        dialogTitle.setText(info+"/t/n,是否继续");
        mDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                initData();
            }
        });
        mDialog.show();
    }
}
