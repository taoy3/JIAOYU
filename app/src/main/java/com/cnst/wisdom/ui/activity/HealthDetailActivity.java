package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.bean.StudentBean;

/**
 * 简述
 * 详细功能描述
 *
 * @author yuhongwen
 * @time 9:51
 * @see
 */
public class HealthDetailActivity extends BaseNetActivity implements View.OnClickListener
{

    private TextView mHealthTex;
    private TextView mHealthDetail_name;
    private TextView mHealthDetail_id;
    private TextView healthDetail_gohome;
    private TextView healthDetail_temperature;
    private TextView healthDetail_redeye;
    private TextView healthDetail_diarrhea;
    private TextView healthDetail_nail;
    private TextView healthDetail_sont;
    private TextView healthDetail_rash;
    private TextView healthDetail_houyan;
    private TextView healthDetail_cough;
    private TextView healthDetail_medicine;
    private TextView healthDetail_takecare;
    private TextView mHealth_headTex;
    private StudentBean.DataInfo mStuBeanData = null;
    private ImageButton mBackRl;
    private static String TAG = "healthdetailactivity";


    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_health_deail);
        Intent intent = getIntent();
        if(intent != null && intent.getSerializableExtra(HealthManage.STUDENTENTRY) instanceof StudentBean.DataInfo)
        {
            mStuBeanData = (StudentBean.DataInfo)intent.getSerializableExtra(HealthManage.STUDENTENTRY);
            //模拟学生健康信息(这里需要考虑的是服务端:对学生信息详情是需要单独请求一次还是 直接数据封装在健康管理模块中)

            mStuBeanData.setTemperature("38");  //体温
            mStuBeanData.setIsRedeye(false);  //是否红眼
            mStuBeanData.setIsCough(true);   //是否咳嗽
            mStuBeanData.setIsHouyan(false); //是否喉炎
            mStuBeanData.setIsMedicine(true);//是否服药
            mStuBeanData.setIsDiarrhea(true); //是否腹泻
            mStuBeanData.setIsNail(false);//是否指甲
            mStuBeanData.setIsRash(false);//是否皮疹
            mStuBeanData.setIsSnot(false);//是否鼻涕
            mStuBeanData.setIsTakeCare(true);//是否重点照顾
//            mStuBeanData.getName();//学生姓名
//            mStuBeanData.getIdCard();//学生编号,注意区分学生序列号
            initView();

        }else{
            Log.i(TAG,"intent 传递失败 可能是没有clone");
        }
    }

    @Override
    protected void initData() {

    }

    protected void initView()
    {
        mHealthTex = (TextView)findViewById(R.id.health_detail_tex);
        mHealth_headTex = (TextView)findViewById(R.id.head_text);
        mHealth_headTex.setText(R.string.health_detail_head);
//        mHealthDetail_lst = (ListView)findViewById(R.id.health_datail_lst);

//        mHealthDetail_lst.setAdapter(new ArrayAdapter(this,R.layout.item_health_detail,mHealthDetailLst) {
//
//
//        });

        mHealthDetail_name= (TextView)findViewById(R.id.health_detail_name);
        mHealthDetail_name.setText(mStuBeanData.getName());

        mHealthDetail_id= (TextView)findViewById(R.id.health_detail_id);
        mHealthDetail_id.setText(mStuBeanData.getIdCard());

        healthDetail_gohome= (TextView)findViewById(R.id.health_detail_gohome);
        if(mStuBeanData.isTakeHome()){
            healthDetail_gohome.setText("带回家");
        }else{
            healthDetail_gohome.setText("不带回家");
        }

        healthDetail_temperature= (TextView)findViewById(R.id.health_detail_temperature);
        healthDetail_temperature.setText(mStuBeanData.getTemperature());

        healthDetail_redeye= (TextView)findViewById(R.id.health_detail_redeye);
        if(mStuBeanData.isRedeye()){
            healthDetail_redeye.setText("是红眼");
        }else{
            healthDetail_redeye.setText("不是红眼");
        }

        healthDetail_diarrhea= (TextView)findViewById(R.id.health_detail_diarrhea);
        if(mStuBeanData.isDiarrhea()){
            healthDetail_diarrhea.setText("是腹泻");
        }else{
            healthDetail_diarrhea.setText("不腹泻");
        }

        healthDetail_nail= (TextView)findViewById(R.id.health_detail_nail);
        if(mStuBeanData.isNail()){
            healthDetail_nail.setText("有指甲");
        }else{
            healthDetail_nail.setText("没有指甲");
        }

        healthDetail_sont= (TextView)findViewById(R.id.health_detail_sont);
        if(mStuBeanData.isSnot()){
            healthDetail_sont.setText("有鼻涕");
        }else{
            healthDetail_sont.setText("没有鼻涕");
        }

        healthDetail_rash= (TextView)findViewById(R.id.health_detail_rash);
        if(mStuBeanData.isRash()){
            healthDetail_rash.setText("有皮疹");
        }else {
            healthDetail_rash.setText("没有皮疹");
        }

        healthDetail_houyan= (TextView)findViewById(R.id.health_detail_houyan);
        if(mStuBeanData.isHouyan()){
            healthDetail_houyan.setText("有喉炎");
        }else{
            healthDetail_houyan.setText("没有喉炎");
        }

        healthDetail_cough= (TextView)findViewById(R.id.health_detail_cough);
        if(mStuBeanData.isCough()){
            healthDetail_cough.setText("有咳嗽");
        }else{
            healthDetail_cough.setText("没有咳嗽");
        }

        healthDetail_medicine= (TextView)findViewById(R.id.health_detail_medicine);
        if(mStuBeanData.isMedicine()){
            healthDetail_medicine.setText("不需要服药");
        }else{
            healthDetail_medicine.setText("需要服药");
        }

        healthDetail_takecare= (TextView)findViewById(R.id.health_detail_takecare);
        if(mStuBeanData.isTakeCare()){
            healthDetail_takecare.setText("需要重点照顾");
        }else {
            healthDetail_takecare.setText("不需要重点照顾");
        }

    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.head_back_action:
                HealthDetailActivity.this.finish();
                break;



        }
    }
}
