package com.taoy3.freight.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by Administrator on 15-7-21.
 */
public class FragmentTabUtils implements RadioGroup.OnCheckedChangeListener {

    private Fragment[] fragments;
    private RadioGroup radioGroup;
    private RadioButton radioButtons[];
    private FragmentManager fragmentManager;
    private int fragmentContentId;
    private int currentTab;
    private OnRgsExtraCheckedChangedListener listener;

    public FragmentTabUtils(Fragment[] fragments, RadioGroup radioGroup, FragmentManager fragmentManager, int fragmentContentId) {
        this.fragments = fragments;
        this.radioGroup = radioGroup;
        this.fragmentManager = fragmentManager;
        this.fragmentContentId = fragmentContentId;
        this.radioButtons = new RadioButton[radioGroup.getChildCount()];
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i] = (RadioButton) radioGroup.getChildAt(i);
        }
        radioButtons[0].setChecked(true);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(fragmentContentId, fragments[0]);
        transaction.commit();
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            if (radioGroup.getChildAt(i).getId() == checkedId) {
                Fragment fragment = fragments[i];
                FragmentTransaction transaction = obtainFragmentTransaction(i);
                getCurrentFragment().onStop();
                if (fragment.isAdded()) {
                    fragment.onStart();
                } else {
                    transaction.replace(fragmentContentId, fragment);
                    transaction.commit();
                }
                showTab(i);
                if (null != listener) {
                    listener.onRgsExtraCheckedChangedListener(radioButtons, true, i);
                }
            }else if (null != listener) {
                listener.onRgsExtraCheckedChangedListener(radioButtons, false, i);
            }

        }
    }

    private void setMargin(int i) {

    }

    public OnRgsExtraCheckedChangedListener getListener() {
        return listener;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    private void showTab(int index) {
        for (int i = 0; i < fragments.length; i++) {
            Fragment fragment = fragments[i];
            FragmentTransaction transaction = obtainFragmentTransaction(index);
            if (index == i) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
            transaction.commit();
            currentTab = index;
        }
    }

    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        return transaction;
    }

    public Fragment getCurrentFragment() {
        return fragments[currentTab];
    }

    public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener listener) {
        this.listener = listener;
    }

    public interface OnRgsExtraCheckedChangedListener {
        void onRgsExtraCheckedChangedListener(RadioButton[] radioButtons, boolean isCheck, int index);
    }
}
