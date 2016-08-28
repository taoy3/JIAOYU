package com.cnst.wisdom.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnst.wisdom.R;
import com.cnst.wisdom.model.bean.LeaveBean;

import java.util.List;

/**
 * 教学计划的教学进度ListView Item界面
 * 显示班级 科目 期数 课程名 状态按钮
 * 默认未完成显示在上面 点击此按钮弹出对话框切换状态
 *
 * @author taoyuan.
 * @since 1.0
 */
public class LeaveAd extends TeachPlanBaseAdapter<LeaveBean> implements View.OnClickListener {
    private Dialog mDialog;
    private TextView dialogTitle;
    private TextView curClickView;//当前修改的控件

    public LeaveAd(Context context, List<LeaveBean> list) {
        super(context, list);
        mDialog = new Dialog(context, R.style.no_title_dialog);
        mDialog.setContentView(R.layout.dialog_sel_subject_state);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialogTitle = (TextView) mDialog.findViewById(R.id.dialog_title);
        dialogTitle.setGravity(Gravity.LEFT);
        dialogTitle.setLineSpacing(3, 1.2f);
        int pad = (int) context.getResources().getDimension(R.dimen.padding_20);
        dialogTitle.setPadding(pad,pad,pad,pad);
        TextView refuseTv = (TextView) mDialog.findViewById(R.id.cancel);
        refuseTv.setOnClickListener(this);
        refuseTv.setText(R.string.refuse);
        TextView agreeTv = (TextView) mDialog.findViewById(R.id.confirm);
        agreeTv.setOnClickListener(this);
        agreeTv.setText(R.string.agree);
        mDialog.findViewById(R.id.confirm).setOnClickListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_leave_manage, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.dateTv.setText(mList.get(position).getDate());
        holder.nameTv.setText(mList.get(position).getClazz()+"\t\t"+mList.get(position).getName());
        holder.reasonTv.setText(mList.get(position).getReason());
        //根据状态设置状态按钮的背景 字体颜色
        if (mList.get(position).getState() == 0) {
            holder.stateBt.setText(R.string.un_affirm);
            holder.stateBt.setBackgroundResource(R.drawable.bg_button_un_affirm);
            holder.stateBt.setTextColor(mContext.getResources().getColor(R.color.un_affirm));
        } else if(mList.get(position).getState() == 1){
            holder.stateBt.setText(R.string.agree);
            holder.stateBt.setBackgroundResource(R.drawable.bg_button_agree);
            holder.stateBt.setTextColor(mContext.getResources().getColor(R.color.holo_green_light));
        }else if(mList.get(position).getState() == 2){
            holder.stateBt.setText(R.string.refuse);
            holder.stateBt.setBackgroundResource(R.drawable.bg_button_refuse);
            holder.stateBt.setTextColor(mContext.getResources().getColor(R.color.red));
        }

        holder.stateBt.setTag(position);
        holder.stateBt.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leave_manage_state:
                if (v.getTag() instanceof Integer) {
                    curClickView = (TextView) v;
                    LeaveBean bean = mList.get((int) curClickView.getTag());
                    dialogTitle.setText("请假人："+bean.getName()+"\n"+"请假日期："+bean.getDate()+"\n"+"事由："+bean.getReason());
                    //弹出对话框
                    mDialog.show();
                }
                break;
            case R.id.cancel:
                commitChangeState(2);
                mDialog.dismiss();
                break;
            case R.id.confirm:
                commitChangeState(1);
                mDialog.dismiss();//更改成功
            default:
                break;
        }
    }

    /**
     * detailId	否	教学计划详细id
     * state	否	是否完成状态：0-未完成；1-已完成
     */
    private void commitChangeState(int state) {
        int position = (int) curClickView.getTag();
        final LeaveBean bean = mList.get(position);
        bean.setState(state);
        if (state == 1){
            curClickView.setText(R.string.agree);
            curClickView.setBackgroundResource(R.drawable.bg_button_agree);
            curClickView.setTextColor(mContext.getResources().getColor(R.color.holo_green_light));
        }else if (state == 2){
            curClickView.setText(R.string.refuse);
            curClickView.setBackgroundResource(R.drawable.bg_button_refuse);
            curClickView.setTextColor(mContext.getResources().getColor(R.color.red));
        }


//        Map map = new HashMap();
//        map.put("detailId", bean.getId());
//        map.put("state", state + "");
//        VolleyManager.getInstance().postString(BaseDataUtils.SERVER + Constants.changePlanState, map, "", new NetResult<String>() {
//            @Override
//            public void onFailure(VolleyError error) {
//                mDialog.dismiss();//更改失败
//            }
//
//            @Override
//            public void onSucceed(String response) {
//                bean.setState(state);
//                if (state == 1) {
//                    curClickView.setText(R.string.complete);
//                    curClickView.setBackgroundResource(R.drawable.bg_button_gray);
//                    curClickView.setTextColor(mContext.getResources().getColor(R.color.tip_text));
//                } else {
//                    curClickView.setText(R.string.un_complete);
//                    curClickView.setBackgroundResource(R.mipmap.complete);
//                    curClickView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
//                }
//                mDialog.dismiss();//更改成功
//            }
//        });
    }

    private static class ViewHolder {
        private TextView dateTv;
        private TextView reasonTv;
        private TextView nameTv;
        private TextView stateBt;

        public ViewHolder(View convertView) {
            dateTv = (TextView) convertView.findViewById(R.id.leave_manage_date);
            nameTv = (TextView) convertView.findViewById(R.id.leave_manage_name);
            reasonTv = (TextView) convertView.findViewById(R.id.leave_manage_reason);
            stateBt = (TextView) convertView.findViewById(R.id.leave_manage_state);
        }
    }
}
