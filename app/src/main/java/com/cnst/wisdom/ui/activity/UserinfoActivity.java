package com.cnst.wisdom.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.Login;
import com.cnst.wisdom.model.bean.UpdateInfo;
import com.cnst.wisdom.model.bean.Userinfo;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.view.CheckboxDialog;
import com.cnst.wisdom.ui.view.EditListDialog;
import com.cnst.wisdom.ui.view.GenderDialog;
import com.cnst.wisdom.utills.SPUtills;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <个人信息页>
 * 获取个人信息，如果因网络原因获取不到数据则回到个人中心页
 * 获取个人信息后显示数据，可以修改信息并提交
 */
public class UserinfoActivity extends BaseNetActivity implements View.OnClickListener, DialogInterface.OnDismissListener
{

    private TextView textView_head_text;
    private ImageButton head_search_action;

    private EditText editText_name;
    private TextView textView_gender;
    private TextView textView_class;
    private TextView textView_post;
    private EditText editText_phone;
    private EditText editText_qq;
    private EditText editText_email;
    private EditText editText_hobby;

    private Userinfo mUserinfo;
    //记录性别
    private int info_gender = -1;
    //当前的岗位
    private String info_station = "";
    private String info_stationId = "";

    //储存班级的选项列表
    private List<Userinfo.DataEntity.ClassListEntity> classList;
    //当前的班级，多选
    private List<Userinfo.DataEntity.ClassListEntity> classChecked;
    //储存岗位的选项列表
    private List<String> postList;

    private VolleyManager mVolleyManager = VolleyManager.getInstance();
    BaseApplication app = (BaseApplication)getApplication();
    Login mlogin = app.getLogin();
    private CheckboxDialog mClassDialog;
    private EditListDialog mPostDialog;
    private GenderDialog mGenderDialog;

    private Dialog mProgressDialog;


    private final static String NODATA = "未填写";

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_userinfo);
        setViews();
        getUserinfo();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    private void setViews()
    {
        findViewById(R.id.head_back_action).setOnClickListener(this);
        textView_head_text = (TextView)findViewById(R.id.head_text);
        textView_head_text.setText("个人信息");

        //搜索按钮外观替换为√
        head_search_action.setBackgroundResource(R.drawable.head_submit_action);
        //设置按钮大小40*40 dp
        ViewGroup.LayoutParams lp = head_search_action.getLayoutParams();
        lp.width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        lp.height = lp.width;
        head_search_action.setLayoutParams(lp);


        head_search_action.setOnClickListener(this);

        textView_class = (TextView)findViewById(R.id.textView_class);
        textView_class.setOnClickListener(this);

        textView_gender = (TextView)findViewById(R.id.textView_gender);
        textView_gender.setOnClickListener(this);

        textView_post = (TextView)findViewById(R.id.textView_post);
        textView_post.setOnClickListener(this);


        editText_name = (EditText)findViewById(R.id.editText_name);
        editText_phone = (EditText)findViewById(R.id.editText_phone);
        editText_qq = (EditText)findViewById(R.id.editText_qq);
        editText_email = (EditText)findViewById(R.id.editText_email);
        editText_hobby = (EditText)findViewById(R.id.editText_hobby);

        mProgressDialog=new Dialog(this,R.style.HollowDialog);
        mProgressDialog.setContentView(R.layout.loadingdialog);
        mProgressDialog.setCancelable(false);

    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
//                submit();
            case R.id.head_back_action:
                backToAccount();
                break;
            case R.id.textView_class:
                getClassList();
                break;
            case R.id.textView_gender:
                getGenderDialog();
                break;
            case R.id.textView_post:
                getPostList();
                break;

            default:
                break;
        }
    }

    /**
     * <提交数据>
     * 1)检查必填项，如有缺少填写的信息则提示
     * 2)提交数据，如果成功则回到个人中心，否则提示提交失败原因
     */
    public void submit()
    {
        if(!checkInfo())
        {
            return;
        }
        mProgressDialog.show();

        head_search_action.setClickable(false);
        Map<String,String> mMap = new HashMap<String,String>();
        mMap.put("id", mlogin.getData().getUserId());
        mMap.put("name", editText_name.getText().toString().trim());
        mMap.put("gender", ""+info_gender);
        mMap.put("classIds", getClassIds());
        mMap.put("stationCode", info_stationId);
        mMap.put("stationName", info_station);
        mMap.put("mobileNum", editText_phone.getText().toString().trim());
        mMap.put("qqNum", editText_qq.getText().toString().trim());
        mMap.put("email", editText_email.getText().toString().trim());
        mMap.put("Hobby", editText_hobby.getText().toString().trim());
        mVolleyManager
                .postString(SPUtills.get(getApplicationContext(), Constants.GET_SERVER, Constants.SERVER).toString()+Constants.UPDATE_INFO,
                        mMap, "queryTeacherInfo_app.kq", new NetResult<String>()
                        {
                            @Override
                            public void onFailure(VolleyError error)
                            {
                                mProgressDialog.dismiss();

                                Toast.makeText(UserinfoActivity.this, "提交失败，请检查网络", Toast.LENGTH_SHORT).show();
                                head_search_action.setClickable(true);
                            }

                            @Override
                            public void onSucceed(String response)
                            {
                                mProgressDialog.dismiss();

                                head_search_action.setClickable(true);
                                Gson gson = new Gson();
                                int code = Constants.STATUS_FAIL;
                                UpdateInfo updateInfo = gson.fromJson(response, UpdateInfo.class);

                                try
                                {
                                    code = updateInfo.getCode();
                                }catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                                Toast.makeText(UserinfoActivity.this, updateInfo.getMsg(), Toast.LENGTH_SHORT).show();

                                switch(code)
                                {
                                    case Constants.STATUS_SUCCESS:
                                        backToAccount();
                                        break;
                                    case Constants.STATUS_ARGUMENT_ERROR:
                                    case Constants.STATUS_DATA_NOTFOUND:
                                    case Constants.STATUS_FAIL:
                                    case Constants.STATUS_ILLEGAL:
                                    case Constants.STATUS_SERVER_EXCEPTION:
                                    case Constants.STATUS_TIMEOUT:
                                        break;
                                }
                            }
                        });
    }

    /**
     * <获取班级>
     * 点击任教班级的内容时弹出多选对话框
     */
    private void getClassList()
    {
        mClassDialog = new CheckboxDialog(this, classList, classChecked);
        mClassDialog.setOnDismissListener(this);
        mClassDialog.show();
    }

    /**
     * <获取岗位>
     * 点击岗位时显示对话框
     * 根据已有内容来选定选项，如果没有对应的选项则填充在输入框内
     */
    private void getPostList()
    {
        mPostDialog = new EditListDialog(this, postList, info_station);
        mPostDialog.setOnDismissListener(this);
        mPostDialog.show();
    }

    /**
     * 对话框关闭时回调
     * 岗位选择对话框：返回选择或手动输入的结果
     * 性别选择对话框：返回单选结果
     * 班级选择对话框：返回复选结果
     */
    @Override
    public void onDismiss(DialogInterface dialog)
    {
        if(dialog instanceof EditListDialog)
        {
            EditListDialog listDialog = (EditListDialog)dialog;
            if(listDialog.getSelIndex()<0)
            {
                if(!listDialog.getInput().equals(""))
                {
                    textView_class.setText(listDialog.getInput());
                }
                return;
            }
        }
        if(dialog == mClassDialog)
        {
            classChecked = mClassDialog.getResult();
            setClassChecked();
        }else if(dialog == mPostDialog)
        {
            info_station = mPostDialog.getResult();
            textView_post.setText(info_station);
        }else if(dialog == mGenderDialog)
        {

            info_gender = mGenderDialog.getGender();
            switch(info_gender)
            {
                case 0:
                    textView_gender.setText("男");
                    break;
                case 1:
                    textView_gender.setText("女");
                    break;
                default:
                    textView_gender.setText("未设置");
                    break;
            }
        }
    }

    /**
     * 调用性别选择对话框，根据已选择的数据来显示对话框中所勾选的项
     */
    public void getGenderDialog()
    {
        mGenderDialog = new GenderDialog(this, info_gender);
        mGenderDialog.setOnDismissListener(this);
        mGenderDialog.show();
    }

    /**
     * <获取个人信息>
     * 从服务器获取个人信息，在完成后更新界面，如果获取失败则返回个人中心
     * 请求成功后无论修改成功与否都显示提示
     */
    private void getUserinfo()
    {
        mProgressDialog.show();

        Map<String,String> mMap = new HashMap<String,String>();
        mMap.put("teacherId", mlogin.getData().getUserId());
        mVolleyManager
                .getString(SPUtills.get(getApplicationContext(), Constants.GET_SERVER, Constants.SERVER).toString()+Constants.QUERY_INFO,
                        mMap, "queryTeacherInfo_app.kq", new NetResult<String>()
                        {
                            @Override
                            public void onFailure(VolleyError error)
                            {
                                mProgressDialog.dismiss();

                                Toast.makeText(UserinfoActivity.this, "无法获取个人信息，请检查网络", Toast.LENGTH_SHORT).show();
                                backToAccount();
                            }

                            @Override
                            public void onSucceed(String response)
                            {
                                mProgressDialog.dismiss();

                                Gson gson = new Gson();
                                int code = Constants.STATUS_FAIL;
                                mUserinfo = gson.fromJson(response, Userinfo.class);

                                try
                                {
                                    code = Integer.parseInt(mUserinfo.getCode());
                                }catch(Exception e)
                                {
                                    e.printStackTrace();
                                }

                                switch(code)
                                {
                                    case Constants.STATUS_SUCCESS:
                                        setInfo();
                                        break;
                                    case Constants.STATUS_ARGUMENT_ERROR:
                                    case Constants.STATUS_DATA_NOTFOUND:
                                    case Constants.STATUS_FAIL:
                                    case Constants.STATUS_ILLEGAL:
                                    case Constants.STATUS_SERVER_EXCEPTION:
                                    case Constants.STATUS_TIMEOUT:
                                        backToAccount();
                                        break;
                                }
                            }
                        });

    }


    /**
     * 在获取个人信息的数据后，将数据显示出来
     */
    private void setInfo()
    {
        //显示姓名
        editText_name.setText(mUserinfo.getData().getTeachInfo().getName());
        //显示性别 0=男/1=女/-1=未设置
        info_gender = mUserinfo.getData().getTeachInfo().getGender();
        String gender = NODATA;
        switch(info_gender)
        {
            case 0:
                gender = "男";
                break;
            case 1:
                gender = "女";
                break;
        }
        textView_gender.setText(gender);


        //显示任教班级
        classList = mUserinfo.getData().getClassList();
        String tempClasses[] = mUserinfo.getData().getTeachInfo().getClassIds().split(",");
        classChecked = new ArrayList<>();

        List<String> classIdList = new ArrayList<>();
        for(int i = 0; i<classList.size(); i++)
        {
            classIdList.add(classList.get(i).getId());
        }

        for(int i = 0; i<tempClasses.length; i++)
        {
            if(classIdList.contains(tempClasses[i]))
            {
                classChecked.add(classList.get(i));
            }
        }
        setClassChecked();

        //显示岗位
        List<Userinfo.DataEntity.StationListEntity> templist = mUserinfo.getData().getStationList();
        postList = new ArrayList<String>();
        for(int i = 0; i<templist.size(); i++)
        {
            postList.add(templist.get(i).getValue());
        }
        info_station = mUserinfo.getData().getTeachInfo().getStationName();
        if(info_station.equals("") || info_station == null || info_station.equals("null"))
        {
            info_station = NODATA;
        }
        textView_post.setText(info_station);


        editText_phone.setText(mUserinfo.getData().getTeachInfo().getMobileNum());
        editText_qq.setText(mUserinfo.getData().getTeachInfo().getQqNum());
        editText_email.setText(mUserinfo.getData().getTeachInfo().getEmail());
        editText_hobby.setText(mUserinfo.getData().getTeachInfo().getHobby());
    }


    /**
     * 根据classChecked的内容，显示已选择的班级，在获取数据/复选框对话框修改后调用
     */
    public void setClassChecked()
    {
        String classText = "";
        for(int i = 0; i<classChecked.size(); i++)
        {
            String name = classChecked.get(i).getName();
            if(classText.equals(""))
            {
                classText = classText+name;
            }else
            {
                classText = classText+"，"+name;
            }

        }
        if(classText.equals("") || classText == null || classText.equals("null"))

        {
            classText = NODATA;
        }

        textView_class.setText(classText);

    }

    /**
     * 在提交数据的时候将已选择的班级的ID以 , 拼接
     */
    private String getClassIds()
    {
        String ids = "";

        for(int i = 0; i<classChecked.size(); i++)
        {
            String id = classChecked.get(i).getId();

            if(ids.equals(""))
            {
                ids = ids+id;
            }else
            {
                ids = ids+","+id;
            }
        }
        return ids;
    }

    /**
     * <返回个人中心>
     * 直接finish()无法刷新头像
     */
    private void backToAccount()
    {
        Intent intent = new Intent(UserinfoActivity.this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * <返回键>
     * 按下返回键：返回个人中心页并刷新数据
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            backToAccount();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 在提交数据之前检查必填项是否已填写
     * 符合条件返回true（发送提交请求），不符合条件返回false并提示原因
     */
    public boolean checkInfo()
    {
        if(editText_name.getText().toString().trim().equals(""))
        {
            Toast.makeText(UserinfoActivity.this, "请填写姓名", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!( info_gender == 1 || info_gender == 0 ))
        {
            Toast.makeText(UserinfoActivity.this, "请选择性别", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(classChecked.size() == 0)
        {
            Toast.makeText(UserinfoActivity.this, "请选择任教班级", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(info_station.equals(""))
        {
            Toast.makeText(UserinfoActivity.this, "请填写岗位", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(editText_phone.getText().toString().trim().equals(""))
        {
            Toast.makeText(UserinfoActivity.this, "请填写手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
