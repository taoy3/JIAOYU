package com.cnst.wisdom.ui.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cnst.wisdom.R;

/**
 * Created by Jonas on 2016/3/8.
 */
public class GenderDialog extends Dialog implements View.OnClickListener, DialogInterface.OnShowListener
{
    private final AppCompatActivity mActivity;
    private RelativeLayout relativeLayout_male;
    private RelativeLayout relativeLayout_female;
    private ImageView imageView_male;
    private ImageView imageView_female;
    private Button confirmBt;

    //记录选择性别 0=男 1=女 -1=未设置
    private int gender;
    //用于返回结果的性别选择
    private int result;

    public GenderDialog(AppCompatActivity activity, int result)
    {
        super(activity, R.style.no_title_dialog);
        this.mActivity = activity;
        this.gender = result;
        this.result = result;
        init();
    }

    private void init()
    {
        setContentView(R.layout.dialog_gender);
        Window window = getWindow();
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        window.setBackgroundDrawableResource(R.drawable.bg_dialog);
        window.setLayout((int)( width*0.8 ), ViewGroup.LayoutParams.WRAP_CONTENT);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            window.setClipToOutline(true);
        }
        relativeLayout_male = (RelativeLayout)findViewById(R.id.relativeLayout_male);
        relativeLayout_male.setOnClickListener(this);
        relativeLayout_female = (RelativeLayout)findViewById(R.id.relativeLayout_female);
        relativeLayout_female.setOnClickListener(this);

        imageView_male = (ImageView)findViewById(R.id.imageView_male);
        imageView_female = (ImageView)findViewById(R.id.imageView_female);

        findViewById(R.id.cancel).setOnClickListener(this);
        confirmBt = (Button)findViewById(R.id.confirm);
        confirmBt.setOnClickListener(this);
        confirmBt.setClickable(false);
        setOnShowListener(this);

        selectGender(gender);
    }


    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                result = gender;
                dismiss();
                break;
            case R.id.relativeLayout_male:
                selectGender(0);
                break;
            case R.id.relativeLayout_female:
                selectGender(1);
                break;
        }
    }

    @Override
    public void onShow(DialogInterface dialog)
    {

    }

    /**
     * <选择性别>
     * 根据点击按钮修改gender t=male f=female
     * 修改按钮样式
     */
    private void selectGender(int gender)
    {
        this.gender = gender;
        if(gender == 0)
        {
            imageView_male.setImageResource(android.R.drawable.radiobutton_on_background);
            imageView_female.setImageResource(android.R.drawable.radiobutton_off_background);

        }else if(gender == 1)
        {
            imageView_male.setImageResource(android.R.drawable.radiobutton_off_background);
            imageView_female.setImageResource(android.R.drawable.radiobutton_on_background);
        }
        if(!confirmBt.isClickable()&&(gender==1||gender==0))
        {
            confirmBt.setClickable(true);
            confirmBt.setBackgroundResource(R.drawable.bg_bottom_right_clickable);
            confirmBt.setTextColor(mActivity.getResources().getColor(R.color.white));
        }
    }

    public int getGender()
    {
        return result;
    }
}
