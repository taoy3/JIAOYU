package com.taoy3.freight.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/12/2.
 */
public class DateViewUtils {
    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_DATAPICK = 0;
    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static AppCompatActivity activity;
    private static TextView dateView;
    public static void setDateView(AppCompatActivity activity, TextView dateView) {
        DateViewUtils.activity = activity;
        DateViewUtils.dateView = dateView;
        dateView.setOnClickListener(new DateButtonOnClickListener());
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        setDateTime();
    }
    private static void setDateTime() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
    }
    //更新日期
    private static void updateDisplay() {
        dateView.setText(new StringBuilder().append(mYear).append("年").append(
                (mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("月").append(
                (mDay < 10) ? "0" + mDay : mDay).append("日"));
    }
    //日期控件的事件
    private static DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }

    };
    //选择日期Button的事件处理
    static class DateButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Message msg = new Message();
            if (dateView.equals(v)) {
                msg.what = DateViewUtils.SHOW_DATAPICK;
            }
            DateViewUtils.saleHandler.sendMessage(msg);
        }
    }
    protected static Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DATE_DIALOG_ID:
                dialog = new DatePickerDialog(activity, mDateSetListener, mYear, mMonth,
                        mDay);
                return dialog;
        }
        onPrepareDialog(DATE_DIALOG_ID,dialog);
        return null;
    }
    protected static void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }
    //处理日期控件的Handler
    static Handler saleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DateViewUtils.SHOW_DATAPICK:
//                    activity.showDialog(DATE_DIALOG_ID);
                    onCreateDialog(DATE_DIALOG_ID);
                    break;
            }
        }
    };
}
