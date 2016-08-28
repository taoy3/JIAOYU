package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.Clazz;
import com.cnst.wisdom.model.bean.Subject;
import com.cnst.wisdom.model.bean.TeachBaseBean;
import com.cnst.wisdom.model.bean.TeachPlanBean;
import com.cnst.wisdom.model.bean.Term;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.adapter.TeachModAd;
import com.cnst.wisdom.ui.adapter.TeachPlanPopSelAd;
import com.cnst.wisdom.ui.view.ListWindow;
import com.cnst.wisdom.ui.view.RefreshListView;
import com.cnst.wisdom.utills.BaseDataUtils;
import com.cnst.wisdom.utills.GeoUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教学计划的修改计划主界面
 * 显示当前用户的所有教学计划
 * 可通过班级 学科 期数进行筛选
 * 通过此页面修改教学计划
 *
 * @author taoyuan.
 * @since 1.0
 */
public class TeachPlanModList extends BaseNetActivity implements View.OnClickListener, AdapterView.OnItemClickListener, PopupWindow.OnDismissListener, RefreshListView.OnRefreshListener {
    private RefreshListView mListView;//显示课程的ListView
    private List<TeachPlanBean> mPlanBeans = new ArrayList<>();//需要显示出来的数据,随筛选条件而变化
    private TeachModAd mAd;//mListView
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
    public static int BACKCODE = 1;
    private final int ADDCODE = 2;
    private ImageButton addBt;
    private FrameLayout emptyView;
    private ProgressBar loadBar;
    private boolean isFirst = true;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_teach_list);
        initView();
        initData();
        classList.add(null);//加入空值，在Adapter中设置当值为空时显示全部
        subjectList.add(null);
        termList.add(null);
        onRefresh();
    }

    protected void initView() {
        findViewById(R.id.teach_sel_item1).setOnClickListener(this);
        findViewById(R.id.teach_sel_item2).setOnClickListener(this);
        findViewById(R.id.teach_sel_item3).setOnClickListener(this);
        selLayout = (LinearLayout) findViewById(R.id.teach_sel_layout);
        selLayout.setVisibility(View.GONE);
        emptyView = (FrameLayout) findViewById(R.id.add);
        emptyView.getChildAt(1).setBackgroundResource(R.mipmap.add_light);
        mListView = (RefreshListView) findViewById(R.id.teach_sel_list);
        mListView.setOnRefreshListener(this);
        mListView.setVisibility(View.INVISIBLE);
        mListView.setDividerHeight(1);
        emptyView.setOnClickListener(this);
        mAd = new TeachModAd(this, mPlanBeans);
        mListView.setAdapter(mAd);
    }

    private void getPlanData() {
        Map map = new HashMap();
        if (clazz != null) {
            map.put("classId", clazz.getId());
        }
        if (subject != null) {
            map.put("subjectId", subject.getPk());
        }
        if (term != null) {
            map.put("termId", term.getPk());
        }
        map.put("pageNum", pageNum + "");
        map.put("pageSize", pageSize + "");
        VolleyManager.getInstance().getString(BaseDataUtils.SERVER + Constants.getPlanModifyList, map, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                mListView.onRefreshComplete(false);
                emptyView.setVisibility(View.VISIBLE);
                ((TextView)emptyView.getChildAt(0)).setText(R.string.player_tips_not_responding);
                isFirst = false;
            }

            @Override
            public void onSucceed(String response) {
                mListView.onRefreshComplete(true);
                loadBar.setVisibility(View.GONE);
                if (response != null && response.length() > 0) {
                    TeachBaseBean<TeachPlanBean> temp = new GeoUtil()
                            .deserializer(response, new TypeToken<TeachBaseBean<TeachPlanBean>>() {
                            }.getType());
                    if (temp != null && temp.getData() != null && temp.getData().size() > 0) {
                        mPlanBeans.addAll(temp.getData());
                        mAd.notifyDataSetChanged();
                    }
                }
                if (isFirst) {
                    isEmpty();
                    isFirst = false;
                }
            }

        });
    }

    private void isEmpty() {
        if (mPlanBeans.size() == 0) {
            selLayout.setVisibility(View.GONE);
            addBt.setVisibility(View.INVISIBLE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.getChildAt(0).setVisibility(View.GONE);
            emptyView.getChildAt(1).setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            findViewById(R.id.underline_gray).setVisibility(View.VISIBLE);
            selLayout.setVisibility(View.VISIBLE);
            addBt.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
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
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.add:
                emptyView.setVisibility(View.INVISIBLE);
                startActivityForResult(new Intent(this, TeachPlanCreate.class), ADDCODE);
                break;
            case R.id.teach_sel_item1:
                selClass(v);
                break;
            case R.id.teach_sel_item2:
                selSubject(v);
                break;
            case R.id.teach_sel_item3:
                selTerm(v);
                break;
            case R.id.head_back_action:
                finish();
                return;
            default:
                break;
        }
    }

    private void selTerm(final View v) {
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
    }

    private void selSubject(View v) {
        if (term != null) {
            term = null;
            termWin.getListView().setSelection(0);
        }
        if (subWin == null) {
            subWin = getPopupWindow(v, subjectList);
        } else {
            subWin.show();
        }
        mListView.setBackgroundColor(getResources().getColor(R.color.popup_main_background));
        mListView.setFocusable(false);
    }

    private void selClass(View v) {
        if (subject != null) {
            subject = null;
            subWin.getListView().setSelection(0);
            if (term != null) {
                term = null;
                termWin.getListView().setSelection(0);
            }
        }

        if (clazzWin == null) {
            clazzWin = getPopupWindow(v, classList);
        } else {
            clazzWin.show();
        }
        mListView.setBackgroundColor(getResources().getColor(R.color.popup_main_background));
        mListView.setFocusable(false);
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
        mListView.setVisibility(View.INVISIBLE);
        selLayout.setVisibility(View.INVISIBLE);
        findViewById(R.id.underline_gray).setVisibility(View.INVISIBLE);
        isFirst = true;
        clazz = null;
        subject = null;
        term = null;
        mPlanBeans.clear();
        mAd.notifyDataSetChanged();
        pageNum = 1;
        getPlanData();
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
        mPlanBeans.clear();
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
