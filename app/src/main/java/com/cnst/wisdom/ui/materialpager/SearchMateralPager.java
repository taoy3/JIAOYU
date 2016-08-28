package com.cnst.wisdom.ui.materialpager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.model.bean.MaterialPagerBean;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author taoyuan
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class SearchMateralPager extends MaterialPager {

    private final float itemHeight;
    private MaterialPagerBean materialPagerBean;

    public void setMaterialPagerBean(MaterialPagerBean materialPagerBean) {
        this.materialPagerBean = materialPagerBean;
    }

    public SearchMateralPager(Activity activity, String code) {
        super(activity, code);
        itemHeight = activity.getResources().getDimension(R.dimen.width_70);
    }

    @Override
    protected void initDataFromService() {
        if(materialPagerBean!=null){
            mListView.onRefreshComplete(false);
            initServiceData(materialPagerBean);
            setListViewHeightBasedOnChildren(mListView);
        }
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if(listItem instanceof LinearLayout){
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }else {
                totalHeight += itemHeight;
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
