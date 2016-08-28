package com.cnst.wisdom.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.Login;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.utills.SPUtills;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * <登录界面>
 * 加载本地登录信息。如果存在符合“自动登录”条件的登录信息，则进行“保存密码”/“自动登录”，跳转到<主界面>。
 * 首次启动，用户需要在2个输入框内按提示分别输入<用户名/手机号>以及<登录密码>，并且有可选项<自动登录>供勾选。点击<登录>按钮进行登录信息验证。
 * 如果用户忘记了密码，可以点击<忘记密码>来通过提供相关证明信息来重置登录密码。
 * 本地保存的登录密码：明文保存
 */

public class LoginActivity extends BaseNetActivity implements View.OnClickListener {

    private EditText editText_user;
    private EditText editText_password;
    private CheckBox mCheckBox;
    private TextView textView_login;
    private TextView dialogTitle;
    private String string_user;
    private String string_password;
    private String autoLogin_time;
    private String autoLogin_checked;
    private Calendar mCalendar = Calendar.getInstance();
    private Dialog mDialog;
    private VolleyManager mVolleyManager = VolleyManager.getInstance();
    private String serverUrl;
    private ImageView imageView_welcome;
    //转到主界面
    private static final int GO_MAIN = 101;
    //首次登录转到注册页
    private static final int GO_REGISTER = 202;
    //忘记密码转到重置密码页
    private static final int GO_RESET = 303;

    private Dialog mProgressDialog;

    @Override
    protected void setLayout() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);

        if (getIntent().getBooleanExtra("logout", false)) {
            showDesktop();
            findViewById(R.id.relativeLayout_login).setVisibility(View.VISIBLE);
        } else {
            imageView_welcome = (ImageView) findViewById(R.id.imageView_welcome);
            imageView_welcome.setVisibility(View.VISIBLE);

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            imageView_welcome.setImageBitmap(
                    decodeSampledBitmapFromResource(getResources(), R.drawable.welcome, (int) (dm.widthPixels / 1.5),
                            (int) (dm.heightPixels / 1.5)));
            imageView_welcome.setAnimation(AnimationUtils.loadAnimation(this, R.anim.welcome_anim));
            imageView_welcome.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    checkLoginInfo();

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imageView_welcome.setVisibility(View.GONE);
                    recycleImage();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
        setViews();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    /**
     * <检查本地已保存的登录信息>
     * 1)如果以前没有勾选过自动登录，则跳过本方法。
     * 2)如果上次自动登录的日期距离本次登录的日期超过30天，则清空本地记录，跳过本方法。
     * 3)解密本地保存的登录密码
     * 4)将本地的登录信息发送到服务器进行验证，如果验证失败，则清空本地记录，跳过本方法。
     */

    private void checkLoginInfo() {
        autoLogin_checked = SPUtills.get(getApplicationContext(), "autoLogin_checked", "false").toString();
        if (!autoLogin_checked.equals("true")) {
            clearInfo();
            return;
        }

        autoLogin_time = SPUtills.get(getApplicationContext(), "autoLogin_time", "").toString();

        try {
            BigDecimal bd_auto = new BigDecimal(autoLogin_time);
            BigDecimal bd_now = new BigDecimal(mCalendar.getTimeInMillis());
            BigDecimal bd_duration = BigDecimal.valueOf(604800000L);//7天的毫秒值，超过7天没登录就清除记录
            if (bd_now.subtract(bd_auto).compareTo(bd_duration) != -1) {
                clearInfo();
                return;
            }
        } catch (Exception e) {
            clearInfo();
            Log.e("sp_time_parse_error", "本地保存日期转化错误");
            return;
        }

        string_user = SPUtills.get(getApplicationContext(), Constants.GET_USERNAME, "").toString();
        string_password = decrypt(SPUtills.get(getApplicationContext(), "autoLogin_password", "").toString());

        checkInfo();
    }

    private void setViews() {
        findViewById(R.id.textView_forgetPassword).setOnClickListener(this);

        editText_user = (EditText) findViewById(R.id.editText_user);
        editText_password = (EditText) findViewById(R.id.editText_password);
        mCheckBox = (CheckBox) findViewById(R.id.checkBox_autoLogin);
        textView_login = (TextView) findViewById(R.id.textView_login);
        textView_login.setOnClickListener(this);
        mDialog = new Dialog(LoginActivity.this, R.style.no_title_dialog);
        mDialog.setContentView(R.layout.dialog_sel_subject_state);
        dialogTitle = (TextView) mDialog.findViewById(R.id.dialog_title);
        mDialog.findViewById(R.id.cancel).setOnClickListener(this);
        mDialog.findViewById(R.id.confirm).setOnClickListener(this);

        editText_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    login();
                    return true;
                }

                return false;
            }
        });

        mProgressDialog = new Dialog(this, R.style.LoadingDialog);
        mProgressDialog.setContentView(R.layout.loadingdialog);
        mProgressDialog.setCancelable(false);
    }


    /**
     * <登录方法>-完成
     * 1)判断用户名/手机号和密码输入框是否为空，如果任一输入框为空则方法结束。
     * 2)发送登录信息到服务器进行验证，显示验证结果。如果验证失败，则要求用户重新输入。
     * 3)判断checkbox自动登录是否勾选，如果已勾选，则保存登录信息，下次启动本应用时将自动完成登录。如果未勾选，则尝试删除已保存的登录信息。
     * 4)判断是否需要进入欢迎界面，如果不需要则进入主界面。
     */
    private void login() {
        textView_login.setClickable(false);
        string_user = editText_user.getText().toString().trim();
        if (string_user.equals("")) {
            Toast.makeText(this, "用户名/手机号不能为空", Toast.LENGTH_SHORT).show();
            textView_login.setClickable(true);

            return;
        }
        string_password = editText_password.getText().toString();
        if (string_password.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            textView_login.setClickable(true);
            return;
        }
        checkInfo();
    }


    /**
     * <验证登录信息>
     * 检查网络是否可用，如果不可用则提示
     * 如果网络可用，则将用户名/手机号和登录密码发送到服务器进行验证
     */
    private void checkInfo() {
        if (!checkNetwork()) {
            Toast.makeText(this, "未连接网络", Toast.LENGTH_SHORT).show();
            textView_login.setClickable(true);
            clearInfo();
            return;
        }

        mProgressDialog.show();

        serverUrl = Constants.SERVER;
        SPUtills.put(getApplicationContext(), Constants.GET_SERVER, serverUrl);
        Map<String, String> mMap = new HashMap<String, String>();
        mMap.put("username", string_user);
        mMap.put("password", string_password);
        mVolleyManager.getString(serverUrl + Constants.LOGIN, mMap, "Login", new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(LoginActivity.this, "无法连接到服务器", Toast.LENGTH_SHORT).show();
                textView_login.setClickable(true);
                clearInfo();
                mProgressDialog.dismiss();
            }

            @Override
            public void onSucceed(String response) {
                textView_login.setClickable(true);
                mProgressDialog.dismiss();

                Gson gson = new Gson();
                Login login = gson.fromJson(response, Login.class);
                int code = login.getCode();
                if (code != Constants.STATUS_SUCCESS) {
                    clearInfo();
                }else {
                        VolleyManager.getInstance().setCoolie(login.getSessionId());

                        SPUtills.put(getApplicationContext(), "sessionId", login.getSessionId());
                        // 保存在BaseApplication里,通过app.getLogin()提取
                        BaseApplication app = (BaseApplication) getApplication();
                        app.setLogin(login);
                        if (mCheckBox.isChecked()) {
                            //TODO:保存登录信息到SP

                            SPUtills.put(getApplicationContext(), "autoLogin_time", String.valueOf(mCalendar.getTimeInMillis()));
                            SPUtills.put(getApplicationContext(), "autoLogin_checked", "true");
                            SPUtills.put(getApplicationContext(), "autoLogin_password", encrypt(string_password));
                            SPUtills.put(getApplicationContext(), Constants.GET_USERNAME, string_user);

                        }
                        switchActivity(GO_MAIN);
                }

            }
        });
    }

    /**
     * <检查网络>
     * 如果网络可用，返回true
     */
    private boolean checkNetwork() {
        Context context = getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * <删除本地登录信息>
     * 删除已保存的用户名/手机号，加密的登录密码，最后一次自动登录的日期，自动登录设置为false
     * 注：启动时不显示界面，先检查本地保存密码的记录，如果自动登录不成功，才显示界面
     */
    private void clearInfo() {
        SPUtills.remove(getApplicationContext(), "autoLogin_user");
        SPUtills.remove(getApplicationContext(), "autoLogin_password");
        SPUtills.remove(getApplicationContext(), "autoLogin_time");
        SPUtills.put(getApplicationContext(), "autoLogin_checked", "false");
        findViewById(R.id.relativeLayout_login).setVisibility(View.VISIBLE);

    }

    /**
     * <加密登录密码>-未完成
     * 将登录密码转为密文，用于保存在本地
     */

    private String encrypt(String str) {
        String pwd;
        pwd = str;
        return pwd;
    }

    /**
     * <解密本地保存的密文>-未完成
     * 将密文还原为登录密码，用于发送到服务器进行登录信息验证
     */
    private String decrypt(String str) {
        String pwd;
        pwd = str;
        return pwd;
    }


    /**
     * <跳转界面>-未完成
     * 1)跳转到欢迎界面，条件？首次登录，长时间未登录，客户端更新，特殊日期
     * 2)首次登录，进入绑定手机号界面
     * 3)如果不符合上述条件，则跳转到主界面
     */
    private void switchActivity(int action) {
        recycleImage();
        Intent intent = new Intent();
        switch (action) {
            //仅在转到主界面的时候执行finish()
            case GO_MAIN:
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case GO_REGISTER:
                intent.setClass(this, RegisterActivity.class);
                break;
            case GO_RESET:
                intent.setClass(this, ResetPasswordActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);

    }

    /**
     * <绑定窗口>-未有接口
     * 1)初始化对话框
     * 2)点击“确定”进入绑定页面
     * 3)点击“取消”则关闭对话框，不再执行任何操作
     */
    private void register() {
        dialogTitle.setText("初次登录需绑定手机号");
        mDialog.show();

    }


    /**
     * <点击事件>
     * 忘记密码
     * 登录：校验输入
     * 对话框-取消
     * 对话框-确定
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_forgetPassword:
                switchActivity(GO_RESET);
                break;
            case R.id.textView_login:
                login();
                break;

            case R.id.cancel:
                mDialog.dismiss();
                break;
            case R.id.confirm:
                mDialog.dismiss();
                //                switchActivity();
                break;
            default:
                break;
        }
    }

    /**
     * <返回键>
     * 按下返回键：显示桌面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDesktop();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showDesktop() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }


    /**
     * 施放图片
     */
    public void recycleImage() {
        try {

            Drawable d = imageView_welcome.getDrawable();
            if (null != d && d instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) d).getBitmap();
                bmp.recycle();
                bmp = null;
            }
            if (null != d) {
                d.setCallback(null);
            }


        } catch (Exception e) {
            return;
        }

        imageView_welcome.setImageBitmap(null);

    }

    /**
     * 根据屏幕大小压缩显示欢迎图片
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

}
