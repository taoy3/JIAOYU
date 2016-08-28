package com.cnst.wisdom.ui.activity;

import android.widget.TextView;

import com.cnst.wisdom.R;

public class ReplyParentsActivity extends PublishDynamicFmActivity {

    @Override
    protected void onStart() {
        TextView titleView = (TextView) findViewById(R.id.head_text);
        titleView.setText("回复家长");
        mEt_content.setHint("说点什么吧");
        super.onStart();
    }
}
