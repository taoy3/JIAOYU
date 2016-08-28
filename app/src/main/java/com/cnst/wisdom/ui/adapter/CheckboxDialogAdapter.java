package com.cnst.wisdom.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.model.bean.Userinfo;

import java.util.List;

/**
 * 用于加载复选框对话框的布局
 */
public class CheckboxDialogAdapter extends BaseAdapter
{

    private Context mContext = null;
    private List<Userinfo.DataEntity.ClassListEntity> list = null;
    private List<Userinfo.DataEntity.ClassListEntity> checked = null;


    public CheckboxDialogAdapter(Context context, List<Userinfo.DataEntity.ClassListEntity> list, List<Userinfo.DataEntity.ClassListEntity> checked)
    {
        mContext = context;
        this.list = list;
        this.checked = checked;
    }

    public void setList(List<Userinfo.DataEntity.ClassListEntity> list)
    {
        this.list = list;

    }


    @Override
    public int getCount()
    {
        int count = 0;
        if(null != list)
        {
            count = list.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position)
    {
        Userinfo.DataEntity.ClassListEntity item = null;
        if(null != list)
        {
            item = list.get(position);
        }

        return item;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        if(null == convertView)
        {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_class_checkbox, null);

            viewHolder.mTextView = (TextView)convertView.findViewById(R.id.textView_classname);
            viewHolder.mCheckBox = (CheckBox)convertView.findViewById(R.id.checkbox_classname);

            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }


        Userinfo.DataEntity.ClassListEntity item = (Userinfo.DataEntity.ClassListEntity)getItem(position);
        if(checked.contains(item))
        {
            viewHolder.mCheckBox.setChecked(true);
        }else
        {
            viewHolder.mCheckBox.setChecked(false);
        }

        if(null != item)
        {
            viewHolder.mTextView.setText(item.getName());
        }

        return convertView;
    }

    private static class ViewHolder
    {
        TextView mTextView;
        CheckBox mCheckBox;
    }
}
