package com.taoy3.freight.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.taoy3.freight.activity.MainActivity;

/**
 * Created by taoy2 on 15-12-22.
 */
public class MyBroadcastReceive extends BroadcastReceiver {
    static final String action_boot="android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "afdasfasdfasdf", 0).show();
        if (intent.getAction().equals(action_boot)){
            Intent StartIntent=new Intent(context,MainActivity.class); //接收到广播后，跳转到MainActivity
            StartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(StartIntent);
        }
    }
}
