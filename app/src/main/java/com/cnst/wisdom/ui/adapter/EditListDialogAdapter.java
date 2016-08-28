package com.cnst.wisdom.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cnst.wisdom.R;

import java.util.List;


/**
 *
 */
public class EditListDialogAdapter extends BaseAdapter
{
    private List<String> list;
    private Context mContext = null;
    private String name;
    private int index = -1;


    public EditListDialogAdapter(Context context, List list, String name)
    {
        mContext = context;
        this.list = list;
        this.name = name;
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
        String item = null;
        if(null != list)
        {
            item = list.get(position);
        }

        return item;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        if(null == convertView)
        {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_post_radio, null);

            viewHolder.mTextView = (TextView)convertView.findViewById(R.id.textView_post);
            viewHolder.mRadioButton = (RadioButton)convertView.findViewById(R.id.radio_post);

            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }


        String item = (String)getItem(position);
        if(null != item)
        {
            viewHolder.mTextView.setText(item);
        }

        viewHolder.mRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    name = (String)getItem(position);
                    notifyDataSetChanged();
                }
            }
        });

        if(item.equals(name))
        {
            viewHolder.mRadioButton.setChecked(true);
            index = position;
        }else
        {
            viewHolder.mRadioButton.setChecked(false);
        }
        viewHolder.mRadioButton.setButtonDrawable(R.drawable.selector_radio_default);


        return convertView;
    }

    private static class ViewHolder
    {
        TextView mTextView;
        RadioButton mRadioButton;
    }

    public void setIndex(String name)
    {
        index = -1;
        this.name = name;
    }

}
