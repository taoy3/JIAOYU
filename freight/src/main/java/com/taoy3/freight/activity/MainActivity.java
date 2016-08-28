package com.taoy3.freight.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.taoy3.freight.BaseApp;
import com.taoy3.freight.R;
import com.taoy3.freight.constant.CacheDataConstant;
import com.taoy3.freight.constant.Config;
import com.taoy3.freight.util.Messaging;
import com.taoy3.freight.view.slideview.SlidingMenu;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity {
    private SlidingMenu menu;
    private TextView userTv;
    private CircleImageView headView;
    private TextView customerTv;
    private Dialog contactDialog;
    private Button qqBt;
    private Button weiChatBt;


    @Override
    protected void setView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        Messaging.register(this);
        findViewById(R.id.start_searchView).setOnClickListener(this);
        findViewById(R.id.main_order).setOnClickListener(this);
        findViewById(R.id.online_serve).setOnClickListener(this);
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setBehindOffsetRes(R.dimen.sliding_menu_offset);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.sliding_menu);
        menu.setBehindScrollScale(0.5f);
        menu.setSlidingAnim();
        findViewById(R.id.main_slide).setOnClickListener(this);
        findViewById(R.id.unDo).setOnClickListener(this);
        findViewById(R.id.fail).setOnClickListener(this);
        findViewById(R.id.pass).setOnClickListener(this);
        findViewById(R.id.main_voyage).setOnClickListener(this);
        findViewById(R.id.main_freight).setOnClickListener(this);
        findViewById(R.id.main_enquiry).setOnClickListener(this);
        findViewById(R.id.my_customers_layout).setOnClickListener(this);
        userTv = (TextView) findViewById(R.id.user);
        headView = (CircleImageView) findViewById(R.id.head_view);
        customerTv = (TextView) findViewById(R.id.customer_quantity);
        userTv.setOnClickListener(this);
        menu.findViewById(R.id.menu_setting).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if(BaseApp.loginStatus==BaseApp.LOGINFAIL){
            userTv.setText(R.string.logining);
        }else if(BaseApp.loginStatus==BaseApp.UNLOGIN){
            userTv.setText(R.string.not_login);
        }
    }
    public void onEvent(Integer msg,Integer who) {
        switch (who){
            case 0:
                if (BaseApp.loginStatus==BaseApp.LOGINOK) {
                    if (CacheDataConstant.userBean != null) {
                        userTv.setText(CacheDataConstant.userBean.getUsername());
                    }
                    File file = new File(Config.photoPath, CacheDataConstant.userBean.getUsername() + ".png");
                    if (file.exists()) {
                        headView.setImageURI(Uri.fromFile(file));
                    }
                } else {
                    userTv.setText(msg);
                }
                break;
            case 1:
                customerTv.setText("已管理" + CacheDataConstant.customers.size() + "位");
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_slide:
                menu.showMenu();
                break;
            case R.id.unDo:
                startActivityForResult(new Intent(this, OrderListActivity.class).putExtra(TYPE, OrderListActivity.UNDO), LOCKCODE);
                break;
            case R.id.fail:
                startActivityForResult(new Intent(this, OrderListActivity.class).putExtra(TYPE, OrderListActivity.REFUSE), LOCKCODE);
                break;
            case R.id.pass:
                startActivityForResult(new Intent(this, OrderListActivity.class).putExtra(TYPE, OrderListActivity.PASS), LOCKCODE);
                break;
            case R.id.main_voyage:
                startActivityForResult(new Intent(this, VoyageActivity.class), LOCKCODE);
                overridePendingTransition(R.anim.alph_enter, R.anim.alph_exit);
                break;
            case R.id.main_freight:
                Intent intent = new Intent(this, FreightActivity.class);
                startActivityForResult(intent, LOCKCODE);
                overridePendingTransition(R.anim.alph_enter, R.anim.alph_exit);
                break;
            case R.id.main_enquiry:
                startActivityForResult(new Intent(this, EnquiryActivity.class), LOCKCODE);
                overridePendingTransition(R.anim.alph_enter, R.anim.alph_exit);
                break;
            case R.id.menu_setting:
                startActivityForResult(new Intent(this, SettingActivity.class), SETTINGCODE);
                break;
            case R.id.my_customers_layout:
                startActivityForResult(new Intent(this, CusActivity.class).putExtra(TYPE, CUSTOMER), CUSMANAGECODE);
                break;
            case R.id.user:
                if (BaseApp.loginStatus == BaseApp.LOGINOK) {
                    startActivityForResult(new Intent(this, UserInfoActivity.class), USERINFOCODE);
                } else if (BaseApp.loginStatus == BaseApp.LOGINING) {
                    Toast.makeText(this, R.string.logining, Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(new Intent(this, LoginActivity.class), USERINFOCODE);
                }
                break;
            case R.id.start_searchView:
                startActivityForResult(new Intent(this, OrderSearchActivity.class), LOCKCODE);
                break;
            case R.id.main_order:
                startActivityForResult(new Intent(this, BookFastActivity.class), LOCKCODE);
                break;
            case R.id.online_serve:
                showContactDialog();
                break;
            default:
                break;
        }
        if(v==qqBt)
        {
            String url="mqqwpa://im/chat?chat_type=wpa&uin=1240597256";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        if(v==weiChatBt)
        {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
            startActivity(intent);
        }
    }

    private void showContactDialog() {
        if(contactDialog==null)
        {
            contactDialog = new Dialog(this,R.style.NotTitleDialog);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            contactDialog.setContentView(linearLayout);
            qqBt = new Button(this);
            qqBt.setGravity(Gravity.CENTER);
            qqBt.setBackgroundResource(0);
            qqBt.setPadding(10,10,10,10);
            qqBt.setText(R.string.qq);
            qqBt.setOnClickListener(this);
            linearLayout.addView(qqBt, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT));
            weiChatBt = new Button(this);
            weiChatBt.setGravity(Gravity.CENTER);
            weiChatBt.setBackgroundResource(0);
            weiChatBt.setPadding(10,10,10,10);
            weiChatBt.setText(R.string.weichat);
            weiChatBt.setOnClickListener(this);
            linearLayout.addView(weiChatBt, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT));
        }
        contactDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case USERINFOCODE:
                if (CacheDataConstant.userBean != null) {
                    userTv.setText(CacheDataConstant.userBean.getUsername());
                }
                File file = new File(Config.photoPath, CacheDataConstant.userBean.getUsername() + ".png");
                if (file.exists()) {
                    headView.setImageURI(Uri.fromFile(file));
                }
                break;
            case CUSMANAGECODE:
                customerTv.setText("已管理" + CacheDataConstant.customers.size() + "位");
                break;
            case SETTINGCODE:
                if(BaseApp.loginStatus==BaseApp.LOGOUT){
                    userTv.setText(R.string.not_login);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Messaging.unregister(this);
    }
}
