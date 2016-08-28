package com.cnst.wisdom.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ViewFlipper;

import com.cnst.wisdom.R;
import com.cnst.wisdom.model.bean.FlipImage;
import com.cnst.wisdom.utills.ImageTools;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片轮播
 * 动态加载图片，每加载一张图片就添加一个按钮
 */
public class MyFlipper
{
    private Context mContext;
    private ViewFlipper mViewFlipper;
    private RadioGroup flipperRadioGroup;


    public MyFlipper(Context context, ViewFlipper viewFlipper, RadioGroup flipperRadioGroup)
    {
        mContext = context;
        mViewFlipper = viewFlipper;
        this.flipperRadioGroup = flipperRadioGroup;


    }

    /**
     * 在一张图片完成下载后，创建新的ImageView和对应的RadioButton
     * 按钮间距50dp
     */
    public void addImage(int order, String filename)
    {

        RadioButton tempButton = new RadioButton(mContext);
        tempButton.setTag(order);
        tempButton.setButtonDrawable(android.R.color.transparent);
        tempButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.selector_radio);
        tempButton.setAlpha(0.618f);

        //设置按钮间距
        tempButton.setWidth((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, mContext.getResources().getDisplayMetrics()));

        tempButton.setPadding(0, 0, 0, 0);
        tempButton.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.MATCH_PARENT);
        tempButton.setLayoutParams(lp);
        flipperRadioGroup.addView(tempButton, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Bitmap bmp=BitmapFactory.decodeFile(filename);
        ImageView tempImage = new ImageView(mContext);
        tempImage.setImageBitmap(bmp);
        tempImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mViewFlipper.addView(tempImage, order);

        mViewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                flipperRadioGroup.check(flipperRadioGroup.getChildAt(mViewFlipper.getDisplayedChild()).getId());
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        tempButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int order = Integer.parseInt(v.getTag().toString());
                int current = mViewFlipper.getDisplayedChild();
                Log.i("tagorder", "order:"+order+"---current:"+current);
                if(order == current)
                {
                    return;
                }else if(order>current)
                {
                    for(int i = 0; i<order-current; i++)
                    {
                        mViewFlipper.showNext();
                    }

                }else
                {
                    for(int i = 0; i<current-order; i++)
                    {
                        mViewFlipper.showPrevious();
                    }
                }
            }

        });
    }


    /**
     * 异步任务下载图片
     * 如果图片存在则跳过下载，直接添加到轮播
     */
    public class FlipImageTask extends AsyncTask<FlipImage,Void,String>
    {

        int order = 0;
        String location;

        @Override
        protected String doInBackground(FlipImage... flipImages)
        {
            order = flipImages[0].getOrder();
            Bitmap bmp = null;
            String url = flipImages[0].getUrl();
            location = Environment.getExternalStorageDirectory().getAbsolutePath();
            String filename = url.substring(url.lastIndexOf("/"));
            filename = filename.substring(0, filename.lastIndexOf("."));
            try
            {
                File file = new File(location+"/"+filename+".png");
                Log.i("tagfile", file.toString());
                if(file.exists())
                {
                    Log.i("tagfile", file.toString()+"====== exists");
                    return filename;
                }

            }catch(Exception e)
            {
                e.printStackTrace();
            }
            if(url != null && !url.equals(""))
            {
                try
                {
                    if(URLUtil.isHttpUrl(url))
                    {
                        URL mUrl = new URL(url);
                        HttpURLConnection conn = (HttpURLConnection)mUrl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream stream = conn.getInputStream();
                        bmp = BitmapFactory.decodeStream(stream);
                        stream.close();
                    }else
                    {
                        bmp = BitmapFactory.decodeFile(url);
                    }
                }catch(Exception e)
                {
                    return null;
                }
                ImageTools.savePhotoToSDCard(bmp, Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
            }
            return filename;
        }

        protected void onPostExecute(String filename)
        {
            if(filename != null)
            {

                addImage(order, location+"/"+filename+".png");
            }
        }
    }

    public void downloadImage(FlipImage flipImage)
    {
        new FlipImageTask().execute(flipImage);

    }
}
