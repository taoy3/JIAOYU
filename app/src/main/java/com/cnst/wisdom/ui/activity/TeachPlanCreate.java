package com.cnst.wisdom.ui.activity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教学计划的教学科目界面
 * 点击班级、学科、期数、教学周期跳出对话框选择相应选项
 * 教学周期需要选择2个日期，再次点击被选日期时，该日期显示会默认后可选择其它日期
 * 当为新建科目时4个选择都完成时确认键显示为高亮，可编辑
 *
 * @author taoyuan.
 * @since 1.0
 */
public class TeachPlanCreate extends BaseNetActivity implements View.OnClickListener, DialogInterface.OnDismissListener, CalendarDialog.SelectorListener {
    private LabelView clazzLayout;
    private LabelView subjectLayout;
    private LabelView termLayout;
    private LabelView cycleLayout;

    private ListDialog mSubjectDialog;
    private ListDialog mTermDialog;
    private ListDialog mClazzDialog;
    private CalendarDialog mCalendarDialog;

    private TextView confirmBt;//确认提交按钮

    private List<Clazz> classList = new ArrayList<>();  //班级种类
    private List<Term> termList = new ArrayList<>();//期数种类
    private List<Subject> subjectList = new ArrayList<>();//学科种类

    private TeachPlanBean mPlanBean = new TeachPlanBean();
    private SimpleDateFormat sdf = new SimpleDateFormat("yy-mm-dd");
    private String TAG = getClass().getName();

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_teach_create);
        initView();
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
        subjectLayout.setClickable(false);
        mSubjectDialog = new ListDialog(this, subjectList);
        mSubjectDialog.setOnDismissListener(this);

        termLayout = (LabelView) findViewById(R.id.teach_set_term);
        termLayout.setOnClickListener(this);
        termLayout.setClickable(false);
        mTermDialog = new ListDialog(this, termList);
        mTermDialog.setOnDismissListener(this);

        cycleLayout = (LabelView) findViewById(R.id.teach_set_cycle);
        cycleLayout.setOnClickListener(this);
        cycleLayout.setClickable(false);

        confirmBt = (TextView) findViewById(R.id.teach_plan_confirm);
        confirmBt.setOnClickListener(this);
        confirmBt.setClickable(false);
    }


    @Override
    public void onClick(View v) {
        v.setClickable(false);
        switch (v.getId()) {
            case R.id.teach_set_clazz:
                if (classList.size() > 0) {
                    mClazzDialog.show();
                    v.setClickable(true);
                }
                break;
            case R.id.teach_set_subject:
                mPlanBean.setTermId(null);
                refreshSubjectData();
                break;
            case R.id.teach_set_term:
                refreshTermData();
                break;
            case R.id.teach_set_cycle:
                showDateView();
                break;
            case R.id.teach_plan_confirm:
                commitPlan();
                break;
            case R.id.head_back_action:
                finish();
                break;
            default:
                break;
        }
    }

    //刷新期数列表
    private void refreshTermData() {
        termList.clear();
        mTermDialog.getAdapter().notifyDataSetChanged();
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

    //刷新科目列表
    private void refreshSubjectData() {
        subjectList.clear();
        mSubjectDialog.getAdapter().notifyDataSetChanged();
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

    //刷新班级列表
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

    //提交教学计划
//    请求参数名	是否可空	说明
//    classId	否	班级id
//    subjectId	否	学科id
//    termId	否	学期id
//    beginTime	否	开始日期，格式yyyy-MM-dd
//    endTime	否	结束日期，格式yyyy-MM-dd
//    startWeekId	否	教学起始周id
    private void commitPlan() {
        Map<String, String> map = new HashMap();
        map.put("classId", mPlanBean.getClassId());
        map.put("subjectId", mPlanBean.getSubjectId());
        map.put("termId", mPlanBean.getTermId());
        map.put("beginTime", mPlanBean.getStartdate());
        map.put("endTime", mPlanBean.getEnddate());
        VolleyManager.getInstance().postString(BaseDataUtils.SERVER + Constants.saveTeachPlan, map, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                if (error.networkResponse != null) {
                    byte[] errorBody = error.networkResponse.data;
                    Toast.makeText(TeachPlanCreate.this, errorBody.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TeachPlanCreate.this, "保存失败", Toast.LENGTH_LONG).show();
                }
                confirmBt.setClickable(true);
            }

            @Override
            public void onSucceed(String response) {
                int code = 0;
                try {
                    String msg = new JSONObject(response).getString("msg");
                    code = new JSONObject(response).getInt("code");
                    Toast.makeText(TeachPlanCreate.this, msg, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code == Constants.STATUS_SUCCESS) {
                    finish();//提交成功
                }
            }
        });
    }

    //显示日期对话框
    private void showDateView() {
        if (mCalendarDialog == null) {
            mCalendarDialog = new CalendarDialog(this, this);
        }
        mCalendarDialog.calV.setMaxSel(2);//设置可选2个日期
        mCalendarDialog.show();
        mCalendarDialog.setOnDismissListener(this);
        cycleLayout.setClickable(true);
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

    //日期界面返回值的获取
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
