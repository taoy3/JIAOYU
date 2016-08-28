package com.cnst.wisdom.ui.activity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.FeedbackBean;
import com.cnst.wisdom.model.bean.Login;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.utills.SPUtills;
import com.google.gson.Gson;

import java.util.HashMap;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * <需求反馈>
 * <需求反馈页面展示，>
 *
 * @author hanshuai
 * @see [相关类/方法]
 * @since [产品/模版版本]
 *
 */
public class Feedback extends BaseNetActivity implements View.OnClickListener
{

    private ImageButton mIbBack;
    private EditText mEditText;
    private CheckBox mCheckBox;
    private Button mButton;
    private ListView mFeedback;

    private String teacherId = null;
    private String content = null;
    private int check = 0;
    private String feedId = null;

    private TextView tab;

    private VolleyManager volleyManager = VolleyManager.getInstance();
    private Button mBtnCommit;

    private FeedbackAdapter mFeedbackAdapter;
    private FeedbackBean.DataEntity mData;
    private FeedbackBean feedbackBean;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_feedback);
        mIbBack = (ImageButton)findViewById(R.id.ib_back);
        mIbBack.setOnClickListener(this);

        mButton = (Button)findViewById(R.id.btn_commit);
        mButton.setOnClickListener(this);

        mEditText = (EditText)findViewById(R.id.et_feedback);
        mCheckBox = (CheckBox)findViewById(R.id.cb_anonymity);
        mBtnCommit = (Button)findViewById(R.id.btn_commit);
        mBtnCommit.setOnClickListener(this);

        mFeedback = (ListView)findViewById(R.id.lv_feedback);
        mFeedbackAdapter = new FeedbackAdapter();

        getDataFromServer();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    /**
     * 填充反馈记录
     */
    private void getDataFromServer()
    {
        BaseApplication app= (BaseApplication)getApplication();
        Login mlogin=app.getLogin();
        teacherId = mlogin.getData().getUserId();


        HashMap map = new HashMap();
        map.put("teacherId", teacherId);
        VolleyManager.getInstance()
                .postString(SPUtills.get(this, Constants.GET_SERVER, "").toString()+Constants.FEEDBACK_LIST, map, "Comment",
                        new NetResult<String>()
                        {
                            @Override
                            public void onFailure(VolleyError error)
                            {
                                Log.e("feedback", "获取反馈记录失败");

                            }

                            @Override
                            public void onSucceed(String response)
                            {

                                Log.e("feedback------------", response);
                                Gson gson = new Gson();
                                feedbackBean = gson.fromJson(response, FeedbackBean.class);
                                if(feedbackBean != null)
                                {
                                    switch(feedbackBean.getCode())
                                    {
                                        case Constants.STATUS_ARGUMENT_ERROR_STRING:
                                            break;
                                        case Constants.STATUS_DATA_NOTFOUND_STRING:
                                            break;
                                        case Constants.STATUS_ILLEGAL_STRING:
                                            break;
                                        case Constants.STATUS_SERVER_EXCEPTION_STRING:
                                            break;
                                        case Constants.STATUS_SUCCESS_STRING:
//                                            mData = feedbackBean.getData();
                                            if(feedbackBean.getData() != null)
                                            {
                                                initDataFromService();
                                            }

                                    }

                                }
                            }
                        });
    }

    private void initDataFromService()
    {
        mFeedbackAdapter.notifyDataSetChanged();
        mFeedback.setAdapter(mFeedbackAdapter);

    }

    private class FeedbackAdapter extends BaseAdapter {

        private FeedbackBean.DataEntity.NeedListEntity mFeedbackContent;


        @Override
        public int getCount()
        {
            return feedbackBean.getData().getNeedList().size();
        }

        @Override
        public Object getItem(int position)
        {
            return feedbackBean.getData().getNeedList().get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder = null;
            if(convertView == null)
            {
                convertView = View.inflate(Feedback.this, R.layout.item_feedback, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView)convertView.findViewById(R.id.iv_item);
                holder.tv_username = (TextView)convertView.findViewById(R.id.username);
                holder.tv_content = (TextView)convertView.findViewById(R.id.content);
                holder.tv_time = (TextView)convertView.findViewById(R.id.time);
                holder.tv_isAnonymous = (TextView)convertView.findViewById(R.id.isAnonymous);
                holder.tv_delete = (TextView)convertView.findViewById(R.id.delete);
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            mFeedbackContent = feedbackBean.getData().getNeedList().get(position);

            holder.tv_username.setText(feedbackBean.getData().getTeachName());
            holder.tv_content.setText(mFeedbackContent.getContent());
            holder.tv_time.setText(mFeedbackContent.getCreateTime());
            if(mFeedbackContent.getIsAnonymous() == 0) {
                holder.tv_isAnonymous.setVisibility(View.INVISIBLE);
            } else if(mFeedbackContent.getIsAnonymous() == 1) {
                holder.tv_isAnonymous.setVisibility(View.VISIBLE);
            }



            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    feedId = feedbackBean.getData().getNeedList().get(position).getId();
                    Log.d("feedback------------" ,feedId);
                    feedbackBean.getData().getNeedList().remove(position);
                    notifyDataSetChanged();
                    deleteFeedback();
                }
            });
            //获取网络头像URL
            String imageUrl = SPUtills.get(getApplicationContext(), Constants.GET_SERVER, Constants.SERVER).toString()+
                    feedbackBean.getData().getHeadImg().trim();
            Glide.with(Feedback.this).load(imageUrl).placeholder(R.drawable.applogo).error(R
                    .drawable
                    .applogo)
                    .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(Feedback.this)).into(holder.iv_icon);
//            holder.iv_icon.setImageResource(R.drawable.ic_temp);


            return convertView;
        }
    }

    private class ViewHolder
    {
        public ImageView iv_icon;
        public TextView tv_username;
        public TextView tv_content;
        public TextView tv_time;
        public TextView tv_isAnonymous;
        public TextView tv_delete;


    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_commit:

                feedBack();
                break;
        }
    }


    /**
     * 删除需求反馈
     */

    private void deleteFeedback() {
        HashMap deletePara = new HashMap();
        deletePara.put("needId", feedId);
        volleyManager.getString(SPUtills.get(getApplicationContext(), Constants.GET_SERVER, "").toString()+Constants.DELETE_DEMANDNEED,
                deletePara, "delete", new NetResult() {
                    @Override
                    public void onFailure(VolleyError error)
                    {

                    }

                    @Override
                    public void onSucceed(Object response)
                    {

//                        getDataFromServer();
                        feedId = null;
                        Toast.makeText(Feedback.this, "需求反馈删除成功", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * 提交反馈需求
     */
    private void feedBack(){

        BaseApplication app= (BaseApplication)getApplication();
        Login mlogin=app.getLogin();
        teacherId = mlogin.getData().getUserId();
        content = mEditText.getText().toString().trim();
        check=mCheckBox.isChecked()?1:0;

        if(content.isEmpty()){
            Toast.makeText(getApplicationContext(),"请输入反馈内容",Toast.LENGTH_SHORT).show();
        }else {
            HashMap feedBackPara = new HashMap();
            feedBackPara.put("teacherId", teacherId);
            feedBackPara.put("content", content);
            feedBackPara.put("isAnonymous", check+"");
            volleyManager.postString(SPUtills.get(getApplicationContext(), Constants.GET_SERVER, "").toString()+Constants.FEED_BACK,
                    feedBackPara, "feedback", new NetResult<String>()
                    {
                        @Override
                        public void onFailure(VolleyError error)
                        {

                        }

                        @Override
                        public void onSucceed(String response)
                        {
                            getDataFromServer();
                            Toast.makeText(Feedback.this, "需求反馈成功", Toast.LENGTH_SHORT).show();
                            mEditText.setText("");
                        }
                    });


        }




    }
}
