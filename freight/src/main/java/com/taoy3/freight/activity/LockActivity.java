package com.taoy3.freight.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.taoy3.freight.R;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.view.LockPatternView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class LockActivity extends AppCompatActivity implements LockPatternView.OnPatternListener {

    private List<LockPatternView.Cell> lockPattern;
    private LockPatternView lockPatternView;
    private int times;
    private TextView tipTv;
    private int taskTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (CacheDataConstant.choosePattern == null) {
            finish();
        }
        lockPattern = CacheDataConstant.choosePattern;
        setContentView(R.layout.activity_lock);
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);
        tipTv = (TextView) findViewById(R.id.lock_tip);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        times++;
        if (pattern.equals(lockPattern)) {
            finish();
        } else if(times==1){
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            Toast.makeText(this, R.string.lock_pattern_error_1, Toast.LENGTH_SHORT).show();
        }else if(times==2){
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            Toast.makeText(this, R.string.lock_pattern_error_2, Toast.LENGTH_SHORT).show();
        }else {
            lockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            Toast.makeText(this, R.string.lock_pattern_error_0, Toast.LENGTH_SHORT).show();
            lockPatternView.disableInput();
            times=0;
            final Timer timer = new Timer();
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            taskTime++;
                            tipTv.setText("还有"+(60- taskTime)+"秒");
                            if(taskTime ==60){
                                lockPatternView.enableInput();
                                tipTv.setText(R.string.enter_the_password_for_gestures);
                                taskTime =0;
                                timer.cancel();
                            }
                        }
                    });
                }
            },0,1000);
        }
        lockPatternView.clearPattern();
    }
}
