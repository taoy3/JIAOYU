package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.OnlineBean;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.materialpager.OnlinePager;
import com.cnst.wisdom.ui.widget.PagerSlidingTabStrip;
import com.cnst.wisdom.utills.SPUtills;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <在线进修>
 * <在线进修相关信息展示>
 *
 * @author pengjingang.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 *
 */

public class OnlineLearn extends BaseNetActivity implements View.OnClickListener
{
    private ViewPager mVpOnline;
    private PagerSlidingTabStrip mTabstripOnline;
    private DisplayMetrics mDm;
    private String[] mTitles;
    private RelativeLayout mHeadBack;
    private ImageButton mHeadMenu;
    private TextView mBtnPop, mBtnSearch;
    private PopupWindow mPopupWindow;
    private VolleyManager volleyManager=VolleyManager.getInstance();
    private ArrayList<OnlineBean.OnlineData> onlineDatas;
    private ArrayList<OnlinePager>mOnlinePagers;
    private String[] mTabTitlearr;
    private View mContentView;
    private FrameLayout mFrameLayout;

    /**
     * 屏幕密度
     */
    private float density;


    @Override
    protected void setLayout() {
        mContentView = getLayoutInflater().inflate(R.layout.activity_onlinelearn,null);
        setContentView(mContentView);
        density = getResources().getDisplayMetrics().density;
        mDm = getResources().getDisplayMetrics();
        initView();
        initListener();
        getDataFromServer();
    }

    @Override
    protected void initData() {

    }

    private void getDataFromServer()
    {
        HashMap map = new HashMap();
        map.put("fcode", "zxjx");
        volleyManager.getString(SPUtills.get(getApplicationContext(), Constants.GET_SERVER, "").toString()+Constants
                .MATERIAL_DICTLIST, map, "Online", new NetResult<String>()
        {
            @Override
            public void onFailure(VolleyError error)
            {
                Log.e("online", "访问网络失败");
            }

            @Override
            public void onSucceed(String response)
            {
                Gson gson = new Gson();
                OnlineBean onlineBean = null;
                onlineBean = gson.fromJson(response, OnlineBean.class);
                if(onlineBean != null)
                {
                    Log.e("online", response);
                    int code = onlineBean.getCode();
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
                            onlineDatas = onlineBean.getData();
                            if(onlineDatas != null)
                            {
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
    /**
     * 解析网络数据
     */
    private void initServiceData()
    {
        mOnlinePagers=new ArrayList<>();
        mTabTitlearr = new String[onlineDatas.size()];
        for(int i=0;i<onlineDatas.size();i++){
            OnlineBean.OnlineData onlineData = onlineDatas.get(i);
            OnlinePager onlinePager=new OnlinePager(this,onlineData.getCode());
            mTabTitlearr[i]=onlineData.getName();
            mOnlinePagers.add(onlinePager);
        }

        mVpOnline.setAdapter(new OnlinePagerAdapter());
        mTabstripOnline.setViewPager(mVpOnline);
        mOnlinePagers.get(0).initData();
    }



    private void initListener()
    {
        mTabstripOnline.setOnPageChangeListener(new MyOnpagerChangerListener());
    }


    protected void initView()
    {
        mVpOnline = (ViewPager)findViewById(R.id.vp_online);
        mFrameLayout = (FrameLayout)findViewById(R.id.fl_head_more_action);


        mTabstripOnline = (PagerSlidingTabStrip)findViewById(R.id.tabstrip_online);
        /*
           设置tabs标签的一些属性
        */
        mTabstripOnline.setTextColor(Color.parseColor("#333333"));
        //设置标签自动扩展——当标签们的总宽度不够屏幕宽度时，自动扩展使每个标签宽度递增匹配屏幕宽度，注意！这一条代码必须在setViewPager前方可生效
        mTabstripOnline.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        mTabstripOnline.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        mTabstripOnline.setUnderlineHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1, mDm));
        // 设置Tab Indicator的高度
        mTabstripOnline.setIndicatorHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mDm));
        // 设置Tab标题文字的大小
        mTabstripOnline.setTextSize((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mDm));
        // 设置Tab Indicator的颜色
        mTabstripOnline.setIndicatorColor(getResources().getColor(R.color.colorPrimary));
        // 设置选中Tab文字的颜色
        mTabstripOnline.setSelectedTextColor(getResources().getColor(R.color.colorPrimary));
        // 取消点击Tab时的背景色
        mTabstripOnline.setTabBackground(0);
        //左右间隔尽可能小，一边可以自动扩展，使每个标签均匀平分屏幕宽度
        mTabstripOnline.setTabPaddingLeftRight(1);

    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.fl_head_more_action:
                //弹出pop
                initPopView();
//                String  margin = ( (RelativeLayout.LayoutParams)mHeadMenu.getLayoutParams() ).bottomMargin+"";
//
//                Log.e("---------",margin);
                break;
            case R.id.btn_pop:
                Intent intent = new Intent(this, Feedback.class);
                startActivity(intent);
                mPopupWindow.dismiss();

                break;
            case R.id.pop_item_search:
                Intent intent_search = new Intent(this, MaterialSearch.class);
                startActivity(intent_search);
                mPopupWindow.dismiss();



            default:
                break;
        }

    }

    private void initPopView()
    {
        View view = View.inflate(this, R.layout.popup_online, null);
        mBtnPop = (TextView)view.findViewById(R.id.btn_pop);
        mBtnPop.setOnClickListener(this);

        mBtnSearch = (TextView)view.findViewById(R.id.pop_item_search);
        mBtnSearch.setOnClickListener(this);

        mPopupWindow = new PopupWindow(view, (int)( 115*density ), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(true);
        //点击pop以外，pop消失
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置动画效果
        mPopupWindow.setAnimationStyle(android.support.v7.appcompat.R.style.Animation_AppCompat_DropDownUp);


        mPopupWindow.showAsDropDown(mHeadMenu,0, (int)( 10*density ));
//        mPopupWindow.showAtLocation(mHeadMenu, Gravity.BOTTOM, -40, 0);
        mContentView.setBackgroundColor(getResources().getColor(R.color.popup_main_background));
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss()
            {
                mContentView.setBackgroundColor(Color.TRANSPARENT);
            }
        });


    }



    /**
     *ViewPager的适配器
     */
    private class OnlinePagerAdapter extends PagerAdapter
    {


        @Override
        public CharSequence getPageTitle(int position)
        {
            return mTabTitlearr[position];
        }

        @Override
        public int getCount()
        {
            return mOnlinePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            OnlinePager onlinePager=mOnlinePagers.get(position);
            container.addView(onlinePager.mRootView);
            return onlinePager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View)object);
        }
    }

    private class MyOnpagerChangerListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {

        }

        @Override
        public void onPageSelected(int position)
        {
            if(mOnlinePagers!=null){
                mOnlinePagers.get(position).initData();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {

        }
    }

}
