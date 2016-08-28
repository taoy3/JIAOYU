package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
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
import com.cnst.wisdom.ui.adapter.TeachPlanPopSelAd;
import com.cnst.wisdom.ui.adapter.TeachProgressAd;
import com.cnst.wisdom.ui.view.ListWindow;
import com.cnst.wisdom.ui.view.RefreshListView;
import com.cnst.wisdom.utills.BaseDataUtils;
import com.cnst.wisdom.utills.GeoUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教学计划的教学进度主界面
 * 显示当前用户的所有教学计划
 * 可通过班级 学科 期数进行筛选
 * 通过此页面查看及修改教学进度
 *
 * @author taoyuan.
 * @since 1.0
 */
public class TeachPlanProgress extends BaseNetActivity implements View.OnClickListener, AdapterView.OnItemClickListener, PopupWindow.OnDismissListener, RefreshListView.OnRefreshListener {
    private RefreshListView mListView;//显示课程的ListView
    private List<TeachPlanBean> mPlanBeans = new ArrayList<>();//需要显示出来的数据,随筛选条件而变化
    private TeachProgressAd mAd;//mListView
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
    private ArrayList<String> mPlanIds = new ArrayList<>();
    private int planIndex;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_teach_list);
        initView();
        initData();
        classList.add(null);
        subjectList.add(null);
        termList.add(null);
        getPlanTerm();
    }

    protected void initView() {
        findViewById(R.id.teach_sel_item1).setOnClickListener(this);
        findViewById(R.id.teach_sel_item2).setOnClickListener(this);
        findViewById(R.id.teach_sel_item3).setOnClickListener(this);
        findViewById(R.id.head_back_action).setOnClickListener(this);

        selLayout = (LinearLayout) findViewById(R.id.teach_sel_layout);
        selLayout.setVisibility(View.GONE);

        mListView = (RefreshListView) findViewById(R.id.teach_sel_list);
        mListView.setVisibility(View.INVISIBLE);
        loadBar = (ProgressBar) findViewById(R.id.pb_loading);
        mAd = new TeachProgressAd(this, mPlanBeans);
        mListView.setAdapter(mAd);
        mListView.setOnRefreshListener(this);
        mListView.setDividerHeight(1);
        emptyView = (FrameLayout) findViewById(R.id.add);
    }


    private void getPlanTerm() {
        Map map = new HashMap();
        map.put("pageNum", pageNum + "");
        map.put("pageSize", pageSize + "");
        VolleyManager.getInstance().getString(BaseDataUtils.SERVER + Constants.getPlanModifyList, map, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                dowFail();
            }

            @Override
            public void onSucceed(String response) {
                if (response != null && response.length() > 0) {
                    TeachBaseBean<TeachPlanBean> temp = new GeoUtil()
                            .deserializer(response, new TypeToken<TeachBaseBean<TeachPlanBean>>() {
                            }.getType());
                    if (temp != null && temp.getData() != null && temp.getData().size() > 0) {
                        for (TeachPlanBean bean : temp.getData()) {
                            mPlanIds.add(bean.getPlanid());
                        }
                        getPlanData();
                    }else {
                        isEmpty();
                    }
                }
            }

        });

    }

    private void getPlanData() {
        if (isFirst) {
            loadBar.setVisibility(View.VISIBLE);
        }
        Map<String, String> map = new HashMap();
        if (clazz != null) {
            map.put("classId", clazz.getId());
        }
        if (subject != null) {
            map.put("subjectId", subject.getPk());
        }
        if (term != null) {
            map.put("termId", term.getPk());
        }
        if (planIndex >= mPlanIds.size()) {
            mListView.onRefreshComplete(true);
            return;
        }
        map.put("planId", mPlanIds.get(planIndex));

        VolleyManager.getInstance().getString(BaseDataUtils.SERVER + Constants.getTeachPlanList, map, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                mListView.onRefreshComplete(false);
                dowFail();
            }

            @Override
            public void onSucceed(String response) {
                mListView.onRefreshComplete(true);
                if (response != null && response.length() > 0) {

                    try {
                        JSONObject rootObject = new JSONObject(response);
                        JSONArray dataArray = rootObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONArray classNameArray = dataArray.getJSONObject(i).getJSONArray("classNameList");
                            for (int j = 0; j < classNameArray.length(); j++) {
                                TeachPlanBean bean = new TeachPlanBean();
                                JSONObject object = classNameArray.getJSONObject(j);
                                bean.setDetailid(object.getString("detailid"));
                                bean.setPlanid(object.getString("planid"));
                                bean.setClassdes(object.getString("classdes"));
                                bean.setState(object.getInt("state"));
                                bean.setSubjectname(object.getString("subjectname"));
                                bean.setTermname(object.getString("termname"));
                                bean.setClassname(object.getString("classname"));
                                mPlanBeans.add(bean);
                            }
                        }
                        if (mPlanBeans.size() < 20 && planIndex < mPlanIds.size() - 1) {
                            planIndex++;
                            getPlanData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (isFirst) {
                    isEmpty();
                    isFirst = false;
                }
                mAd.notifyDataSetChanged();
            }
        });
    }

    private void dowFail() {
        emptyView.setVisibility(View.VISIBLE);
        loadBar.setVisibility(View.GONE);
        ((TextView) emptyView.getChildAt(0)).setText(R.string.player_tips_not_responding);
        isFirst = false;
    }

    private void isEmpty() {
        loadBar.setVisibility(View.GONE);
        if (mPlanBeans.size() == 0) {
            selLayout.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            selLayout.setVisibility(View.VISIBLE);
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
                break;
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
        if (subWin == null) {
            subWin = getPopupWindow(v, subjectList);
        } else {
            subWin.show();
        }
        mListView.setBackgroundColor(getResources().getColor(R.color.popup_main_background));
        mListView.setFocusable(false);
    }

    private void selClass(View v) {
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
        mPlanBeans.clear();
        mAd.notifyDataSetChanged();
        planIndex = 0;
        getPlanData();
    }

    @Override
    public void onLoadMore() {
        planIndex++;
        getPlanData();
    }
}
