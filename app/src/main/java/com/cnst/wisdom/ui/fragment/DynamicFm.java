package com.cnst.wisdom.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseFragment;
import com.cnst.wisdom.model.bean.FlipImage;
import com.cnst.wisdom.ui.widget.MyFlipper;

/**
 * 动态界面
 */
public class DynamicFm extends BaseFragment implements View.OnClickListener
{
    private ViewFlipper mViewFlipper;
    private RadioGroup flipperRadioGroup;
    private MyFlipper mMyFlipper;
    private ListView mLv_dynamic;
    private DynamicFmAdapter mAdapter;
    private FrameLayout mFl_add_dynamicFm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View recomment = View.inflate(BaseApplication.getContext(), R.layout.fm_dynamic, null);
        mViewFlipper = (ViewFlipper)recomment.findViewById(R.id.viewFlipper);
        flipperRadioGroup = (RadioGroup)recomment.findViewById(R.id.radioGroup_flipper);
        mLv_dynamic = (ListView)recomment.findViewById(R.id.lv_dynamic);
        mMyFlipper = new MyFlipper(getActivity(), mViewFlipper, flipperRadioGroup);
        initData();
        mViewFlipper.setOnClickListener(null);
        mViewFlipper.setOnTouchListener((View.OnTouchListener)getActivity());
        return recomment;
    }

    private void initData()
    {
        mAdapter = new DynamicFmAdapter();
        mLv_dynamic.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(mLv_dynamic);
    }

    @Override
    protected void initPresenter()
    {

    }

    /**
     * 下载轮播图片
     */
    public void downloadImage()
    {
        if(mMyFlipper == null)
        {
            return;
        }

        String urls[] = {"http://img0.imgtn.bdimg.com/it/u=68762416,2618495342&fm=21&gp=0.jpg", "http://img5.imgtn.bdimg.com/it/u=2812890543,4218476304&fm=21&gp=0.jpg"};
        for(int i = 0; i<urls.length; i++)
        {
            FlipImage flipImage = new FlipImage(i, urls[i]);

            mMyFlipper.downloadImage(flipImage);
        }
        Log.i("down", this.toString());
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
//                startActivity(new Intent(getContext(), PublishDynamicFmActivity.class));
        }
    }

    class DynamicFmAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return 3;
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder = null;
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = View.inflate(getContext(), R.layout.item_listview_dynamic, null);
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            //           View view =  View.inflate(getContext(), R.layout.item_listview_dynamic, null);
            return convertView;
        }
    }

    public static class ViewHolder
    {

    }

    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        if(listView == null)
        {
            return;
        }
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null)
        {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for(int i = 0; i<listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+( listView.getDividerHeight()*( listAdapter.getCount()-1 ) );
        listView.setLayoutParams(params);
    }

    public void showNext()
    {
        mViewFlipper.showNext();
    }

    public void showPrevious()
    {
        mViewFlipper.showPrevious();
    }

}
