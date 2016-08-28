package com.cnst.wisdom.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cnst.wisdom.R;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.TeachPlanBean;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.utills.BaseDataUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教学计划的教学进度ListView Item界面
 * 显示班级 科目 期数 课程名 状态按钮
 * 默认未完成显示在上面 点击此按钮弹出对话框切换状态
 *
 * @author taoyuan.
 * @since 1.0
 */
public class TeachProgressAd extends TeachPlanBaseAdapter<TeachPlanBean> implements View.OnClickListener {
    private Dialog mDialog;
    private TextView dialogTitle;
    private TextView curClickView;//当前修改的控件

    public TeachProgressAd(Context context, List<TeachPlanBean> list) {
        super(context, list);
        mDialog = new Dialog(context, R.style.no_title_dialog);
        mDialog.setContentView(R.layout.dialog_sel_subject_state);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialogTitle = (TextView) mDialog.findViewById(R.id.dialog_title);
        mDialog.findViewById(R.id.cancel).setOnClickListener(this);
        mDialog.findViewById(R.id.confirm).setOnClickListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_teach_progress, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.clazzTv.setText(mList.get(position).getClassdes());
        holder.subjectTv.setText(mList.get(position).getSubjectname());
        holder.periodsTv.setText(mList.get(position).getTermname());
        //课程按序号排序
        holder.nameTv.setText(mList.get(position).getClassname());
        //根据状态设置状态按钮的背景 字体颜色
        if (mList.get(position).getState() == 0) {
            holder.stateBt.setText(R.string.un_complete);
            holder.stateBt.setBackgroundResource(R.mipmap.complete);
            holder.stateBt.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.stateBt.setText(R.string.complete);
            holder.stateBt.setBackgroundResource(R.drawable.bg_button_gray);
            holder.stateBt.setTextColor(mContext.getResources().getColor(R.color.tip_text));
        }

        holder.stateBt.setTag(position);
        holder.stateBt.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teach_progress_state:
                if (v.getTag() instanceof Integer) {
                    curClickView = (TextView) v;
                    int state = mList.get((int) curClickView.getTag()).getState();
                    if (state == 0) {
                        dialogTitle.setText(R.string.change_complete);
                    } else {
                        dialogTitle.setText(R.string.change_un_complete);
                    }
                    //弹出对话框
                    mDialog.show();
                }
                break;
            case R.id.cancel:
                mDialog.dismiss();
                break;
            case R.id.confirm:
                commitChangeState();
            default:
                break;
        }
    }

    /**
     * detailId	否	教学计划详细id
     * state	否	是否完成状态：0-未完成；1-已完成
     */
    private void commitChangeState() {
        int position = (int) curClickView.getTag();
        final TeachPlanBean bean = mList.get(position);
        final int state = bean.getState()==0?1:0;
        Map map = new HashMap();
        map.put("detailId", bean.getDetailid());
        map.put("state", state + "");
        VolleyManager.getInstance().postString(BaseDataUtils.SERVER + Constants.changePlanState, map, "", new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                mDialog.dismiss();//更改失败
            }

            @Override
            public void onSucceed(String response) {
                if (state == 1) {
                    curClickView.setText(R.string.complete);
                    curClickView.setBackgroundResource(R.drawable.bg_button_gray);
                    curClickView.setTextColor(mContext.getResources().getColor(R.color.tip_text));
                } else {
                    curClickView.setText(R.string.un_complete);
                    curClickView.setBackgroundResource(R.mipmap.complete);
                    curClickView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                }
                mDialog.dismiss();//更改成功
            }
        });
    }

    private static class ViewHolder {
        private TextView clazzTv;
        private TextView subjectTv;
        private TextView periodsTv;
        private TextView nameTv;
        private TextView stateBt;

        public ViewHolder(View convertView) {
            clazzTv = (TextView) convertView.findViewById(R.id.teach_sel_item1);
            subjectTv = (TextView) convertView.findViewById(R.id.teach_sel_item2);
            periodsTv = (TextView) convertView.findViewById(R.id.teach_sel_item3);
            nameTv = (TextView) convertView.findViewById(R.id.teach_progress_name);
            stateBt = (TextView) convertView.findViewById(R.id.teach_progress_state);
        }
    }
}
