package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.bean.Clazz;
import com.cnst.wisdom.model.bean.LeaveBean;
import com.cnst.wisdom.model.bean.Subject;
import com.cnst.wisdom.model.bean.Term;
import com.cnst.wisdom.ui.adapter.LeaveAd;
import com.cnst.wisdom.ui.adapter.TeachPlanPopSelAd;
import com.cnst.wisdom.ui.view.ListWindow;
import com.cnst.wisdom.ui.view.RefreshListView;
import com.cnst.wisdom.utills.BaseDataUtils;

import java.util.ArrayList;
import java.util.List;

public class LeaveManageAt extends BaseNetActivity implements View.OnClickListener, AdapterView.OnItemClickListener, PopupWindow.OnDismissListener, RefreshListView.OnRefreshListener {
    private RefreshListView mListView;//显示课程的ListView
    private List<LeaveBean> mLeaveBean = new ArrayList<>();//需要显示出来的数据,随筛选条件而变化
    private LeaveAd mAd;//mListView
    private LinearLayout selLayout;
    private ListWindow clazzWin;//班级筛选弹出框
    private ListWindow subWin;//科目筛选弹出框
    private ListWindow termWin;//期数筛选弹出框
    private Clazz clazz;//默认选择第一项,即全部
    private Subject subject;//默认选择第一项,即全部
    private Term term;//默认选择第一项,即全部
    private List<Clazz> classList = new ArrayList<>();//班级种类
    private List<Term> termList = new ArrayList<>();//期数种类
    private List<Subject> subjectList = new ArrayList<>();//学科种类
    private String TAG = getClass().getName();
    private int pageNum = 1;//当前页码
    private int pageSize = 20;//每页条数
    private ProgressBar loadBar;
    private FrameLayout emptyView;
    private boolean isFirst = true;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_leave_manage);
        findViewById(R.id.teach_sel_item1).setOnClickListener(this);
        findViewById(R.id.teach_sel_item2).setOnClickListener(this);
        findViewById(R.id.teach_sel_item3).setOnClickListener(this);
        findViewById(R.id.head_back_action).setOnClickListener(this);

        selLayout = (LinearLayout) findViewById(R.id.teach_sel_layout);
        selLayout.setVisibility(View.GONE);

        mListView = (RefreshListView) findViewById(R.id.teach_sel_list);
        mListView.setVisibility(View.INVISIBLE);
        loadBar = (ProgressBar) findViewById(R.id.pb_loading);
        loadBar.setVisibility(View.GONE);
        mAd = new LeaveAd(this, mLeaveBean);
        mListView.setAdapter(mAd);
        mListView.setOnRefreshListener(this);
        mListView.setDividerHeight(1);
        emptyView = (FrameLayout) findViewById(R.id.add);
        initData();
        classList.add(null);
        subjectList.add(null);
        termList.add(null);
        getPlanData();
    }


    private void addData() {
        for (int i = 0; i < 10; i++) {
            LeaveBean bean = new LeaveBean();
            bean.setDate("1月3日");
            bean.setClazz("小一班");
            bean.setName("林真心");
            bean.setReason("身体不舒服，有点感冒发烧");
            bean.setState(i%3);
            mLeaveBean.add(bean);
        }
        isEmpty();
        mAd.notifyDataSetChanged();
    }

    private void getPlanData() {
        addData();
        mListView.onRefreshComplete(true);
    }

    private void isEmpty() {
        if (mLeaveBean.size() == 0) {
            selLayout.setVisibility(View.GONE);
//            emptyView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            selLayout.setVisibility(View.VISIBLE);
//            emptyView.setVisibility(View.GONE);
        }
    }

    protected void initData() {
        BaseDataUtils.getClazzList(new BaseDataUtils.ReturnData<Clazz>() {
            @Override
            public void refreshData(List<Clazz> list) {
                if (list != null && list.size() > 0) {
                    classList.addAll(list);
                } else {
                    mListView.setBackgroundColor(getResources().getColor(R.color.white));
                    mListView.setFocusable(true);
                }
            }
        });
        BaseDataUtils.getsSubjectList(new BaseDataUtils.ReturnData<Subject>() {
            @Override
            public void refreshData(List<Subject> list) {
                if (list != null && list.size() > 0) {
                    subjectList.addAll(list);
                } else {
                    mListView.setBackgroundColor(getResources().getColor(R.color.white));
                    mListView.setFocusable(true);
                }
            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.teach_sel_item1:
                if (clazzWin == null) {
                    clazzWin = getPopupWindow(v, classList);
                } else {
                    clazzWin.show();
                }
                mListView.setBackgroundColor(getResources().getColor(R.color.popup_main_background));
                mListView.setFocusable(false);
                break;
            case R.id.teach_sel_item2:
                if (subWin == null) {
                    subWin = getPopupWindow(v, subjectList);
                } else {
                    subWin.show();
                }
                mListView.setBackgroundColor(getResources().getColor(R.color.popup_main_background));
                mListView.setFocusable(false);
                break;
            case R.id.teach_sel_item3:
                termList.clear();
                if (subject == null) {
                    return;
                }
                BaseDataUtils.getsTermList(new BaseDataUtils.ReturnData<Term>() {
                    @Override
                    public void refreshData(List<Term> list) {
                        if (list != null && list.size() > 0) {
                            termList.add(null);
                            termList.addAll(list);
                            if (termWin == null) {
                                termWin = getPopupWindow(v, termList);
                            } else {
                                termWin.show();
                            }
                        } else {
                            onDismiss();
                        }
                        if (termWin != null && termWin.getListView() != null) {
                            ((BaseAdapter) termWin.getListView().getAdapter()).notifyDataSetChanged();
                        }
                    }
                }, subject.getPk());
                mListView.setBackgroundColor(getResources().getColor(R.color.popup_main_background));
                mListView.setFocusable(false);
                break;
            case R.id.head_back_action:
                finish();
                break;
            default:
                break;
        }
    }

    private ListWindow getPopupWindow(View v, List data) {
        ListWindow popupWindow = new ListWindow(v, data, this);
        popupWindow.setOnDismissListener(this);
        popupWindow.setItemClickListener(this);
        popupWindow.show();
        return popupWindow;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //获取选中的文本信息
        if (clazzWin != null && parent == clazzWin.getListView()) {
            if (subject != null) {
                subject = null;
                ((TeachPlanPopSelAd) subWin.getListView().getAdapter()).setCurPosition(0);
                if (term != null) {
                    term = null;
                    ((TeachPlanPopSelAd) termWin.getListView().getAdapter()).setCurPosition(0);
                }
            }
            ((TeachPlanPopSelAd) clazzWin.getListView().getAdapter()).setCurPosition(position);
            clazz = classList.get(position);
            clazzWin.dismiss();
        } else if (subWin != null && parent == subWin.getListView()) {
            if (term != null) {
                term = null;
                ((TeachPlanPopSelAd) termWin.getListView().getAdapter()).setCurPosition(0);
            }
            ((TeachPlanPopSelAd) subWin.getListView().getAdapter()).setCurPosition(position);
            subject = subjectList.get(position);
            subWin.dismiss();
        } else if (termWin != null && parent == termWin.getListView()) {
            ((TeachPlanPopSelAd) termWin.getListView().getAdapter()).setCurPosition(position);
            term = termList.get(position);
            termWin.dismiss();
        }
        onRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onRefresh();
    }

    @Override
    public void onDismiss() {
        mListView.setBackgroundColor(getResources().getColor(R.color.white));
        mListView.setFocusable(true);
    }

    @Override
    public void onRefresh() {
        mListView.setBackgroundColor(getResources().getColor(R.color.white));
        mListView.setFocusable(true);
        mLeaveBean.clear();
        mAd.notifyDataSetChanged();
        pageNum = 1;
        getPlanData();
    }

    @Override
    public void onLoadMore() {
        pageNum++;
        getPlanData();
    }
}
