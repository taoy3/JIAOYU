package com.cnst.wisdom.ui.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.CourseDetail;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

import java.util.List;

/**
 * Created by Jonas on 2016/1/20.
 * <一句话功能简述>
 * <功能详细描述>
 *
 * @author meilianbing.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class GuidanceAdapter extends TeachPlanBaseAdapter<CourseDetail>{
    private final BitmapDisplayConfig displayConfig;
    private String searchText = null;
    private BitmapUtils bitmapUtils;

    public GuidanceAdapter(Context context, List<CourseDetail> list) {
        super(context, list);
        bitmapUtils = new BitmapUtils(context);
        displayConfig = new BitmapDisplayConfig();
        displayConfig.setLoadFailedDrawable(context.getResources().getDrawable(R.mipmap.video));
    }


    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CourseDetail courseDetial = mList.get(position);
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_guidance, null);
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        StringBuffer buffer = new StringBuffer();
        if(courseDetial.getSubject()!=null){
            buffer.append(courseDetial.getSubject()+"  ");
        }
        if(courseDetial.getTerm()!=null){
            buffer.append(courseDetial.getTerm()+"  ");
        }
        if(courseDetial.getWeek()!=null){
            buffer.append(courseDetial.getWeek()+"  ");
        }
        viewHolder.tvType.setSelected(true);
        if(searchText!=null){
            checkText(buffer.toString().trim(), viewHolder.tvSubject);
            checkText(courseDetial.getClassname()+"\t\t["+courseDetial.getClassification()+"]", viewHolder.tvType);
        }else {
            viewHolder.tvSubject.setText(buffer.toString().trim());
//                viewHolder.tvType.measure(
//                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                int x= viewHolder.tvType.getMeasuredWidth();
//                float width = viewHolder.tvType.getPaint().measureText(str);
//                if(width>x){
//                    viewHolder.tvType.setPadding(0, -2, 0, 0);
//                }else {
//                    viewHolder.tvType.setPadding(0, 5, 0, 0);
//                }
            viewHolder.tvType.setText(courseDetial.getClassname()+"\t\t["+courseDetial.getClassification()+"]\t\t");
        }
        if(Boolean.valueOf(courseDetial.isVideo()) == true)
        {
            viewHolder.iconView.setMaxWidth(50);
            bitmapUtils.display(viewHolder.iconView, courseDetial.getThumbnailPath(),displayConfig);
        }else
        {
            viewHolder.iconView.setMaxWidth(88);
            if(courseDetial.getThumbnailPath()!=null){
                bitmapUtils.display(viewHolder.iconView, Constants.SERVER + courseDetial.getThumbnailPath().substring(1),displayConfig);
            }
        }
        return convertView;
    }

    private void checkText(String text, TextView textView) {
        textView.setText(text);
        if(searchText!=null&&text!=null&&text.contains(searchText)&&text.length()-searchText.length()>=0){
            for (int i = 0; i < text.length()-searchText.length()+1; i++) {
                String sub = text.substring(i,searchText.length()+i);
                if(searchText.equals(sub)){
                    SpannableStringBuilder builder = new SpannableStringBuilder(text);
                    //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                    ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary));
                    builder.setSpan(redSpan, i, searchText.length()+i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(builder);
                    break;
                }
            }
        }
    }
    private static class ViewHolder{
        TextView tvSubject;
        TextView tvType;
        ImageView iconView;

        public ViewHolder(View convertView) {
            this.tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);
            this.tvType = (TextView) convertView.findViewById(R.id.tvType);
            this.iconView = (ImageView) convertView.findViewById(R.id.icon);
        }
    }
}
