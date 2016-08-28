package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.bean.MedicineTipBean;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.adapter.ImageAdapter;
import com.cnst.wisdom.ui.view.CircleImageView;
import com.cnst.wisdom.ui.view.SwitchButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MedicineTipDetailAt extends BaseNetActivity implements View.OnClickListener, SwitchButton.SlideListener {
    private String id;
    private TextView answerTv;
    private RelativeLayout answerLy;
    private VolleyManager volleyManager = VolleyManager.getInstance();
    private String TAG = getClass().getName();
    private TextView clazzTv;
    private TextView tipTv;
    private TextView nameTv;
    private TextView timesTv;
    private TextView timeTv;
    private TextView thisTv;
    private CircleImageView headView;
    private TextView teacherTv;
    private TextView answerTimeTv;
    private TextView msgTv;
    private GridView pictureGv;
    public static final int PUBLISH =1;
    private ArrayList<String> listfile = new ArrayList<>();
    private ImageAdapter adapter;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_medicine_tip_detail);
        id = getIntent().getStringExtra("id");
    }

    protected void initView() {
        clazzTv = (TextView) findViewById(R.id.medicine_detail_clazz);
        nameTv = (TextView) findViewById(R.id.medicine_detail_name);
        tipTv = (TextView) findViewById(R.id.medicine_detail_tip);
        timesTv = (TextView) findViewById(R.id.medicine_detail_times);
        timeTv = (TextView) findViewById(R.id.medicine_detail_time);
        thisTv = (TextView) findViewById(R.id.medicine_detail_this);
        SwitchButton switchButton = (SwitchButton) findViewById(R.id.medicine_detail_state);
        switchButton.setSlideListener(this);
        answerTv = (TextView) findViewById(R.id.medicine_detail_answer);
        answerTv.setOnClickListener(this);
        answerLy = (RelativeLayout) findViewById(R.id.medicine_detail_answer_layout);

        findViewById(R.id.medicine_detail_del).setOnClickListener(this);
        headView = (CircleImageView) findViewById(R.id.imageView_userhead);
        teacherTv = (TextView) findViewById(R.id.tv_name);
        answerTimeTv = (TextView) findViewById(R.id.tv_date);
        msgTv = (TextView) findViewById(R.id.tv_massage);
        pictureGv = (GridView) findViewById(R.id.medicine_detail_grid);
        pictureGv.setNumColumns(3);
        adapter = new ImageAdapter(this,listfile);
        pictureGv.setAdapter(adapter);
    }

    protected void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("id",id);
        volleyManager.getString("", map, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {

            }

            @Override
            public void onSucceed(String response) {
                if (response != null && response.length() > 0) {
                    Gson gson = new Gson();
                    try {
                        JSONObject object = new JSONObject(response);
                        String plan = object.getString("data");
                        MedicineTipBean bean = gson.fromJson(plan, MedicineTipBean.class);
                        clazzTv.setText(bean.getClazz());
                        nameTv.setText(bean.getName());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.head_back_action:
                finish();
                break;
            case R.id.medicine_detail_answer:
                startActivityForResult(new Intent(this, ReplyParentsActivity.class), PUBLISH);
                break;
            case R.id.medicine_detail_del:
                delReply();
                break;
            default:
                break;
        }
    }

    private void delReply() {
        answerTv.setVisibility(View.VISIBLE);
        answerLy.setVisibility(View.GONE);
    }
    private void switchReplyView(boolean isReply){

    }

    @Override
    public void onChanged(boolean isOpen) {
        switchReplyView(isOpen);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();
        answerTv.setVisibility(View.GONE);
        answerLy.setVisibility(View.VISIBLE);
    }
}
