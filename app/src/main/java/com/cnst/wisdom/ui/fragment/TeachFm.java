package com.cnst.wisdom.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.model.bean.FlipImage;
import com.cnst.wisdom.base.BaseNetFragment;
import com.cnst.wisdom.ui.widget.MyFlipper;

import java.util.Map;

/**
 * 教学界面
 */
public class TeachFm extends BaseNetFragment implements View.OnClickListener
{
    private ViewFlipper mViewFlipper;
    private RadioGroup flipperRadioGroup;
    private MyFlipper mMyFlipper;

    @Override
    public boolean setSuccViewShowFirse()
    {
        mLoadingView.setVisibility(View.GONE);
        return true;
    }

    @Override
    public String setRequestURL()
    {
        return null;
    }

    @Override
    public void onNetSucceed(String response)
    {
    }


    @Override
    public String setHeadTitle()
    {
        return BaseApplication.findString(R.string.btn_teach);
    }

    @Override
    public View createSucceedView()
    {
        View view = View.inflate(getContext(), R.layout.fm_teach, null);
        mViewFlipper = (ViewFlipper)view.findViewById(R.id.viewFlipper);
        flipperRadioGroup = (RadioGroup)view.findViewById(R.id.radioGroup_flipper);
        mMyFlipper = new MyFlipper(getActivity(), mViewFlipper, flipperRadioGroup);

        downloadImage();
        return view;
    }

    @Override
    public Map setparams()
    {
        return null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        initPresenter();
    }

    /**
     * 下载轮播图片
     */
    private void downloadImage()
    {
        String urls[] = {"http://www.yejs.com.cn/userfiles/2037.JPG", "http://img9.ph.126.net/WQ-sPrm3b1cdklu9CNQsuw==/6597653607331168739.jpg"};
        for(int i = 0; i<urls.length; i++)
        {
            FlipImage flipImage = new FlipImage(i, urls[i]);
            mMyFlipper.downloadImage(flipImage);
        }
        Log.i("down", this.toString());

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
