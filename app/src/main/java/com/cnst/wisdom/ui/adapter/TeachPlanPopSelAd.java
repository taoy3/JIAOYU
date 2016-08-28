package com.cnst.wisdom.ui.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.model.bean.Clazz;
import com.cnst.wisdom.model.bean.Course;
import com.cnst.wisdom.model.bean.Subject;
import com.cnst.wisdom.model.bean.Term;

import java.util.List;


/**
 * 教学筛选时弹出框Adapter
 * 筛选班级或学科或期数 选中的背景设置为OnClick
 *
 * @author jiangzuyun.
 * @see [相关类/方法]
 * @since [产品/模版版本]
 */
public class TeachPlanPopSelAd extends TeachPlanBaseAdapter {
    private int curPosition;

    public TeachPlanPopSelAd(Context context, List list) {
        super(context, list);
    }


    public int getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(int curPosition) {
        notifyDataSetChanged();
        this.curPosition = curPosition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.pop_list_item, parent, false);
        TextView stateBt = (TextView) convertView.findViewById(R.id.text);
        String text = "";
        if (position == 0) {
            text = mContext.getString(R.string.all);
        } else {
            Object object = mList.get(position);
            if (object instanceof Clazz) {
                text = ((Clazz) object).getName();
            } else if (object instanceof Subject) {
                text = ((Subject) object).getName();
            } else if (object instanceof Term) {
                text = ((Term) object).getName();
            } else if (object instanceof Course) {
                text = ((Course) object).getName();
            }
        }
        stateBt.setText(text);
        if (position == curPosition) {
            convertView.setBackgroundResource(R.color.onClick);
        }
        return convertView;
    }
}
