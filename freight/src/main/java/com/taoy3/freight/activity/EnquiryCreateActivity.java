package com.taoy3.freight.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.taoy3.freight.R;
import com.taoy3.freight.bean.EnquiryBean;
import com.taoy3.freight.util.SQLUtils;

import java.util.Date;


public class EnquiryCreateActivity extends BaseActivity{
    private EditText remarkEt;
    private String startPort;
    private TextView startPortTv;
    private String destPort;
    private TextView destPortTv;
    private EnquiryBean bean= new EnquiryBean();

    @Override
    protected void setView() {
        setContentView(R.layout.activity_enquiry_create);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        remarkEt = (EditText) findViewById(R.id.cus_deatil_remark);
        startPortTv = (TextView) findViewById(R.id.start_port);
        destPortTv = (TextView) findViewById(R.id.dest_port);
        TextView submitView = (TextView) findViewById(R.id.title_right);
        submitView.setText(R.string.submit);
        submitView.setOnClickListener(this);
        findViewById(R.id.start_port).setOnClickListener(this);
        findViewById(R.id.dest_port).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        bean.setType(getIntent().getIntExtra(TYPE,0));
        bean.setState(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_port:
                startActivityForResult(new Intent(this, PortActivity.class), STARTPORTCODE);
                break;
            case R.id.dest_port:
                startActivityForResult(new Intent(this, PortActivity.class), DESTPORTCODE);
                break;
            case R.id.title_right:
                Toast.makeText(this,startPort+"-"+destPort+"-"+remarkEt.getText().toString(),Toast.LENGTH_SHORT).show();
                bean.setStart(startPort);
                bean.setDest(destPort);
                bean.setRemarks(remarkEt.getText().toString());
                bean.setApplydate(new Date());
                try {
                    SQLUtils.dataDb.save(bean);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                showDialog();
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_submit_enquiry);
        dialog.setCancelable(false);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                setResult(LOCKCODE, new Intent().putExtra("enquiry", bean));
                dialog.dismiss();
                finish();
            }
        }).start();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            switch (resultCode){
                case STARTPORTCODE:
                    startPort = data.getStringExtra(STARTPORT);
                    startPortTv.setText(startPort);
                    break;
                case DESTPORTCODE:
                    destPort = data.getStringExtra(DESTPORT);
                    destPortTv.setText(destPort);
                    break;
                default:
                    break;
            }
        }
    }
}
