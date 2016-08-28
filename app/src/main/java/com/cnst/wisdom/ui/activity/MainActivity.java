package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseFragment;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.ui.fragment.DynamicFm;
import com.cnst.wisdom.ui.fragment.FmFactory;
import com.cnst.wisdom.ui.fragment.MeFm;
import com.cnst.wisdom.ui.fragment.TeachFm;
import com.cnst.wisdom.ui.view.NoScrollViewPager;

/**
 * 主界面 viewpager依赖的activity
 *
 * @author jiangzuyun.
 * @see [put，get]
 * @since [产品/模版版本]
 */
public class MainActivity extends BaseNetActivity implements View.OnClickListener, GestureDetector.OnGestureListener, View.OnTouchListener {

    private NoScrollViewPager mViewPager;
    //分别为  消息 教学 动态  班务
    private int[] pagers = {0, 1, 2, 3};
    //分别为  消息 教学 动态  班务
    private int[] checkedId = {R.id.radio0, R.id.radio1, R.id.radio2, R.id.radio3};
    private RadioGroup mRadioGroup;
    private MyFragmentAdapter mAdapter;
    private MeFm mMeFm;
    private DynamicFm mDynamicFm;
    private TeachFm mTeachFm;
    private boolean pageIsShowed[] = {false, false, false, false};
    public static GestureDetector mDetector;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_main);
        mViewPager = (NoScrollViewPager) findViewById(R.id.vp);
        //禁止左右滑动
        mViewPager.setNoScroll(true);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        mAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        MyOnpagerChangerListener listener = new MyOnpagerChangerListener();
        mViewPager.setOnPageChangeListener(listener);
        mViewPager.setOffscreenPageLimit(5);
        setOnClickeners();
        mViewPager.setCurrentItem(2);

        mMeFm = (MeFm) mAdapter.getItem(3);
        mTeachFm = (TeachFm) mAdapter.getItem(2);
        mDynamicFm = (DynamicFm) mAdapter.getItem(1);

        mDetector = new GestureDetector(getApplicationContext(), this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    private void setOnClickeners() {
        findViewById(R.id.radio0).setOnClickListener(this);
        findViewById(R.id.radio1).setOnClickListener(this);
        findViewById(R.id.radio2).setOnClickListener(this);
        findViewById(R.id.radio3).setOnClickListener(this);

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int flipper_location[] = new int[2];
        mTeachFm.getView().findViewById(R.id.viewFlipper).getLocationInWindow(flipper_location);
        int flipper_h = mTeachFm.getView().findViewById(R.id.viewFlipper).getHeight();
        if (!(e1.getY() >= flipper_location[1] && e1.getY() <= flipper_location[1] + flipper_h)) {
            return false;
        }
        if (e1.getX() - e2.getX() > 15) {
            switch (mViewPager.getCurrentItem()) {
                case 1:
                    mDynamicFm.showNext();
                    break;
                case 2:
                    mTeachFm.showNext();
                    break;
                case 3:
                    mMeFm.showNext();
                    break;
            }
        } else if (e1.getX() - e2.getX() < -15) {
            switch (mViewPager.getCurrentItem()) {
                case 1:
                    mDynamicFm.showPrevious();
                    break;
                case 2:
                    mTeachFm.showPrevious();
                    break;
                case 3:
                    mMeFm.showPrevious();
                    break;
            }
        }
        return false;
    }

    class MyFragmentAdapter extends FragmentStatePagerAdapter {

        private final FmFactory mFactory;

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
            mFactory = new FmFactory();
        }

        @Override
        public BaseFragment getItem(int position) {

            return mFactory.createFragment(pagers[position]);
        }

        @Override
        public int getCount() {
            return pagers.length;
        }
    }

    class MyOnpagerChangerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            int id = 0;
            switch (position) {
                case 0:
                    id = R.id.radio0;
                    break;
                case 1:
                    id = R.id.radio1;
                    if (!pageIsShowed[position]) {
                        mDynamicFm.downloadImage();
                    }
                    break;
                case 2:
                    id = R.id.radio2;
                    break;
                case 3:
                    id = R.id.radio3;
                    if (!pageIsShowed[position]) {
                        mMeFm.downloadImage();
                    }
                    break;

            }
            pageIsShowed[position] = true;
            mRadioGroup.check(id);
        }
    }

    @Override
    public void onClick(View v) {
        int index = -1;
        switch (v.getId()) {
            case R.id.radio0:
                index = 0;
                break;
            case R.id.radio1:
                index = 1;
                break;
            case R.id.radio2:
                index = 2;
                break;
            case R.id.radio3:
                index = 3;
                break;

            default:
                break;
        }
        mViewPager.setCurrentItem(index);
    }

    //    教学计划
    public void teackplan(View v) {
        jumpTo(TeachPlan.class);
    }

    //课前指导
    public void guidance(View v) {
        jumpTo(Guidance.class);
    }

    //    在线进修
    public void onlinelearning(View v) {
        jumpTo(OnlineLearn.class);
    }

    //    资源素材
    public void material(View v) {
        jumpTo(Material.class);
    }

    //    考勤管理
    public void attendance(View v) {
        jumpTo(Attendance.class);
    }

    //    健康管理
    public void health(View v) {
        jumpTo(HealthManage.class);
    }

    //个人中心
    public void account(View v) {
        jumpTo(AccountActivity.class);
    }

    //家长嘱咐
    public void parents(View v) {
        jumpTo(ParentsTip.class);
    }

    //成长档案
    public void profile(View v) {
        jumpTo(ProfileActivity.class);
    }

    public void jumpTo(Class clazz) {
        Intent starter = new Intent(this, clazz);
        startActivity(starter);
    }

    /**
     * <返回键>
     * 按下返回键：显示桌面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return this.mDetector.onTouchEvent(event);
    }


}
