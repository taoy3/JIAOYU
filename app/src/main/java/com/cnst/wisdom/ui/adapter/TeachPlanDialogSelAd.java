package com.cnst.wisdom.ui.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.model.bean.Clazz;
import com.cnst.wisdom.model.bean.Subject;
import com.cnst.wisdom.model.bean.Term;

import java.util.List;


/**
 * 教学科目创建时弹出对话框界面Adapter
 * 选择班级或学科或期数
 * @author jiangzuyun.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class TeachPlanDialogSelAd extends TeachPlanBaseAdapter
{
    private int curPosition = -1;
    public boolean isConfirm;
    public TeachPlanDialogSelAd(Context context, List list,ListView listView) {
        super(context, list);
    }


    public int getCurPosition()
    {
        return isConfirm?curPosition:-1;
    }

    public void setCurPosition(int curPosition)
    {
        isConfirm = false;
        this.curPosition = curPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = mLayoutInflater.inflate(R.layout.item_subject_name,parent,false);
        TextView stateBt = (TextView)convertView;
        String text = "";
        Object object = mList.get(position);
        if(object instanceof Clazz){
            text = ((Clazz)object).getName();
        }else if(object instanceof Subject){
            text = ((Subject)object).getName();
        }else if(object instanceof Term){
            text = ((Term)object).getName();
        }
        if(position==curPosition){
            stateBt.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.radiobutton_on_background, 0);
            if(position==0){
                convertView.setBackgroundResource(R.drawable.bg_dialog_onclick);
            }else {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.onClick));
            }
        }
        stateBt.setText(text);
        return convertView;
    }
}
