package com.cnst.wisdom.ui.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.ui.adapter.TeachPlanDialogSelAd;

import java.util.List;

/**
 * <一句话功能描述>
 * <功能详细描述>
 *
 * @author hanshuai
 * @see [相关类/方法]
 * @since [产品/模板版本]
 */
public class ListDialog extends Dialog implements AdapterView.OnItemClickListener, View.OnClickListener, DialogInterface.OnShowListener{
    private final List list;
    private final AppCompatActivity mActivity;
    private Button confirmBt;
    private ListView mListView;
//    private int selIndex = -1;//默认设为-1，防止按返回键时默认选第一项问题
//    private int selPosition = -1;
    private TeachPlanDialogSelAd adapter;
    private LinearLayout.LayoutParams params;

    public ListDialog(AppCompatActivity activity, List list) {
        super(activity, R.style.no_title_dialog);
        this.list = list;
        this.mActivity = activity;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_list);
        Window window = getWindow();
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        window.setBackgroundDrawableResource(R.drawable.bg_dialog);
        window.setLayout((int) (width * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            window.setClipToOutline(true);
        }
        mListView = (ListView) findViewById(R.id.sel_list);
        adapter = new TeachPlanDialogSelAd(mActivity, list, mListView);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(this);
        params = (LinearLayout.LayoutParams) mListView.getLayoutParams();
        findViewById(R.id.cancel).setOnClickListener(this);
        confirmBt = (Button) findViewById(R.id.confirm);
        confirmBt.setOnClickListener(this);
        setOnShowListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setCurPosition(position);
        adapter.notifyDataSetChanged();
        if (adapter.getCurPosition()>-1) {
            confirmBt.setClickable(true);
            confirmBt.setBackgroundResource(R.drawable.bg_bottom_right_clickable);
            confirmBt.setTextColor(mActivity.getResources().getColor(R.color.white));
        }
    }

    public TeachPlanDialogSelAd getAdapter() {
        return adapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                adapter.setCurPosition(-1);
                adapter.notifyDataSetChanged();
                dismiss();
                break;
            case R.id.confirm:
                adapter.isConfirm = true;
                dismiss();
                break;
        }
    }

//    public int getSelIndex() {
//        return selIndex;
//    }

//    public void setSelIndex(int selText) {
//        this.selIndex = selText;
//    }
//
//    public int getSelPositon() {
//        return selPosition;
//    }
//
//    public void setSelPositon(int selPosition) {
//        this.selPosition = selPosition;
//    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (list.size() > 8) {
            setListMaxHeight(8);
        } else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    public void setListMaxHeight(int max) {
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < max; i++) {
            View mView = adapter.getView(i, null, mListView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
        }
        params.height = totalHeight;
        mListView.setLayoutParams(params);
        mListView.requestLayout();
    }
}
