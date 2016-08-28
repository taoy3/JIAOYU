package com.cnst.wisdom.ui.materialpager;


import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cnst.wisdom.R;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.OnlinePagerBean;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.activity.LearnDetail;
import com.cnst.wisdom.utills.SPUtills;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author pengjingnag.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class OnlinePager
{
    private String mCode;
    private Context mContext;
    protected ListView mListView;
    public  View mRootView;
    private VolleyManager volleyManager=VolleyManager.getInstance();
    private ArrayList<OnlinePagerBean.OnlinePager>mOnlinepagerDatas;
    private String searchText;

    public void setmOnlinepagerDatas(ArrayList<OnlinePagerBean.OnlinePager> mOnlinepagerDatas) {
        this.mOnlinepagerDatas = mOnlinepagerDatas;
    }

    public OnlinePager(Context context,String code){
        this.mContext=context;
        this.mCode=code;
        mRootView = getRootView();

    }

    public View getRootView()
    {
        View view = View.inflate(mContext, R.layout.lv_online_teach, null);
        mListView = (ListView)view.findViewById(R.id.lv_online_teach);
        return view;
    }

    public void initData()
    {
        HashMap map = new HashMap();
        map.put("dictCode", mCode);
        map.put("pageNum", "1");
        map.put("pageSize", "20");
        volleyManager.getString(SPUtills.get(mContext, Constants.GET_SERVER, "").toString()+Constants
                .QUERY_MATERIAL, map, "Online", new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error)
            {
                Log.e("query_online","访问网络失败");
            }

            @Override
            public void onSucceed(String response)
            {
                Gson gson = new Gson();
                OnlinePagerBean onlinePagerBean=null;
                onlinePagerBean=gson.fromJson(response,OnlinePagerBean.class);
                if(onlinePagerBean!=null){
                    Log.e("Online", onlinePagerBean.toString());
                    int code =onlinePagerBean.getCode();
                    switch(code)
                    {
                        case Constants.STATUS_ARGUMENT_ERROR:
                            break;
                        case Constants.STATUS_DATA_NOTFOUND:
                            break;
                        case Constants.STATUS_ILLEGAL:
                            break;
                        case Constants.STATUS_SERVER_EXCEPTION:
                            break;
                        case Constants.STATUS_SUCCESS:
                            mOnlinepagerDatas = onlinePagerBean.getData();
                            if(mOnlinepagerDatas != null)
                            {
                                Log.e("OnlinePager", response);
                                initServiceData();
                            }
                            break;
                        case Constants.STATUS_TIMEOUT:
                            break;
                    }

                }
            }
        });
    }

    public void initServiceData()
    {
        OnlinePagerInfoAdapter  adapter=new OnlinePagerInfoAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String resourceId = mOnlinepagerDatas.get(position).getResourceId();
                String videoId = mOnlinepagerDatas.get(position).getVideoId();


                if (resourceId != null) {
                    Intent intent = new Intent(mContext, LearnDetail.class);
                    intent.putExtra("resourceId", resourceId);
                    intent.putExtra("videoId", videoId);


                    mContext.startActivity(intent);
                }


            }
        });

    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    private void checkText(String text, TextView textView)
    {
        textView.setText(text);
        if(text != null && text.length()-searchText.length()>=0)
        {
            for(int i = 0; i<text.length()-searchText.length()+1; i++)
            {
                String sub = text.substring(i, searchText.length()+i);
                if(searchText.equals(sub))
                {
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                    ForegroundColorSpan redSpan = new ForegroundColorSpan(
                            mContext.getResources().getColor(R.color.colorPrimary));
                    builder.setSpan(redSpan, i, searchText.length()+i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(builder);
                    break;
                }
            }
        }
    }

    private HttpUtils mHttp;
    private BitmapUtils bitmapUtils;

    private class OnlinePagerInfoAdapter extends BaseAdapter{

        public OnlinePagerInfoAdapter()
        {
            bitmapUtils = new BitmapUtils(mContext);
            mHttp = new HttpUtils();
        }
        @Override
        public int getCount()
        {
            Log.e("online",String.valueOf(OnlinePager.this.mOnlinepagerDatas.size()));
            return OnlinePager.this.mOnlinepagerDatas.size();
        }

        @Override
        public OnlinePagerBean.OnlinePager getItem(int position)
        {
            return mOnlinepagerDatas.get(position);
//            return null;
        }


        @Override
        public long getItemId(int position)
        {
            //            return position;
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder=null;
            if(convertView==null){
                holder = new ViewHolder();
                convertView= View.inflate(mContext, R.layout.item_online, null);
                holder.mImageView = (ImageView)convertView.findViewById(R.id.iv_item);
                holder.mTextView= (TextView)convertView.findViewById(R.id.tv_item);
                holder.ivArrow = (ImageView)convertView.findViewById(R.id.iv_arrow);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder)convertView.getTag();
            }
            final OnlinePagerBean.OnlinePager item = getItem(position);
            if(searchText!=null){
                checkText(mOnlinepagerDatas.get(position).getTitle(),holder.mTextView);
            }else {
                holder.mTextView.setText(mOnlinepagerDatas.get(position).getTitle());
            }
//            Glide.with(mContext).load(mOnlinepagerDatas.get(position).getThumbnailPath()).placeholder(R.drawable.ic_empty_page).error(R.drawable
//                    .ic_empty_page)
//                    .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .bitmapTransform(new CropCircleTransformation(mContext)).into(holder.mImageView);
            bitmapUtils.display(holder.mImageView, item.getThumbnailPath());

            return convertView;
        }
    }
    private class ViewHolder{
        public ImageView mImageView;
        public TextView mTextView;
        public ImageView ivArrow;
    }
}






















