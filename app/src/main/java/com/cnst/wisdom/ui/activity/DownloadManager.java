package com.cnst.wisdom.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.MaterialBean;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.materialpager.DownloadManagerPager;
import com.cnst.wisdom.ui.widget.PagerSlidingTabStrip;
import com.cnst.wisdom.utills.SPUtills;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 下载管理页面
 *
 * @author Huangyingde
 * @time 15:50
 * @see
 */
public class DownloadManager extends BaseNetActivity implements View.OnClickListener
{
    private VolleyManager volleyManager = VolleyManager.getInstance();
    private PagerSlidingTabStrip mDownloadManagerTabs;
    private ViewPager mViewPager;
    private RelativeLayout mBack;
    private ImageView mMore;
    private String[] mDownloadManagerArrs;
    private ArrayList<MaterialBean.MaterialList> materialList;
    private DisplayMetrics mDm;
    private String mUrl;
    private ArrayList<DownloadManagerPager> mDownloadManagerPagersList;//资源页面数据集合
//    private FrameLayout mFl_more;
    private MyOnPagerChangeListener mMyOnPagerChangeListener = new MyOnPagerChangeListener();

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_download_manager);
        //        init();
        //        initData();
        //        initListener();
        //获取访问网络的url
        mUrl = SPUtills.get(getApplicationContext(), Constants.GET_SERVER, "").toString()+Constants.MATERIAL_DICTLIST;
        init();
        //先检查本地是否有数据如果有数据加载本地，没有数据加载网络数据
        String localData = SPUtills.get(getApplicationContext(), mUrl, "").toString();
        Log.e("222222222222", localData);
        if(!TextUtils.isEmpty(localData))
        {
            initLocalDota(localData);
        }else
        {
            initDataFormService();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    /**
     * 从本地加载缓存信息，本地如果有数据先加载本地数据
     *
     * @param localData
     */
    private void initLocalDota(String localData)
    {
        Gson gson = new Gson();
        MaterialBean materialBean = null;
        materialBean = gson.fromJson(localData, MaterialBean.class);
        if(materialBean != null)
        {
            Log.e("Material", materialBean.toString());
            int code = materialBean.code;
            switch(code)
            {
                case Constants.STATUS_ARGUMENT_ERROR:
                    break;
                case Constants.STATUS_DATA_NOTFOUND:
                    break;
                case Constants.STATUS_ILLEGAL:
                    break;
                case Constants.STATUS_SERVER_EXCEPTION:
                    break;
                case Constants.STATUS_SUCCESS:

                    materialList = materialBean.data;
                    if(materialList != null)
                    {

                        initServiceData();

                    }
                    break;
                case Constants.STATUS_TIMEOUT:
                    break;
            }
        }
    }

    /**
     * 调用网络接口获取数据
     */
    private void initDataFormService()
    {
        //网络请求参数
        HashMap map = new HashMap();
        map.put("fcode", "zysc");

        volleyManager.getString(mUrl, map, "Material", new NetResult<String>()
        {


            @Override
            public void onFailure(VolleyError error)
            {

            }

            @Override
            public void onSucceed(String response)
            {

                Gson gson = new Gson();
                MaterialBean materialBean = null;
                materialBean = gson.fromJson(response, MaterialBean.class);
                if(materialBean != null)
                {
                    Log.e("Material", materialBean.toString());
                    int code = materialBean.code;
                    switch(code)
                    {
                        case Constants.STATUS_ARGUMENT_ERROR:
                            break;
                        case Constants.STATUS_DATA_NOTFOUND:
                            break;
                        case Constants.STATUS_ILLEGAL:
                            break;
                        case Constants.STATUS_SERVER_EXCEPTION:
                            break;
                        case Constants.STATUS_SUCCESS:

                            materialList = materialBean.data;
                            if(materialList != null)
                            {
                                //第一次加载时进行网络缓存处理
                                SPUtills.put(DownloadManager.this, mUrl, response);
                                initServiceData();

                            }
                            break;
                        case Constants.STATUS_TIMEOUT:
                            break;
                    }
                }


            }
        });
    }

    private void initServiceData()
    {
        //初始化页面数据集合
        mDownloadManagerPagersList = new ArrayList<>();
        //      初始化tabs标签
        mDownloadManagerArrs = new String[materialList.size()+1];

        //       先初始化下载资源全部页面
        DownloadManagerPager downloadManagerAllPager = new DownloadManagerPager(this, "all");
        mDownloadManagerArrs[0] = "全部";
        mDownloadManagerPagersList.add(downloadManagerAllPager);

        for(int i = 0; i<materialList.size(); i++)
        {

            MaterialBean.MaterialList material = materialList.get(i);
            //根据资源分类集合动态生成资源页面
            DownloadManagerPager downloadManagerPager = new DownloadManagerPager(this, material.code);

            mDownloadManagerArrs[i+1] = material.name;

            mDownloadManagerPagersList.add(downloadManagerPager);
        }

        // 给viewPager设置适配器
        DownloadManagerPagerAdapter adapter = new DownloadManagerPagerAdapter();

        mViewPager.setAdapter(adapter);
        //        mViewPager.setOffscreenPageLimit(adapter.getCount());
        // 将Tabs和viewPager绑定
        mDownloadManagerTabs.setViewPager(mViewPager);
        initListener();
        //      初始化第一页
        mDownloadManagerPagersList.get(0).initData();
    }

    private void init()
    {
        mDm = getResources().getDisplayMetrics();
        mDownloadManagerTabs = (PagerSlidingTabStrip)findViewById(R.id.dm_tabs);
        mViewPager = (ViewPager)findViewById(R.id.dm_viewpager);

        // 设置tabs
        setTabsProperty();
    }


    private void initListener()
    {
        mBack.setOnClickListener(this);
//        mFl_more.setOnClickListener(this);
        mDownloadManagerTabs.setOnPageChangeListener(mMyOnPagerChangeListener);

        //        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        //        {
        //            @Override
        //            public void onGlobalLayout()
        //            {
        //                // 刚进入Material界面时，手动选中第一个页面
        //                mMyOnPagerChangeListener.onPageSelected(0);
        //                // mViewPager.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
        //            }
        //        });
    }

    /**
     * 设置tabs标签的一些属性
     */
    private void setTabsProperty()
    {
        mDownloadManagerTabs.setTextColor(Color.parseColor("#333333"));
        mDownloadManagerTabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        mDownloadManagerTabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        mDownloadManagerTabs.setUnderlineHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, mDm));
        // 设置Tab Indicator的高度
        mDownloadManagerTabs.setIndicatorHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mDm));
        // 设置Tab标题文字的大小
        mDownloadManagerTabs.setTextSize((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mDm));

        // 设置Tab Indicator的颜色
        mDownloadManagerTabs.setIndicatorColor(Color.parseColor("#03CA8F"));
        // 设置选中Tab文字的颜色
        mDownloadManagerTabs.setSelectedTextColor(Color.parseColor("#03CA8F"));
        // 取消点击Tab时的背景色
        mDownloadManagerTabs.setTabBackground(0);
    }

    @Override
    public void onClick(View v)
    {
        if(v == mBack)
        {
            finish();
        }else if(v == mMore)
        {
            // 彈出popupWindow
            initPopupWindow();
        }
    }

    private void initPopupWindow()
    {
        View popView = View.inflate(this, R.layout.popup_downloadmanager, null);
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        PopupWindow popupWindow = new PopupWindow(popView, width, height);
        // 设置popupWindow
        popupWindow.setFocusable(true);          // 设置可以获得焦点
        popupWindow.setOutsideTouchable(true);   // 设置点击外面popWindow可以消失
        // 需要给popWindow指定一个背景色,不然点击外面的时候不可以消失
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 给popupWindow设置动画效果
        popupWindow.setAnimationStyle(android.support.v7.appcompat.R.style.Animation_AppCompat_Dialog);

        // 让popupWindow显示在mMore的下面
        popupWindow.showAsDropDown(mMore);

        // 搜索的点击事件
        popView.findViewById(R.id.pop_download_manager).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO
            }
        });
    }

    class MyOnPagerChangeListener implements ViewPager.OnPageChangeListener
    {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
            if(mDownloadManagerPagersList != null)
            {
                mDownloadManagerPagersList.get(position).initData();
            }
        /*BaseNetFragment fragment = MaterialFragmentFactory.createFragment(position);
            if(fragment != null)
            {
                *//**
         * 开始加载数据
         *//*
                fragment.showLoadingView(true);
            }*/
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }

    /**
     * viewPage用于显示数据的自定义适配器类
     */
    class DownloadManagerPagerAdapter extends PagerAdapter
    {
        @Override
        public CharSequence getPageTitle(int position)
        {

            return mDownloadManagerArrs[position];
        }

        @Override
        public int getCount()
        {
            return mDownloadManagerPagersList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            DownloadManagerPager downloadManagerPager = mDownloadManagerPagersList.get(position);
            container.addView(downloadManagerPager.mRootView);
            return downloadManagerPager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            //            super.destroyItem(container, position, object);
            container.removeView((View)object);
        }
    }


}
