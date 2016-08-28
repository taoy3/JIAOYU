package com.cnst.wisdom.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.Clazz;
import com.cnst.wisdom.model.bean.Subject;
import com.cnst.wisdom.model.bean.TeachPlanBean;
import com.cnst.wisdom.model.bean.Term;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.view.LabelView;
import com.cnst.wisdom.ui.view.ListDialog;
import com.cnst.wisdom.ui.widget.calendar.CalendarDialog;
import com.cnst.wisdom.utills.BaseDataUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 修改教学计划界面
 * 点击班级、学科、期数、教学周期跳出对话框选择相应选项
 * 教学周期需要选择2个日期，再次点击被选日期时，该日期显示会默认后可选择其它日期
 * 当有修改时确认键显示为高亮，可编辑
 *
 * @author taoyuan.
 * @since 1.0
 */
public class TeachPlanModify extends BaseNetActivity implements View.OnClickListener, DialogInterface.OnDismissListener, CalendarDialog.SelectorListener {
    private TextView delBt;
    private TextView confirmBt;//确认提交按钮

    private LabelView clazzLayout;
    private LabelView subjectLayout;
    private LabelView termLayout;
    private LabelView cycleLayout;

    private ListDialog mSubjectDialog;
    private ListDialog mTermDialog;
    private ListDialog mClazzDialog;
    private CalendarDialog mCalendarDialog;

    private List<Clazz> classList = new ArrayList<>();  //班级种类
    private List<Term> termList = new ArrayList<>();//期数种类
    private List<Subject> subjectList = new ArrayList<>();//学科种类

    private TeachPlanBean mPlanBean;
    private SimpleDateFormat sdf = new SimpleDateFormat("yy-mm-dd");
    private String TAG = getClass().getName();
    private Dialog mDialog;
    private TextView dialogTitle;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_teach_create);
        initView();
        getPlanInfo();
        initClazzData();
    }

    @Override
    protected void initData() {

    }

    protected void initView() {
        clazzLayout = (LabelView) findViewById(R.id.teach_set_clazz);
        clazzLayout.setOnClickListener(this);
        mClazzDialog = new ListDialog(this, classList);
        mClazzDialog.setOnDismissListener(this);

        subjectLayout = (LabelView) findViewById(R.id.teach_set_subject);
        subjectLayout.setOnClickListener(this);
        subjectLayout.setSelectedAble(true);
        mSubjectDialog = new ListDialog(this, subjectList);
        mSubjectDialog.setOnDismissListener(this);

        termLayout = (LabelView) findViewById(R.id.teach_set_term);
        termLayout.setOnClickListener(this);
        termLayout.setSelectedAble(true);
        mTermDialog = new ListDialog(this, termList);
        mTermDialog.setOnDismissListener(this);

        cycleLayout = (LabelView) findViewById(R.id.teach_set_cycle);
        cycleLayout.setOnClickListener(this);
        cycleLayout.setSelectedAble(true);

        confirmBt = (TextView) findViewById(R.id.teach_plan_confirm);
        delBt = (TextView) findViewById(R.id.teach_plan_del);
        findViewById(R.id.head_back_action).setOnClickListener(this);
        delBt.setVisibility(View.VISIBLE);
        delBt.setClickable(true);
        delBt.setOnClickListener(this);
        confirmBt.setText(R.string.confirm_mof);
        confirmBt.setOnClickListener(this);
        confirmBt.setClickable(false);

        mDialog = new Dialog(this, R.style.no_title_dialog);
        mDialog.setContentView(R.layout.dialog_sel_subject_state);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialogTitle = (TextView) mDialog.findViewById(R.id.dialog_title);
        mDialog.findViewById(R.id.cancel).setOnClickListener(this);
        mDialog.findViewById(R.id.confirm).setOnClickListener(this);
    }

    private void getPlanInfo() {
        Serializable data = getIntent().getSerializableExtra(TAG);
        mPlanBean = (TeachPlanBean) data;
        clazzLayout.setText(mPlanBean.getClassdes());
        subjectLayout.setText(mPlanBean.getSubjectname());
        termLayout.setText(mPlanBean.getTermname());
        cycleLayout.setText(mPlanBean.getStartdate() + CalendarDialog.DATESPACE + mPlanBean.getEnddate());
        Map<String, String> map = new HashMap<>();
        map.put("planId", mPlanBean.getPlanid());
        VolleyManager.getInstance().getString(BaseDataUtils.SERVER + Constants.getTeachPlanInfo, map, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {

            }

            @Override
            public void onSucceed(String response) {
                if (response != null && response.length() > 0) {
                    Gson gson = new Gson();
                    try {
                        JSONObject object = new JSONObject(response);
                        String plan = object.getString("data");
                        TeachPlanBean bean = gson.fromJson(plan, TeachPlanBean.class);
                        mPlanBean.setClassId(bean.getClassId());
                        mPlanBean.setSubjectId(bean.getSubjectId());
                        mPlanBean.setTermId(bean.getTermId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teach_set_clazz:
                if(classList.size()>0){
                    mClazzDialog.show();
                    v.setClickable(true);
                }
                break;
            case R.id.teach_set_subject:
                refreshSubjectData();
                break;
            case R.id.teach_set_term:
                refreshTermData();
                break;
            case R.id.teach_set_cycle:
                showDateView();
                break;
            case R.id.teach_plan_confirm:
                dialogTitle.setText("该教学计划下的课前指导和教学进度也将被修改\n是否修改？");
                mDialog.show();
                break;
            case R.id.teach_plan_del:
                dialogTitle.setText("该教学计划下的课前指导和教学进度也将被删除\n是否删除？");
                mDialog.show();
                break;
            case R.id.confirm:
                if (dialogTitle.getText().toString().contains("修改")) {
                    modPlan();
                } else {
                    delPlan();
                }
                mDialog.dismiss();
                break;
            case R.id.cancel:
                mDialog.dismiss();
                break;
            case R.id.head_back_action:
                finish();
                break;
            default:
                break;
        }

    }

    private void refreshTermData() {
        termLayout.setClickable(false);
        termList.clear();
        BaseDataUtils.getsTermList(new BaseDataUtils.ReturnData<Term>() {
            @Override
            public void refreshData(List<Term> list) {
                termLayout.setClickable(true);
                if (list != null && list.size() > 0) {
                    termList.addAll(list);
                    for (int i = 0; i < termList.size(); i++) {
                        if (termList.get(i).getPk().equals(mPlanBean.getTermId())) {
                            mTermDialog.getAdapter().setCurPosition(i);
                            break;
                        }
                    }
                    mTermDialog.getAdapter().notifyDataSetChanged();
                    mTermDialog.show();
                }
            }
        }, mPlanBean.getSubjectId());
    }

    private void refreshSubjectData() {
        subjectLayout.setClickable(false);
        subjectList.clear();
        BaseDataUtils.getsSubjectList(new BaseDataUtils.ReturnData<Subject>() {
            @Override
            public void refreshData(List<Subject> list) {
                subjectLayout.setClickable(true);
                if (list != null && list.size() > 0) {
                    subjectList.addAll(list);
                    for (int i = 0; i < subjectList.size(); i++) {
                        if (subjectList.get(i).getPk().equals(mPlanBean.getSubjectId())) {
                            mSubjectDialog.getAdapter().setCurPosition(i);
                            break;
                        }
                    }
                    mSubjectDialog.getAdapter().notifyDataSetChanged();
                    mSubjectDialog.show();
                }
            }
        });
    }

    private void initClazzData() {
        BaseDataUtils.getClazzList(new BaseDataUtils.ReturnData<Clazz>() {
            @Override
            public void refreshData(List<Clazz> list) {
                clazzLayout.setClickable(true);
                if (list != null && list.size() > 0) {
                    classList.addAll(list);
                    mClazzDialog.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * planId	否	教学计划主键
     * classId	否	班级id
     * subjectId	否	学科id
     * termId	否	学期id
     * beginTime	否	开始日期，格式yyyy-MM-dd
     * endTime	否	结束日期，格式yyyy-MM-dd
     * startWeekId	否	教学起始周id
     */
    private void modPlan() {
        confirmBt.setClickable(false);
        Map<String, String> map = new HashMap();
        map.put("planId", mPlanBean.getPlanid());
        map.put("classId", mPlanBean.getClassId());
        map.put("subjectId", mPlanBean.getSubjectId());
        map.put("termId", mPlanBean.getTermId());
        map.put("beginTime", mPlanBean.getStartdate());
        map.put("endTime", mPlanBean.getEnddate());
        map.put("startWeekId", System.currentTimeMillis() + "");
        VolleyManager.getInstance().postString(BaseDataUtils.SERVER + Constants.modifyTeachPlan, map, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                confirmBt.setClickable(true);
                if (error.networkResponse != null) {
                    byte[] errorBody = error.networkResponse.data;
                    Toast.makeText(TeachPlanModify.this, errorBody.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TeachPlanModify.this, "保存失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSucceed(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    int code = object.getInt("code");
                    String msg = object.getString("msg");
                    if(code==Constants.STATUS_SUCCESS){
                        finish();//删除成功
                    }else {
                        Toast.makeText(TeachPlanModify.this,msg,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void delPlan() {
        delBt.setClickable(false);
        Map map = new HashMap();
        map.put("planId", mPlanBean.getPlanid());
        VolleyManager.getInstance().postString(BaseDataUtils.SERVER + Constants.delTeachPlan, map, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                delBt.setClickable(true);
                if (error.networkResponse != null) {
                    byte[] errorBody = error.networkResponse.data;
                    Toast.makeText(TeachPlanModify.this, errorBody.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TeachPlanModify.this, "删除失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSucceed(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    int code = object.getInt("code");
                    String msg = object.getString("msg");
                    if (code == Constants.STATUS_SUCCESS) {
                        finish();//删除成功
                    } else {
                        Toast.makeText(TeachPlanModify.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDateView() {
        if (mCalendarDialog == null) {
            mCalendarDialog = new CalendarDialog(this, this);
        }
        mCalendarDialog.calV.setMaxSel(2);
        mCalendarDialog.show();
        mCalendarDialog.setOnDismissListener(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //判断是不是ListDialog并且选择中了Item
        int selPostion = -1;
        if (dialog instanceof ListDialog) {
            ListDialog listDialog = (ListDialog) dialog;
            selPostion = listDialog.getAdapter().getCurPosition();
            if (selPostion < 0) {
                return;
            }
        }
        if (dialog == mClazzDialog) {
            Clazz clazz = classList.get(selPostion);
            clazzLayout.setText(clazz.getName());
            mPlanBean.setClassId(clazz.getId());
            subjectLayout.setSelectedAble(true);
            termLayout.setSelectedAble(false);
            cycleLayout.setSelectedAble(false);
            confirmBt.setClickable(false);
            confirmBt.setTextColor(getResources().getColor(R.color.tip_text));
            confirmBt.setBackgroundResource(R.drawable.bg_button_gray);
        } else if (dialog == mSubjectDialog) {
            Subject subject = subjectList.get(selPostion);
            subjectLayout.setText(subject.getName());
            mPlanBean.setSubjectId(subject.getPk());
            termLayout.setSelectedAble(true);
            cycleLayout.setSelectedAble(false);
            confirmBt.setClickable(false);
            confirmBt.setTextColor(getResources().getColor(R.color.tip_text));
            confirmBt.setBackgroundResource(R.drawable.bg_button_gray);
        } else if (dialog == mTermDialog) {
            Term term = termList.get(selPostion);
            termLayout.setText(term.getName());
            mPlanBean.setTermId(term.getPk());
            cycleLayout.setSelectedAble(true);
            confirmBt.setClickable(false);
            confirmBt.setTextColor(getResources().getColor(R.color.tip_text));
            confirmBt.setBackgroundResource(R.drawable.bg_button_gray);
        }
    }


    @Override
    public void onSelector(String date) {
        cycleLayout.setText(date);
        String[] time = date.split(CalendarDialog.DATESPACE);
        mPlanBean.setStartdate(time[0]);
        mPlanBean.setEnddate(time[1]);
        cycleLayout.setClickable(true);
        confirmBt.setClickable(true);
        confirmBt.setTextColor(getResources().getColor(R.color.white));
        confirmBt.setBackgroundResource(R.drawable.bg_button_clickable);
    }

    @Override
    protected void onDestroy() {
        VolleyManager.getInstance().mRequestQueue.cancelAll(TAG);
        super.onDestroy();
    }
}
