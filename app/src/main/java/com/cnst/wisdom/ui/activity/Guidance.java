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
import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.Course;
import com.cnst.wisdom.model.bean.CourseDetail;
import com.cnst.wisdom.model.bean.CourseDetailBean;
import com.cnst.wisdom.model.bean.Login;
import com.cnst.wisdom.model.bean.Subject;
import com.cnst.wisdom.model.bean.TeachBaseBean;
import com.cnst.wisdom.model.bean.TeachPlanBean;
import com.cnst.wisdom.model.bean.Term;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.adapter.GuidanceAdapter;
import com.cnst.wisdom.ui.adapter.TeachPlanPopSelAd;
import com.cnst.wisdom.ui.view.ListWindow;
import com.cnst.wisdom.ui.view.RefreshListView;
import com.cnst.wisdom.utills.BaseDataUtils;
import com.cnst.wisdom.utills.GeoUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课前指导界面
 * 可通过班级 学科 期数进行筛选
 *
 * @author taoyuan.
 * @since 1.0
 */
public class Guidance extends BaseNetActivity implements View.OnClickListener, AdapterView.OnItemClickListener, PopupWindow.OnDismissListener, RefreshListView.OnRefreshListener {
    private RefreshListView mListView;//显示课程的ListView
    private List<CourseDetail> courseDetailList = new ArrayList<>();//需要显示出来的数据,随筛选条件而变化
    private GuidanceAdapter adapter;
    private ListWindow courseWin;//课程名筛选弹出框
    private ListWindow subWin;//科目筛选弹出框
    private ListWindow termWin;//期数筛选弹出框
    private Course course;//默认选择第一项,即全部
    private Subject subject;//默认选择第一项,即全部
    private Term term;//默认选择第一项,即全部
    private List<Course> courseList = new ArrayList<>();//班级种类
    private List<Term> termList = new ArrayList<>();//期数种类
    private List<Subject> subjectList = new ArrayList<>();//学科种类
    private String TAG = getClass().getName();
    private int pageNum = 1;//当前页码
    private int pageSize = 200;//每页条数
    private LinearLayout termLayout;
    private LinearLayout courseLayout;
    private ProgressBar loadBar;
    private FrameLayout emptyView;
    private LinearLayout selLayout;
    private boolean isFirst = true;
    private ArrayList<String> mPlanIds = new ArrayList<>();
    private int planIndex;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_teach_list);

        findViewById(R.id.teach_sel_item1).setOnClickListener(this);
        termLayout = (LinearLayout) findViewById(R.id.teach_sel_item2);
        termLayout.setOnClickListener(this);
        courseLayout = (LinearLayout) findViewById(R.id.teach_sel_item3);
        courseLayout.setOnClickListener(this);

        TextView subjectTv = (TextView) findViewById(R.id.teach_sel_text1);
        subjectTv.setText(R.string.subject);
        TextView termTv = (TextView) findViewById(R.id.teach_sel_text2);
        termTv.setText(R.string.term);
        TextView courseTv = (TextView) findViewById(R.id.teach_sel_text3);
        courseTv.setText(R.string.course);

        selLayout = (LinearLayout) findViewById(R.id.teach_sel_layout);
        selLayout.setVisibility(View.GONE);


        mListView = (RefreshListView) findViewById(R.id.teach_sel_list);
        mListView.setVisibility(View.INVISIBLE);
        emptyView = (FrameLayout) findViewById(R.id.add);
        ((TextView) emptyView.getChildAt(0)).setText("暂无素材");
        adapter = new GuidanceAdapter(this, courseDetailList);
        mListView.setAdapter(adapter);
        mListView.setDividerHeight(1);
        mListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CourseDetail courseDetail = courseDetailList.get(position);
                if (courseDetail.isVideo()) {
                    Intent intent = new Intent(Guidance.this, GuidanceDetailActivity.class);
                    intent.putExtra("vid", courseDetail.getVideoId());
                    intent.putExtra("course", courseDetail);
//                    intent.putExtra("course", courseDetail);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Guidance.this, PictureActivity.class);
                    intent.putExtra("resourceId", courseDetailList.get(position).getResourceId());
                    intent.putExtra("filePath", courseDetailList.get(position).getFilePath());
                    intent.putExtra("pic_name", courseDetailList.get(position).getClassname());
                    intent.putExtra("code", "tupian");
                    intent.putExtra("pic", courseDetailList.get(position).getThumbnailPath());
                    startActivity(intent);
                }
            }
        });
        initData();
        courseList.add(null);
        subjectList.add(null);
        termList.add(null);
        getPlanTerm();
    }

    private void getPlanTerm() {
        Map map = new HashMap();
        VolleyManager.getInstance().getString(BaseDataUtils.SERVER + Constants.getPlanModifyList, map, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                downFail();
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
                        refreshList();
                    }else {
                        isEmpty();
                    }
                }
            }

        });

    }

    private void refreshList() {
        if (isFirst) {
            loadBar.setVisibility(View.VISIBLE);
        }
        HashMap<String,String> map = new HashMap();
        BaseApplication app = BaseApplication.getApplication();
        Login mlogin = app.getLogin();
        if (mlogin == null || mlogin.getData() == null || mlogin.getData().getUserId() == null) {
            return;
        }
        map.put("userId", mlogin.getData().getUserId());
        if (subject != null)
            map.put("subject_id", subject.getPk());
        if (term != null)
            map.put("term_id", term.getPk());
        if (course != null)
            map.put("classname_id", course.getPk());
        map.put("pageNum",String.valueOf(1) );
        map.put("pageSize", String.valueOf(200));
        if (planIndex >= mPlanIds.size()) {
            mListView.onRefreshComplete(true);
            return;
        }
        map.put("teach_plan_id", mPlanIds.get(planIndex));
        VolleyManager.getInstance().getString(Constants.GuidanceResource, map, TAG, new NetResult<String>() {

            @Override
            public void onFailure(VolleyError error) {
                mListView.onRefreshComplete(false);
                downFail();
            }

            @Override
            public void onSucceed(String response) {
                mListView.onRefreshComplete(true);
                Gson gson = new Gson();
                CourseDetailBean courseDetailBean = gson.fromJson(response, CourseDetailBean.class);
                if (courseDetailBean != null && courseDetailBean.getCode() == Constants.STATUS_SUCCESS) {
                    if (courseDetailBean != null && courseDetailBean.getData() != null && courseDetailBean.getData().size() > 0) {
                        courseDetailList.addAll(courseDetailBean.getData());
                        Collections.sort(courseDetailList);
                        adapter.notifyDataSetChanged();
                    }
                    if (courseDetailList.size() < 20&&planIndex < mPlanIds.size()-1) {
                        planIndex++;
                        refreshList();
                    } else {
                        isEmpty();
                        isFirst = false;
                    }
                }
            }
        });
    }

    private void downFail() {
        emptyView.setVisibility(View.VISIBLE);
        ((TextView)emptyView.getChildAt(0)).setText(R.string.player_tips_not_responding);
        isFirst = false;
        loadBar.setVisibility(View.GONE);
    }

    private void isEmpty() {
        loadBar.setVisibility(View.GONE);
        if (courseDetailList.size() == 0) {
            selLayout.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.VISIBLE);
            selLayout.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    protected void initData() {
        BaseDataUtils.getsSubjectList(new BaseDataUtils.ReturnData<Subject>() {
            @Override
            public void refreshData(List<Subject> list) {
                loadBar.setVisibility(View.GONE);
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

    /**
     * 根据期数id获取课程列表
     */
    private void getCourseList() {
        loadBar.setVisibility(View.VISIBLE);
        courseList.clear();
        if (courseWin != null && courseWin.getListView() != null) {
            ((BaseAdapter) courseWin.getListView().getAdapter()).notifyDataSetChanged();
        }
        HashMap courseMap = new HashMap();
        courseMap.put("type", "classname");
        courseMap.put("fk", term.getPk());
        VolleyManager.getInstance().getString(Constants.GuidanceCategory, courseMap, TAG, new NetResult<String>() {
            @Override
            public void onFailure(VolleyError error) {
                loadBar.setVisibility(View.GONE);
            }

            @Override
            public void onSucceed(String response) {
                loadBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                CourseDetailBean courseBean = gson.fromJson(response, CourseDetailBean.class);
                if (courseBean != null) {
                    switch (courseBean.getCode()) {
                        case Constants.STATUS_ARGUMENT_ERROR:
                            break;
                        case Constants.STATUS_DATA_NOTFOUND:
                            break;
                        case Constants.STATUS_ILLEGAL:
                            break;
                        case Constants.STATUS_SERVER_EXCEPTION:
                            break;
                        case Constants.STATUS_SUCCESS:
                            TeachBaseBean<Course> temp;
                            if (response != null && response.length() > 0) {
                                temp = new GeoUtil()
                                        .deserializer(response, new TypeToken<TeachBaseBean<Course>>() {
                                        }.getType());
                                List<Course> list = temp.getData();
                                if (list != null && list.size() > 0) {
                                    courseList.add(null);
                                    courseList.addAll(list);
                                    if (courseWin == null) {
                                        courseWin = getPopupWindow(courseLayout, courseList);
                                    } else {
                                        courseWin.show();
                                    }
                                } else {
                                    onDismiss();
                                }
                                if (courseWin != null && courseWin.getListView() != null) {
                                    ((BaseAdapter) courseWin.getListView().getAdapter()).notifyDataSetChanged();
                                }
                            }

                            break;
                        case Constants.STATUS_TIMEOUT:
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teach_sel_item1:
                if (term != null) {
                    term = null;
                    termWin.getListView().setSelection(0);
                    if (course != null) {
                        course = null;
                        courseWin.getListView().setSelection(0);
                    }
                }

                if (subWin == null) {
                    subWin = getPopupWindow(v, subjectList);
                } else {
                    subWin.show();
                }
                break;
            case R.id.teach_sel_item2:
                termList.clear();
                if (subject == null) {
                    return;
                }
                if (course != null) {
                    course = null;
                    courseWin.getListView().setSelection(0);
                }
                loadBar.setVisibility(View.VISIBLE);
                BaseDataUtils.getsTermList(new BaseDataUtils.ReturnData<Term>() {
                    @Override
                    public void refreshData(List<Term> list) {
                        loadBar.setVisibility(View.GONE);
                        if (list != null && list.size() > 0) {
                            termList.add(null);
                            termList.addAll(list);
                            if (termWin == null) {
                                termWin = getPopupWindow(termLayout, termList);
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
                break;
            case R.id.teach_sel_item3:
                if (term == null) {
                    return;
                }
                getCourseList();
                break;
            case R.id.head_back_action:
                finish();
                return;
            default:
                break;
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
        if (subWin != null && parent == subWin.getListView()) {
            if (term != null) {
                term = null;
                ((TeachPlanPopSelAd) termWin.getListView().getAdapter()).setCurPosition(0);
                if (course != null) {
                    course = null;
                    ((TeachPlanPopSelAd) courseWin.getListView().getAdapter()).setCurPosition(0);
                }
            }
            ((TeachPlanPopSelAd) subWin.getListView().getAdapter()).setCurPosition(position);
            subject = subjectList.get(position);
            subWin.dismiss();
        } else if (termWin != null && parent == termWin.getListView()) {
            if (course != null) {
                course = null;
                ((TeachPlanPopSelAd) courseWin.getListView().getAdapter()).setCurPosition(0);
            }
            ((TeachPlanPopSelAd) termWin.getListView().getAdapter()).setCurPosition(position);
            term = termList.get(position);
            termWin.dismiss();
        } else if (courseWin != null && parent == courseWin.getListView()) {
            ((TeachPlanPopSelAd) courseWin.getListView().getAdapter()).setCurPosition(position);
            course = courseList.get(position);
            courseWin.dismiss();
        }
        onRefresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selLayout.setVisibility(View.INVISIBLE);
        courseDetailList.clear();
        adapter.notifyDataSetChanged();
        planIndex = 0;
        refreshList();
    }

    @Override
    public void onDismiss() {
        mListView.setBackgroundColor(getResources().getColor(R.color.white));
        mListView.setFocusable(true);
    }

    @Override
    public void onRefresh() {
        courseDetailList.clear();
        adapter.notifyDataSetChanged();
        planIndex = 0;
        refreshList();
    }

    @Override
    public void onLoadMore() {
        planIndex++;
        refreshList();
    }
}
