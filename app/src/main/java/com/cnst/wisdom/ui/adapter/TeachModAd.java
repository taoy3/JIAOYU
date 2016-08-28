package com.cnst.wisdom.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.model.bean.TeachPlanBean;
import com.cnst.wisdom.ui.activity.TeachPlanModList;
import com.cnst.wisdom.ui.activity.TeachPlanModify;
import com.cnst.wisdom.ui.widget.calendar.CalendarDialog;

import java.util.List;

/**
 * 修改计划的ListView Item界面
 * 显示班级 期数 课程名 修改按钮
 * 点击修改进入TeachPlanCourse页面修改计划
 * @author jiangzuyun.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class TeachModAd extends TeachPlanBaseAdapter<TeachPlanBean> implements View.OnClickListener
{
    public TeachModAd(Context context, List list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            convertView = mLayoutInflater.inflate(R.layout.item_teach_mod,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder)convertView.getTag();
        holder.clazzTv.setText(mList.get(position).getClassdes());
        holder.subjectTv.setText(mList.get(position).getSubjectname());
        holder.termTv.setText(mList.get(position).getTermname());
        holder.dateTv.setText(mContext.getResources().getString(R.string.teach_start)+"："+
                mList.get(position).getStartdate()+ CalendarDialog.DATESPACE+mList.get(position).getEnddate());
        holder.stateBt.setText(R.string.mof);
        holder.stateBt.setTag(position);
        holder.stateBt.setOnClickListener(this);
//        if(position==0||position==mList.size()-1){
//            convertView.findViewById(R.id.under_line).setVisibility(View.INVISIBLE);
//        }
        return convertView;
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.teach_mod_state:
                int position = (Integer)v.getTag();
                mContext.startActivityForResult(new Intent(mContext, TeachPlanModify.class)
                        .putExtra(TeachPlanModify.class.getName(), mList.get(position)), TeachPlanModList.BACKCODE);
                break;
            default:
                break;
        }
    }

    private static class ViewHolder
    {
        private TextView clazzTv;
        private TextView subjectTv;
        private TextView termTv;
        private TextView dateTv;
        private TextView stateBt;
        public ViewHolder(View convertView)
        {
            clazzTv = (TextView)convertView.findViewById(R.id.teach_mod_clazz);
            subjectTv = (TextView)convertView.findViewById(R.id.teach_mod_subject);
            termTv = (TextView)convertView.findViewById(R.id.teach_mod_term);
            dateTv = (TextView) convertView.findViewById(R.id.teach_mod_date);
            stateBt = (TextView)convertView.findViewById(R.id.teach_mod_state);
        }
    }
}
