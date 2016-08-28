package com.taoy3.freight.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by taoy2 on 15-12-22.
 */
public class AlarmReceiver extends BroadcastReceiver{
    public final String MYTAG = "Ray";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.v(MYTAG, "I am AlarmReceiver,I receive the message");
        Intent in = new Intent();
        in.setClass(context, TestActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(in);
    }
}
