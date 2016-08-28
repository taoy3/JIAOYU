package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.face.ChatEmoji;
import com.cnst.wisdom.face.FaceAdapter;
import com.cnst.wisdom.face.FaceConversionUtil;
import com.cnst.wisdom.face.ViewPagerAdapter;
import com.cnst.wisdom.logic.ImgsActivity;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 提交页面
 *
 * @author tangbinfeng
 * @time 16:48
 * @see
 */
public class PublishDynamicFmActivity extends BaseNetActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{
    private static final int TAKE_PICTURE = 0;
    private RelativeLayout mHead_back_action;
    protected EditText mEt_content;//输入框
    private FrameLayout mFl_submit;//提交按钮
    private Button mBtn_image;//选择图片按钮
    private Button mBtn_camera;//拍照
    private BitmapUtils mBitmapUtils;
    ArrayList<String> listfile = new ArrayList<String>();
    private GridView mGv_image;
    private ImageDynamicFmAdapter mAdapter;
    private Button mBtn_face;//表情按钮
    private RelativeLayout mLl_facechoose;
    private ViewPager mVp_face;
    /** 表情页界面集合 */
    private ArrayList<View> pageViews;
    /** 表情数据填充器 */
    private List<FaceAdapter> faceAdapters;
    /** 表情集合 */
    private List<List<ChatEmoji>> emojis;
    /** 当前表情页 */
    private int current = 0;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_publish_dynamic_fm);
    }

    protected void initView()
    {
        mHead_back_action = (RelativeLayout)findViewById(R.id.head_back_action);
        mGv_image = (GridView)findViewById(R.id.gv_image);
        mLl_facechoose = (RelativeLayout)findViewById(R.id.ll_facechoose);
        mEt_content = (EditText)findViewById(R.id.et_content);
        mBtn_image = (Button)findViewById(R.id.btn_image);
        mBtn_face = (Button)findViewById(R.id.btn_face);
        mBtn_camera = (Button)findViewById(R.id.btn_camera);
        mVp_face = (ViewPager)findViewById(R.id.vp_contains);
    }

    protected void initData()
    {
        if(mBitmapUtils == null)
        {
            mBitmapUtils = new BitmapUtils(this);
        }
        if(listfile != null)
        {
            mAdapter = new ImageDynamicFmAdapter();
            mGv_image.setAdapter(mAdapter);
        }
        //        初始化表情数据
        emojis = FaceConversionUtil.getInstace().emojiLists;
        Init_viewPager();

        mVp_face.setAdapter(new ViewPagerAdapter(pageViews));
        initListener();
    }

    private void initListener()
    {
        mHead_back_action.setOnClickListener(this);
        mFl_submit.setOnClickListener(this);
        mBtn_image.setOnClickListener(this);
        mBtn_camera.setOnClickListener(this);
        mBtn_face.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.head_back_action:
                finish();
                break;
            case R.id.btn_camera:
                photograph();
                break;
            case R.id.btn_image:
                Intent intent = new Intent();
                intent.setClass(this, ImgsActivity.class);
                startActivityForResult(intent, 0);
            case R.id.btn_face:
                // 隐藏表情选择框
                if(mLl_facechoose.getVisibility() == View.VISIBLE)
                {
                    mLl_facechoose.setVisibility(View.GONE);
                }else
                {
                    mLl_facechoose.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void photograph()
    {
        Uri imageUri = null;
        String fileName = null;
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileName = "imageCamera.jpg";
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), fileName));
        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 初始化显示表情的viewpager
     */
    private void Init_viewPager()
    {
        pageViews = new ArrayList();
        // 左侧添加空页
        View nullView1 = new View(this);
        // 设置透明背景
        nullView1.setBackgroundColor(Color.TRANSPARENT);
        pageViews.add(nullView1);

        // 中间添加表情页

        faceAdapters = new ArrayList<FaceAdapter>();
        for(int i = 0; i<emojis.size(); i++)
        {
            GridView view = new GridView(this);
            FaceAdapter adapter = new FaceAdapter(this, emojis.get(i));
            view.setAdapter(adapter);
            faceAdapters.add(adapter);
            view.setOnItemClickListener(this);
            view.setNumColumns(7);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setPadding(5, 0, 5, 0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            pageViews.add(view);
        }

        // 右侧添加空页面
        View nullView2 = new View(this);
        // 设置透明背景
        nullView2.setBackgroundColor(Color.TRANSPARENT);
        pageViews.add(nullView2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            switch(requestCode)
            {
                case TAKE_PICTURE:
                    String path = Environment.getExternalStorageDirectory()+"/imageCamera.jpg";
                    listfile.add(listfile.size(), path);

                    break;
            }
        }
        if(data != null)
        {
            Bundle bundle = data.getExtras();
            if(bundle != null)
            {
                if(bundle.getStringArrayList("files") != null)
                {
                    listfile = bundle.getStringArrayList("files");
                    if(listfile.size() != 0)
                    {
                        listfile.add("");
                    }
                }
            }

        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        ChatEmoji emoji = (ChatEmoji)faceAdapters.get(current).getItem(position);
        if(emoji.getId() == R.drawable.face_del_icon)
        {
            //            int selection = et_sendmessage.getSelectionStart();
            //            String text = et_sendmessage.getText().toString();
            //            if(selection>0)
            //            {
            //                String text2 = text.substring(selection-1);
            //                if("]".equals(text2))
            //                {
            //                    int start = text.lastIndexOf("[");
            //                    int end = selection;
            //                    et_sendmessage.getText().delete(start, end);
            //                    return;
            //                }
            //                et_sendmessage.getText().delete(selection-1, selection);
            //            }
        }
        //        if(!TextUtils.isEmpty(emoji.getCharacter()))
        //        {
        //            if(mListener != null)
        //            {
        //                mListener.onCorpusSelected(emoji);
        //            }
        //            SpannableString spannableString = FaceConversionUtil.getInstace()
        //                    .addFace(getContext(), emoji.getId(), emoji.getCharacter());
        //            et_sendmessage.append(spannableString);
        //        }
    }

    class ImageDynamicFmAdapter extends BaseAdapter
    {

        private BitmapUtils mBitmapUtils;

        public ImageDynamicFmAdapter()
        {
            if(mBitmapUtils == null)
            {
                mBitmapUtils = new BitmapUtils(PublishDynamicFmActivity.this);
            }

        }

        @Override
        public int getCount()
        {

            return listfile.size();

        }

        @Override
        public int getViewTypeCount()
        {
            return 2;
        }

        @Override
        public int getItemViewType(int position)
        {
            if(position<=listfile.size()-1)
            {
                return 1;
            }else
            {
                return 2;
            }

        }

        @Override
        public String getItem(int position)
        {

            return listfile.get(position);

        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final ViewHolder viewHolder;
            getItemViewType(position);
            if(convertView == null)
            {
                viewHolder = new ViewHolder();
                convertView = View.inflate(PublishDynamicFmActivity.this, R.layout.item_gridview_image, null);
                viewHolder.iv_iamge = (ImageView)convertView.findViewById(R.id.iv_iamge);
                viewHolder.btn_del = (Button)convertView.findViewById(R.id.btn_del);
                convertView.setTag(viewHolder);
            }else
            {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            int type = getItemViewType(position);
            if(type == 1)
            {
                String imagePath = listfile.get(position);
                Log.e("imagePathimagePath", imagePath);
                if(!TextUtils.isEmpty(imagePath) || position != listfile.size()-1)
                {
                    mBitmapUtils.display(viewHolder.iv_iamge, imagePath);
                }
                viewHolder.btn_del.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(position != listfile.size()-1)
                        {

                            listfile.remove(position);
                            notifyDataSetChanged();

                        }

                    }
                });
            }else
            {
                viewHolder.iv_iamge.setBackgroundResource(R.drawable.imgbg);
                viewHolder.iv_iamge.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(position == listfile.size()-1)
                        {
                            Intent intent = new Intent();
                            intent.setClass(PublishDynamicFmActivity.this, ImgsActivity.class);
                            startActivityForResult(intent, 0);
                        }
                    }
                });
            }


            return convertView;
        }
    }


    static class ViewHolder
    {
        public ImageView iv_iamge;
        public Button btn_del;
    }

}
