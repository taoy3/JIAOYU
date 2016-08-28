package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseListAdapter;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.Clazz;
import com.cnst.wisdom.model.bean.Recommend;
import com.cnst.wisdom.model.bean.StudentBean;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.presenter.HealthPresenter;
import com.cnst.wisdom.ui.adapter.ViewHolderHelper;
import com.cnst.wisdom.ui.view.AttenView;
import com.cnst.wisdom.ui.widget.InnerGridView;
import com.cnst.wisdom.ui.widget.calendar.CalendarDialog;
import com.cnst.wisdom.utills.BaseDataUtils;
import com.cnst.wisdom.utills.DisplayUtils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 健康管理
 *
 * @author jiangzuyun
 * @see
 */
public class HealthManage extends BaseNetActivity implements AttenView<String>, View.OnClickListener, CalendarDialog.SelectorListener {
    public static final String STUDENTENTRY = "studentEntry";
    private ListView mAttendance;
    private ProgressBar mLoadingView;
    private BaseListAdapter mAttendapter;
    private HealthPresenter mPresenter;
    private static String TAG = "HealthManage";
    private List<Clazz> mClassesLst = new ArrayList<Clazz>();
    //data 分别存放2种数据 TODAY  YESTODAY
    private SparseArray sdata = new SparseArray(2);
    //    WeakReference<SparseArray> weakSdata = new WeakReference<SparseArray>(sdata);

    private int TODAY = 0;//使用具体日期
    private int YESTODAY = 1;//使用具体日期--月日0119

    private int CALENDAR = 2;//比较特殊2表示 选中的日期 不管是哪天
    /**
     * 当前 的数据类型
     */

    private int CURRDATA = TODAY;

    /**
     * 图片 应该使用的宽度
     */
    private int mIgw;
    private SimpleDateFormat mDateFormat;
    private SimpleDateFormat mCacheDateFormat;
    private CalendarDialog mCalendarDialog;

    private RelativeLayout mHealthRl;
    private ImageView mArror;
    private boolean mArrorFlag = false;

    private RotateAnimation down = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private RotateAnimation up = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    private ListPopupWindow mListPopup;
    private RelativeLayout mHealthDate;
    private TextView mHealthClasses;
    //    private PopupWindow     PopupWindow;

    private TextView mTvDate;
    private StudentBean mStuBean = null;
    private List<StudentBean.DataInfo> mLstData1 = new ArrayList<StudentBean.DataInfo>();
    private List<StudentBean.DataInfo> mLstData2 = new ArrayList<StudentBean.DataInfo>();
    private List<StudentBean.DataInfo> mLstData3 = new ArrayList<StudentBean.DataInfo>();

    @Override
    protected void setLayout() {
        mPresenter = new HealthPresenter(this, this);
        setContentView(R.layout.activity_health_manage);

        initView();
        ((TextView) findViewById(R.id.head_text)).setText(BaseApplication.findString(R.string.health));

        mIgw = (BaseApplication.getScreenwidth() / 4 - DisplayUtils.dip2px(20, this));
        mDateFormat = new SimpleDateFormat("MM月dd日");
        mCacheDateFormat = new SimpleDateFormat("MMdd");

        String today = mDateFormat.format(new Date());
        mTvDate.setText("(" + today + ")");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        //初始化 今天 昨天
        YESTODAY = Integer.valueOf(mCacheDateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DATE, +1);
        CURRDATA = TODAY = Integer.valueOf(mCacheDateFormat.format(calendar.getTime()));

        //TODO 从数据库获取数据 初始化到 sdata中  判断数据库中的缓存是否过期，过期就清除
        //没有数据再拿网络数据
        //在查询之前先获取教师关联的学生班级
        BaseDataUtils.getClazzList(new BaseDataUtils.ReturnData<Clazz>() {
            @Override
            public void refreshData(List<Clazz> list) {
                if (list == null) {
                    Log.i(TAG, "classList is null");

                    showLoadingView(false);
                    Toast.makeText(HealthManage.this, "您还没有教班级", Toast.LENGTH_SHORT).show();
                    return;
                }
                mClassesLst = list;
                mPresenter.getData(mClassesLst.get(0).getId(), "2016-03-0317:20:26", "HealthManage");
            }
        });
    }

    @Override
    protected void initData() {

    }

    protected void initView() {
        mHealthRl = (RelativeLayout) findViewById(R.id.health_classes);
        mHealthRl.setOnClickListener(this);
        mArror = (ImageView) findViewById(R.id.iv_toggle);
        mLoadingView = (ProgressBar) findViewById(R.id.pb_loading);
        mAttendance = (ListView) findViewById(R.id.vp_attendance);
        mHealthDate = (RelativeLayout) findViewById(R.id.health_date);
        mHealthDate.setOnClickListener(this);
        mHealthClasses = (TextView) findViewById(R.id.health_classes_tv);

        mTvDate = (TextView) findViewById(R.id.tv_chose_time);
    }

    @Override
    public void showSucceedView(boolean show) {
        mAttendapter = new BaseListAdapter<Recommend>(this, R.layout.item_list_recommend) {
            @Override
            public void convert(ViewHolderHelper helper, final Recommend item) {
                if (item == null) {
                    Toast.makeText(HealthManage.this, "您还没关联学生信息", Toast.LENGTH_SHORT).show();
                    //                    View recRl = Attendance.this.findViewById(R.id.recommend_rl);
                    View recRl = getLayoutInflater().inflate(R.layout.item_list_recommend, null, false);
                    recRl.setVisibility(View.GONE);
                    return;
                }
                helper.setText(R.id.tv_list_title, item.title);
                helper.setClickListener(R.id.tv_list_rec_more, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

                InnerGridView gridView = helper.findViewById(R.id.ingv_recommend);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    //替换Button 为ImageView 有可能是 监听拦截
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(HealthManage.this, HealthDetailActivity.class);
                        if (item.title.equals("健康")) {
                            intent.putExtra(HealthManage.STUDENTENTRY, mLstData1.get(position));
                        } else if (item.title.equals("提示")) {
                            intent.putExtra(HealthManage.STUDENTENTRY, mLstData2.get(position));
                        } else if (item.title.equals("警示")) {
                            intent.putExtra(HealthManage.STUDENTENTRY, mLstData3.get(position));
                        }
                        startActivity(intent);
                    }
                });

                if (item.title.equals("健康")) {
                    for (int i = 0; i < item.attendanceData.getData().size(); i++) {
                        if (item.attendanceData.getData().get(i).getDynAttributes().getHealthInfo().equals("0")) {
                            mLstData1.add(item.attendanceData.getData().get(i));//此时集合中只有出勤的学生数据
                            //                          lstData.remove(item.attendanceData.getDatsssssa().get(i));
                            Log.i(TAG, "lstData(0)--->" + mLstData1.size());
                        }
                    }
                    String recMore = "(" + String.valueOf(mLstData1.size()) + "/" + String.valueOf(item.attendanceData.getData().size()) + ")";
                    helper.setText(R.id.tv_list_rec_more, recMore);
                    helper.setAdapter(R.id.ingv_recommend,
                            new BaseListAdapter<StudentBean.DataInfo>(BaseApplication.getContext(), mLstData1,
                                    R.layout.item_gridview_atten) {

                                @Override
                                public void convert(ViewHolderHelper helper, final StudentBean.DataInfo item) {
                                    if (item.getDynAttributes().getHealthInfo().equals("0")) {
                                        helper.setText(R.id.imv_tv_item_grid_atten, "健康");
                                        helper.setText(R.id.tv_item_grid_atten, item.getName());


                                    } else if (item.getDynAttributes().getHealthInfo().equals("2")) {
                                        helper.setText(R.id.imv_tv_item_grid_atten, "提示");
                                        helper.setText(R.id.tv_item_grid_atten, item.getName());

                                    } else if (item.getDynAttributes().getHealthInfo().equals("3")) {
                                        helper.setText(R.id.imv_tv_item_grid_atten, "警示");
                                        helper.setText(R.id.tv_item_grid_atten, item.getName());
                                    }

                                    helper.setBackground(R.id.im_item_grid_recommend, R.drawable.applogo);

                                }

                            });

                } else if (item.title.equals("提示")) {
                    for (int i = 0; i < item.attendanceData.getData().size(); i++) {
                        if (item.attendanceData.getData().get(i).getDynAttributes().getHealthInfo().equals("2")) {
                            mLstData2.add(item.attendanceData.getData().get(i));
                            Log.i(TAG, "lstData(2)----->" + mLstData2.size());
                        }
                    }
                    String recMore = "(" + String.valueOf(mLstData2.size()) + "/" + String.valueOf(item.attendanceData.getData().size()) + ")";
                    helper.setText(R.id.tv_list_rec_more, recMore);
                    helper.setAdapter(R.id.ingv_recommend,
                            new BaseListAdapter<StudentBean.DataInfo>(BaseApplication.getContext(), mLstData2,
                                    R.layout.item_gridview_atten) {

                                @Override
                                public void convert(ViewHolderHelper helper, final StudentBean.DataInfo item) {
                                    if (item.getDynAttributes().getHealthInfo().equals("0")) {
                                        helper.setText(R.id.imv_tv_item_grid_atten, "健康");
                                        helper.setText(R.id.tv_item_grid_atten, item.getName());


                                    } else if (item.getDynAttributes().getHealthInfo().equals("2")) {
                                        helper.setText(R.id.imv_tv_item_grid_atten, "提示");
                                        helper.setText(R.id.tv_item_grid_atten, item.getName());

                                    } else if (item.getDynAttributes().getHealthInfo().equals("3")) {
                                        helper.setText(R.id.imv_tv_item_grid_atten, "警示");
                                        helper.setText(R.id.tv_item_grid_atten, item.getName());
                                    }

                                    helper.setBackground(R.id.im_item_grid_recommend, R.drawable.applogo);

                                }

                            });

                } else if (item.title.equals("警示")) {
                    for (int i = 0; i < item.attendanceData.getData().size(); i++) {
                        if (item.attendanceData.getData().get(i).getDynAttributes().getHealthInfo().equals("3")) {
                            mLstData3.add(item.attendanceData.getData().get(i));
                            Log.i(TAG, "lstData(3)--------->" + mLstData3.size());
                        }
                    }
                    String recMore = "(" + String.valueOf(mLstData3.size()) + "/" + String.valueOf(item.attendanceData.getData().size()) + ")";
                    helper.setText(R.id.tv_list_rec_more, recMore);
                    helper.setAdapter(R.id.ingv_recommend,
                            new BaseListAdapter<StudentBean.DataInfo>(BaseApplication.getContext(), mLstData3, R.layout.item_gridview_atten) {

                                @Override
                                public void convert(ViewHolderHelper helper, final StudentBean.DataInfo item) {
                                    if (item.getDynAttributes().getHealthInfo().equals("0")) {
                                        helper.setText(R.id.imv_tv_item_grid_atten, "健康");
                                        helper.setText(R.id.tv_item_grid_atten, item.getName());


                                    } else if (item.getDynAttributes().getHealthInfo().equals("2")) {
                                        helper.setText(R.id.imv_tv_item_grid_atten, "提示");
                                        helper.setText(R.id.tv_item_grid_atten, item.getName());

                                    } else if (item.getDynAttributes().getHealthInfo().equals("3")) {
                                        helper.setText(R.id.imv_tv_item_grid_atten, "警示");
                                        helper.setText(R.id.tv_item_grid_atten, item.getName());
                                    }

                                    helper.setBackground(R.id.im_item_grid_recommend, R.drawable.applogo);

                                }

                            });
                }

            }
        };

        mAttendance.setAdapter(mAttendapter);
    }

    @Override
    public void showEmptyView(boolean show) {

    }

    @Override
    public void showErrorView(boolean show) {
    }

    @Override
    public void showLoadingView(boolean show) {
        mLoadingView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void refreshData(String response) {

        Gson gson = new Gson();
        mStuBean = gson.fromJson((String) response, StudentBean.class);
        if (mStuBean.getCode() == Constants.STATUS_SUCCESS) {
            ArrayList<Recommend> recData = new ArrayList<Recommend>();
            List<StudentBean.DataInfo> data = mStuBean.getData();
            recData.add(0, new Recommend("健康", mStuBean));
            recData.add(1, new Recommend("提示", mStuBean));
            recData.add(2, new Recommend("警示", mStuBean));
            mAttendapter.refreshData(recData);
        } else if (mStuBean.getCode() == Constants.STATUS_FAIL)    //data == null code =-1
        {
            mAttendapter.refreshData(null);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyManager.getInstance().cancel(CURRDATA);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.health_classes:
                showClasses();
                break;

            case R.id.health_date:
                showDateView();
                break;
        }
    }

    private void showDateView() {
        if (mCalendarDialog == null) {
            mCalendarDialog = new CalendarDialog(this, this);
        }
        mCalendarDialog.show();
    }

    private void showClasses() {
        mArror.setAnimation(mArrorFlag ? up : down);
        mArrorFlag = !mArrorFlag;
        showClassesPop();
    }

    private void showClassesPop() {
        mListPopup = new ListPopupWindow(this);
        String[] classLst = new String[mClassesLst.size()];
        for (int i = 0; i < mClassesLst.size(); i++) {
            classLst[i] = mClassesLst.get(i).getName();
        }
        mListPopup.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classLst));
        mListPopup.setModal(true);
        mListPopup.setWidth(AbsListView.LayoutParams.WRAP_CONTENT);
        mListPopup.setHeight(AbsListView.LayoutParams.WRAP_CONTENT);
        mListPopup.setAnchorView(findViewById(R.id.health_classes));
        mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mArror.setAnimation(mArrorFlag ? up : down);
                mArrorFlag = !mArrorFlag;
            }
        });
        mListPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListPopup.dismiss();
                mHealthClasses.setText("(" + mClassesLst.get(position).getName() + ")");
            }
        });
        mListPopup.show();
    }

    @Override
    public void onSelector(String date) {
        mTvDate.setText("(" + date.substring(date.indexOf("-") + 1).replace("-", "月") + ")");
    }

}
