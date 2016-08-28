package com.taoy3.freight.util;

import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.taoy3.freight.view.AutoScrollViewPager;


/**
 * Created by taoy2 on 15-11-2.
 */
public class PagerRadioUtils implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    private AutoScrollViewPager pager;
    private RadioGroup group;
    private RadioButton buttons[];

    public PagerRadioUtils(AutoScrollViewPager pager, RadioGroup group) {
        this.pager = pager;
        this.group = group;
        this.buttons = new RadioButton[group.getChildCount()];
        for (int i = 0; i < group.getChildCount();i++){
            buttons[i] = (RadioButton) group.getChildAt(i);
        }
        buttons[0].setChecked(true);
    }
    public void setChangeListener(){
        pager.addOnPageChangeListener(this);
        group.setOnCheckedChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        buttons[position%group.getChildCount()].setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {
        pager.stopAutoScroll();
        for (int i = 0; i < buttons.length; i++) {
            if(buttons[i].getId()==id){
                pager.setCurrentItem(i);
                pager.startAutoScroll(5000);
            }
        }
    }
}
