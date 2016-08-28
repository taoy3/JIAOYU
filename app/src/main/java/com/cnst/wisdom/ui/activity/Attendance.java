package com.cnst.wisdom.ui.activity;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import com.cnst.wisdom.presenter.AttenPresenter;
import com.cnst.wisdom.ui.adapter.ViewHolderHelper;
import com.cnst.wisdom.ui.view.AttenView;
import com.cnst.wisdom.ui.widget.calendar.CalendarDialog;
import com.cnst.wisdom.utills.BaseDataUtils;
import com.cnst.wisdom.utills.DisplayUtils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 考勤管理
 *
 * @author jiangzuyun.
 * @since [产品/模版版本]
 */
public class Attendance extends BaseNetActivity implements AttenView<String>, View.OnClickListener, CalendarDialog.SelectorListener, AdapterView.OnItemClickListener
{

    private RadioButton     mRbtnToday;
    private TextView        mtv_sw_class;
    private TextView        mRbtnCalendar;
    private ListView        mAttendanceLst;
    private ProgressBar     mLoadingView;
    private BaseListAdapter mAttendapter;
    private AttenPresenter  mPresenter;
    private String TAG = "Attendance";
    private List<Clazz> mClassesLst = new ArrayList<Clazz>();
    private int TODAY    = 0;//使用具体日期
    private int YESTODAY = 1;//使用具体日期--月日0119

    private int CALENDAR = 2;//比较特殊2表示 选中的日期 不管是哪天
    /**
     * 当前 的数据类型
     */
    private int CURRDATA = TODAY;

    /**
     * 图片 应该使用的宽度
     */
    private int              mIgw;
    private SimpleDateFormat mDateFormat;
    private CalendarDialog   mCalendarDialog;
    private ImageView        mIv_toggle;
    private RotateAnimation down = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private RotateAnimation up   = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private boolean         popShow;
    private ListPopupWindow mListPopup;

    {
        up.setFillAfter(true);
        up.setDuration(300);
        down.setFillAfter(true);
        down.setDuration(300);
    }

    private void assignViews()
    {
        mtv_sw_class = (TextView)findViewById(R.id.tv_sw_class);
        mIv_toggle = (ImageView)findViewById(R.id.iv_toggle);
        mRbtnCalendar = (TextView)findViewById(R.id.tv_chose_time);
        mAttendanceLst = (ListView)findViewById(R.id.vp_attendance);
        mLoadingView = (ProgressBar)findViewById(R.id.pb_loading);
    }

    public void init()
    {
        mPresenter = new AttenPresenter(this, this);
        setContentView(R.layout.activity_attendance);
        assignViews();
        ( (TextView)findViewById(R.id.head_text) ).setText(BaseApplication.findString(R.string.atten));

        mDateFormat = new SimpleDateFormat("MM月dd日");

        String today = mDateFormat.format(new Date());
        mRbtnCalendar.setText("("+today+")");

        //没有数据再拿网络数据
        //        mPresenter.getData(new Date(), TODAY);
        //add by yhw-->根据班级和时间来查询考勤,班级classId获取根据TeacherId
        BaseDataUtils.getClazzList(new BaseDataUtils.ReturnData<Clazz>()
        {
            @Override
            public void refreshData(List<Clazz> list)
            {
                if(list == null)
                {
                    Log.i(TAG, "classList is null");

                    showLoadingView(false);
                    Toast.makeText(Attendance.this, "您还没有教班级", Toast.LENGTH_SHORT).show();
                    return;
                }
                mClassesLst = list;
                //                Log.i(TAG,"classesLst is:  "+mClassesLst.size());
                //                Log.i(TAG,"classesLst is:  "+mClassesLst.get(0).getId());//班级classId
                //                Log.i(TAG,"classesLst is:  "+mClassesLst.get(0).getName());//班级Name
                mPresenter.getData(mClassesLst.get(0).getId(), "2016-03-0317:20:26", "Attendance");
                initPopwin();
            }
        });
//        mPresenter.getData("全部","2016-02-24 15:43:53","Attendance");

        if("phone".equals(BaseApplication.findString(R.string.screen_type)))
        {
            mIgw = ( BaseApplication.getScreenwidth()/4-DisplayUtils.dip2px(20, this) );
        }else
        {
            mIgw = ( BaseApplication.getScreenwidth()/6-DisplayUtils.dip2px(20, this) );
        }
    }

    @Override
    protected void setLayout() {
        init();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }


    @Override
    public void showSucceedView(boolean show)
    {
        mAttendapter = new BaseListAdapter<Recommend>(this, R.layout.item_list_recommend)
         {
            @Override
            public void convert(ViewHolderHelper helper, Recommend item)
            {
                if(item == null){
                    Toast.makeText(Attendance.this, "您还没关联学生信息", Toast.LENGTH_SHORT).show();
//                    View recRl = Attendance.this.findViewById(R.id.recommend_rl);
                    View recRl = getLayoutInflater().inflate(R.layout.item_list_recommend,null,false);
                    recRl.setVisibility(View.GONE);
                    return;
                }
                helper.setText(R.id.tv_list_title, item.title);
//                helper.setClickListener(R.id.tv_list_rec_more, new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                    }
//                });
                GridView gridView = helper.findViewById(R.id.ingv_recommend);
//                if("phone".equals(BaseApplication.findString(R.string.screen_type)))
//                {
//                    gridView.setNumColumns(2);
//                }else
//                {
//                    gridView.setNumColumns(3);
//                }
                //convertView 刷新数据
                final List<StudentBean.DataInfo> lstData = new ArrayList<StudentBean.DataInfo>();
                if(item.title.equals("出勤")){
                    for(int i = 0;i<item.attendanceData.getData().size();i++){
                        if("0".equals(item.attendanceData.getData().get(i).getDynAttributes().getAttendanceInfo())){
                            lstData.add(item.attendanceData.getData().get(i));//此时集合中只有出勤的学生数据
//                          lstData.remove(item.attendanceData.getDatsssssa().get(i));
                            Log.i(TAG, "lstData(0)--->"+lstData.size());
                        }
                    }
                }else if(item.title.equals("缺勤")){
                    for(int i = 0;i<item.attendanceData.getData().size();i++){
                        if("-1".equals(item.attendanceData.getData().get(i).getDynAttributes().getAttendanceInfo())){
                            lstData.add(item.attendanceData.getData().get(i));
//                            lstData.remove(item.attendanceData.getData().get(i));//此时集合中只有缺勤的学生数据
                            Log.i(TAG, "lstData(-1)-->"+lstData.size());
                        }
                    }

                }else if(item.title.equals("迟到")){
                    for(int i = 0;i<item.attendanceData.getData().size();i++){
                        if("1".equals(item.attendanceData.getData().get(i).getDynAttributes().getAttendanceInfo())){
                            lstData.add(item.attendanceData.getData().get(i));
//                            lstData.remove(item.attendanceData.getData().get(i));//此时集合中只有迟到的学生数据
                            Log.i(TAG, "lstData(1)-->"+lstData.size());
                        }
                    }

                }else if(item.title.equals("请假")){
                    for(int i = 0;i<item.attendanceData.getData().size();i++){
                        if("2".equals(item.attendanceData.getData().get(i).getDynAttributes().getAttendanceInfo())){
                            lstData.add(item.attendanceData.getData().get(i));
//                            lstData.remove(item.attendanceData.getData().get(i));//此时集合中只有请假的学生数据
                            Log.i(TAG, "lstData(2)-->"+lstData.size());
                        }
                    }
                }
                String recMore = "("+String.valueOf(lstData.size())+"/"+String.valueOf(item.attendanceData.getData().size())+")";
                helper.setText(R.id.tv_list_rec_more,recMore);


                helper.setAdapter(R.id.ingv_recommend,
                        new BaseListAdapter<StudentBean.DataInfo>(BaseApplication.getContext(), lstData,
                                R.layout.item_gridview_atten)
                        {

//                            @Override
//                            public void refreshData(List<AttendanceBean.DataInfo> data)
//                            {
//                                super.refreshData(data);
//                            }

                            public void convert(ViewHolderHelper helper, final StudentBean.DataInfo item)
                            {
//                                helper.setText(R.id.tv_item_grid_atten, item.getAbs());
//                                helper.setCircleImageFromUrl(R.id.im_item_grid_recommend, mIgw, item.getImage_url());
//                                  helper.setText(R.id.tv_item_grid_atten,item.getName());
                                  if(item.getDynAttributes().getAttendanceInfo().equals("0")){
                                      helper.setText(R.id.imv_tv_item_grid_atten,"出勤");
                                      helper.setText(R.id.tv_item_grid_atten, item.getName());

                                  }else if(item.getDynAttributes().getAttendanceInfo().equals("-1")){
                                      helper.setText(R.id.imv_tv_item_grid_atten,"缺勤");
                                      helper.setText(R.id.tv_item_grid_atten, item.getName());

                                  }else if(item.getDynAttributes().getAttendanceInfo().equals("1")){
                                      helper.setText(R.id.imv_tv_item_grid_atten,"迟到");
                                      helper.setText(R.id.tv_item_grid_atten, item.getName());

                                  }else if(item.getDynAttributes().getAttendanceInfo().equals("2")){
                                      helper.setText(R.id.imv_tv_item_grid_atten,"请假");
                                      helper.setText(R.id.tv_item_grid_atten, item.getName());
                                  }
                                helper.setBackground(R.id.im_item_grid_recommend, R.drawable.applogo);
                                helper.setClickListener(R.id.tv_item_grid_atten, new View.OnClickListener()
                                {

                                    @Override
                                    public void onClick(View v)
                                    {
                                    }
                                });
                            }

                        });
            }
        };

        mAttendanceLst.setAdapter(mAttendapter);
    }

    @Override
    public void showEmptyView(boolean show)
    {

    }

    @Override
    public void showErrorView(boolean show)
    {
    }

    @Override
    public void showLoadingView(boolean show)
    {
        mLoadingView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void refreshData(String response)
    {
//        bdmeizhi bdmeizhi = null;
//        bdmeizhi = gson.fromJson((String)response, com.cnst.wisdom.model.bean.bdmeizhi.class);
//        ArrayList<Recommend> mRec_data = new ArrayList<Recommend>();
//        List<String> url_maps = new ArrayList<>();
//        for(int i = 0; i<bdmeizhi.getData().size()-1; i++)
//        {
//            //            List<bdmeizhi.DataEntity> data = bdmeizhi.getData();
//            //            bdmeizhi.setData(data);
//            mRec_data.add(new Recommend("今日热点："+i, bdmeizhi));
//            url_maps.add(bdmeizhi.getData().get(i).getImage_url());
//        }
//        mAttendapter.refreshData(mRec_data);
//        add by yhw
        Gson gson = new Gson();
        StudentBean stuBean = gson.fromJson((String)response,StudentBean.class);
        //增加对网络返回结果的判断
        if(stuBean.getCode()== Constants.STATUS_SUCCESS){
            ArrayList<Recommend> recData = new ArrayList<Recommend>();
            List<StudentBean.DataInfo> data = stuBean.getData();
            recData.add(0,new Recommend("出勤",stuBean));
            recData.add(1,new Recommend("迟到",stuBean));
            recData.add(2,new Recommend("缺勤",stuBean));
            recData.add(3,new Recommend("请假",stuBean));
            mAttendapter.refreshData(recData);
        }else if(stuBean.getCode() == Constants.STATUS_FAIL)    //data == null code =-1
        {
            mAttendapter.refreshData(null);
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mPresenter.cancelNet(CURRDATA);
    }

    /**
     * 弹出日历对话框
     */
    private void showDateView()
    {
        if(mCalendarDialog == null)
        {
            mCalendarDialog = new CalendarDialog(this, this);
        }
        mCalendarDialog.show();
    }

    private void getData(int cuday)
    {
        mPresenter.cancelNet(CURRDATA);
        CURRDATA = cuday;
        //今天的数据 有获取过 则不再获取
//        mPresenter.getData("全部", "",cuday);
        mPresenter.getData(mClassesLst.get(0).getId(),"cuday","Attendance");
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.head_back_action:
                finish();
                Toast.makeText(this,"链接搜素界面",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onSelector(String date)
    {
        mRbtnCalendar.setText("("+date.substring(date.indexOf("-")+1).replace("-", "月")+")");

        getData(CALENDAR);
    }

    public void choseDate(View v)
    {
        showDateView();
    }

    public void classChose(View v)
    {
        toggleAni();
        togglePop();
    }

    private void toggleAni()
    {
        mIv_toggle.startAnimation(popShow ? up : down);
        popShow = !popShow;
    }

    private void togglePop()
    {
        if(mListPopup.isShowing())
        {
            mListPopup.dismiss();
        }else
        {
            mListPopup.show();
        }

    }

    private void initPopwin()
    {
        mListPopup = new ListPopupWindow(this);
        String[] classLst = new String[mClassesLst.size()];
        for(int i= 0;i<mClassesLst.size();i++){
            classLst[i] = mClassesLst.get(i).getName();
        }
        mListPopup.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classLst));
        mListPopup.setModal(true);
        mListPopup.setWidth(AbsListView.LayoutParams.WRAP_CONTENT);
        mListPopup.setHeight(AbsListView.LayoutParams.WRAP_CONTENT);
        mListPopup.setAnchorView(findViewById(R.id.rl_pop));
        mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                toggleAni();
            }
        });
        mListPopup.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        mListPopup.dismiss();
        screenOutClass();
        mtv_sw_class.setText("("+mClassesLst.get(position).getName()+")");
    }

    private void screenOutClass()
    {
        //TODO
    }
}
