package com.cnst.wisdom.ui.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cnst.wisdom.R;
import com.cnst.wisdom.ui.adapter.TeachPlanPopSelAd;
import com.cnst.wisdom.utills.DisplayUtils;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author taoyuan
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class ListWindow extends PopupWindow{
    private final List data;
    private TeachPlanPopSelAd adapter;
    private ListView listView;
    private View anchorView;
    private FrameLayout.LayoutParams params;

    public ListWindow(View anchorView, List data,Context context){
        super(context);
        this.anchorView = anchorView;
        this.data = data;
        init(context);
    }

    private void init(Context context) {
        int displayWidth = DisplayUtils.getWidthPx((Activity) context);
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.pop_sel, null);
        listView = (ListView) frameLayout.getChildAt(0);
        listView.setVerticalScrollBarEnabled(false);
        params = (FrameLayout.LayoutParams) listView.getLayoutParams();
        adapter = new TeachPlanPopSelAd(context, data);
        listView.setAdapter(adapter);
        setContentView(frameLayout);
        setWidth(displayWidth/3);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true); //设置在外部点击,弹出框消失
        setFocusable(true);
        setBackgroundDrawable(context.getResources().getDrawable(R.color.white));//设置弹出框背景
    }
    public void show(){
        if (data.size() > 9) {
            setListMaxHeight(9);
        } else {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
        adapter.notifyDataSetChanged();
        showAsDropDown(anchorView, 0, 0);
    }

    public void setListMaxHeight(int max) {
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < max; i++) {
            View mView = adapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += mView.getMeasuredHeight();
        }
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        listView.setOnItemClickListener(itemClickListener);
    }

    public AdapterView<?> getListView() {
        return listView;
    }
}
