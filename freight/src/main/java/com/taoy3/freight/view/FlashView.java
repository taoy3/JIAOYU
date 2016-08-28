package com.taoy3.freight.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import com.taoy3.freight.R;

/**
 * Created by taoy2 on 15-11-5.
 */
public class FlashView extends Dialog implements Animation.AnimationListener{
    private int flag;
    private Animation aIconUp;
    private Animation aIconDown;
    private Animation aLayoutUp;
    private Animation aLayoutDown;
    private View iconUpView;
    private View iconDownView;
    private View layoutUpView;
    private View layoutDownView;
    private Context context;

    public FlashView(Context context) {
        super(context);
        initView();
    }

    public FlashView(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    protected FlashView(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_flash,null);
        this.setContentView(R.layout.view_flash);
        
        ScaleAnimation aLine = (ScaleAnimation) AnimationUtils.loadAnimation(context, R.anim.flash_line_scale);
        aLine.setAnimationListener(this);

        aIconUp = AnimationUtils.loadAnimation(context, R.anim.flash_icon_translate_up);
        aIconDown = AnimationUtils.loadAnimation(context, R.anim.flash_icon_translate_down);
        aIconDown.setAnimationListener(this);

        aLayoutUp = AnimationUtils.loadAnimation(context, R.anim.flash_layout_translate_up);
        aLayoutDown = AnimationUtils.loadAnimation(context, R.anim.flash_layout_translate_down);
        aLayoutDown.setAnimationListener(this);

        view.findViewById(R.id.line).startAnimation(aLine);
        iconUpView = view.findViewById(R.id.icon_up);
        iconDownView = view.findViewById(R.id.icon_down);
        layoutUpView = view.findViewById(R.id.layout_up);
        layoutDownView = view.findViewById(R.id.layout_down);
    }
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        flag++;
        switch (flag){
            case 1:
                iconUpView.startAnimation(aIconUp);
                iconDownView.startAnimation(aIconDown);
                break;
            case 2:
                layoutUpView.startAnimation(aLayoutUp);
                layoutDownView.startAnimation(aLayoutDown);
                break;
            case 3:
                dismiss();
                break;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
