package com.taoy3.freight.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.taoy3.freight.BaseApp;
import com.taoy3.freight.R;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.constant.Config;
import com.taoy3.freight.view.LockPatternView;

import java.util.ArrayList;
import java.util.List;

public class LockSetupActivity extends AppCompatActivity implements LockPatternView.OnPatternListener{

    private LockPatternView lockPatternView;
    private TextView tipTv;

    private static final int STEP_0 = 0; // 输入旧密码密码
    private static final int STEP_1 = 1; // 开始
    private static final int STEP_2 = 2; // 第一次设置手势完成
    private static final int STEP_3 = 3; // 第二次设置手势完成

    private int step;

    private List<LockPatternView.Cell> choosePattern;

    private boolean confirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_setup);
        tipTv = (TextView) findViewById(R.id.lock_tip);
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);
        if(CacheDataConstant.choosePattern!=null){
            step = STEP_0;
            choosePattern = CacheDataConstant.choosePattern;
            tipTv.setText(R.string.scale_design_old);
        }else {
            step = STEP_1;
        }
        updateView();
    }

    private void updateView() {
        switch (step) {
            case STEP_0:
                confirm = false;
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
                break;
            case STEP_1:
                choosePattern = null;
                confirm = false;
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
                break;
            case STEP_2:
                lockPatternView.disableInput();
                lockPatternView.clearPattern();
                lockPatternView.enableInput();
                break;
            case STEP_3:
                if (confirm) {
                    lockPatternView.disableInput();
                    SharedPreferences preferences = getSharedPreferences(Config.LOCK, MODE_PRIVATE);
                    preferences.edit()
                            .putString(Config.LOCK_KEY,LockPatternView.patternToString(choosePattern))
                            .commit();
                    CacheDataConstant.choosePattern = choosePattern;
                    BaseApp.lockIsOk = true;
                    setResult(Config.LOCKCODE,new Intent());
                    finish();
                } else {
                    lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    tipTv.setText(R.string.password_not_match);
                    step = STEP_1;
                    updateView();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPatternStart() {
    }

    @Override
    public void onPatternCleared() {
    }

    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {
        if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
            tipTv.setText(R.string.lock_pattern_recording_incorrect_too_short);
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            return;
        }

        if (choosePattern == null) {
            choosePattern = new ArrayList(pattern);
            step = STEP_2;
            updateView();
            tipTv.setText(R.string.scale_design_again);
            return;
        }
        if(step==STEP_0&&choosePattern.equals(pattern)){
            step = STEP_1;
            updateView();
            tipTv.setText(R.string.input_new_password);
            return;
        }else if(step==STEP_0){
            tipTv.setText(R.string.scale_design_old_match);
            updateView();
            return;
        }
        if (choosePattern.equals(pattern)) {
            confirm = true;
        } else {
            confirm = false;
        }
        step = STEP_3;
        updateView();
    }
}
