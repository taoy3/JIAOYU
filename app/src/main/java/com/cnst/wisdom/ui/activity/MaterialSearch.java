package com.cnst.wisdom.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cnst.wisdom.BaseApplication;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.Constants;
import com.cnst.wisdom.model.bean.CourseDetail;
import com.cnst.wisdom.model.bean.CourseDetailBean;
import com.cnst.wisdom.model.bean.Login;
import com.cnst.wisdom.model.bean.MaterialPagerBean;
import com.cnst.wisdom.model.bean.OnlinePagerBean;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.model.net.VolleyManager;
import com.cnst.wisdom.ui.adapter.GuidanceAdapter;
import com.cnst.wisdom.ui.materialpager.SearchMateralPager;
import com.cnst.wisdom.ui.materialpager.SearchOnlinePager;
import com.cnst.wisdom.ui.widget.CustomSearchView;
import com.cnst.wisdom.ui.widget.ScrollListView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MaterialSearch extends BaseNetActivity implements View.OnClickListener, TextView.OnEditorActionListener, AdapterView.OnItemClickListener, CustomSearchView.TextIsEmptyListener {

    private ArrayList<CourseDetail> courseDetailList = new ArrayList<>();
    private GuidanceAdapter guidanceAdapter;
    private CustomSearchView searchView;
    private LinearLayout typeLayout;
    private View spaceView1;
    private View spaceView2;
    private View guidanceView;
    private LinearLayout materialView;
    private VolleyManager volleyManager = VolleyManager.getInstance();
    private SearchMateralPager materialPager;
    private final int GUIDANCE = 0;//课前指导
    private final int MATERIAL = 1;//资源素材
    private final int ONLINELEARN = 2;//在线进修
    private final int ALL = -1;
    private Gson gson = new Gson();
    private MaterialPagerBean materialPagerBean;
    private LinearLayout onLineLearnView;
    private SearchOnlinePager onLineLearnPager;
    private OnlinePagerBean onLineLearnBean;
    private FrameLayout emptyTip;
    private ScrollView scrollView;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_guidance_search);
    }

    @Override
    protected void initData() {

    }

    protected void initView() {
        searchView = (CustomSearchView) findViewById(R.id.search_view);
        typeLayout = (LinearLayout) findViewById(R.id.type_layout);
        scrollView = (ScrollView) findViewById(R.id.scroll_search);
        emptyTip = (FrameLayout) findViewById(R.id.data_empty);

        guidanceView = findViewById(R.id.guidance_layout);
        ScrollListView guidanceLv = (ScrollListView) findViewById(R.id.lv_guidance);
        guidanceLv.setOnItemClickListener(this);
        guidanceAdapter = new GuidanceAdapter(this, courseDetailList);
        guidanceLv.setAdapter(guidanceAdapter);

        spaceView1 = findViewById(R.id.space_1);

        materialView = (LinearLayout) findViewById(R.id.material_layout);
        materialPager = new SearchMateralPager(this, "all");
        materialView.addView(materialPager.mRootView);

        spaceView2 = findViewById(R.id.space_2);

        onLineLearnView = (LinearLayout) findViewById(R.id.onlinelearn_layout);
        onLineLearnPager = new SearchOnlinePager(this, "all");
        onLineLearnView.addView(onLineLearnPager.mRootView);

        searchView.setSearchListener(this);
        searchView.setEmptyListener(this);
        searchView.setExitListener(new CustomSearchView.ExitListener() {
            @Override
            public void exitSearch() {
                exitWithAnim();
            }
        });
        findViewById(R.id.ibtn_contacts).setOnClickListener(this);
        findViewById(R.id.ibtn_guidance).setOnClickListener(this);
        findViewById(R.id.ibtn_material).setOnClickListener(this);
        findViewById(R.id.ibtn_onlinelearn).setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitWithAnim();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitWithAnim() {
        finish();
//        overridePendingTransition();
    }

    @Override
    public void onClick(View v) {
        if(searchView.getText().length()==0){
            return;
        }
        switch (v.getId()) {
            case R.id.ibtn_contacts:
                break;
            case R.id.ibtn_guidance:
                search(searchView.getText(), GUIDANCE);
                break;
            case R.id.ibtn_material:
                search(searchView.getText(), MATERIAL);
                break;
            case R.id.ibtn_onlinelearn:
                search(searchView.getText(), ONLINELEARN);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        search(searchView.getText(), ALL);
        return true;
    }

    private void search(String key, final int type) {
        materialPager.setSearchText(searchView.getText());
        onLineLearnPager.setSearchText(searchView.getText());
        guidanceAdapter.setSearchText(searchView.getText());
        courseDetailList.clear();
        materialPagerBean=new MaterialPagerBean();
        materialPagerBean.data = new ArrayList<>();

        onLineLearnBean=new OnlinePagerBean();
        onLineLearnBean.setData(new ArrayList<OnlinePagerBean.OnlinePager>());
        HashMap<String,String> map = new HashMap();
        if (key == null) {
            return;
        }
        BaseApplication app=BaseApplication.getApplication();
        Login mlogin=app.getLogin();
        if(mlogin==null||mlogin.getData()==null||mlogin.getData().getUserId()==null){
            return;
        }
        map.put("userId",mlogin.getData().getUserId());
        try {
            String strUTF8 = URLDecoder.decode(key, "UTF-8");
            map.put("searchKeyword", strUTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (type != -1) {
            map.put("type", type+"");
        }

        map.put("pageNum", String.valueOf(1));
        map.put("pageSize", String.valueOf(10));
        volleyManager.postString(Constants.SERVER + Constants.QUERYRESOURCEMATERIALLIKE, map,
                "Material", new NetResult<String>() {
                    @Override
                    public void onFailure(VolleyError error) {
                    }

                    @Override
                    public void onSucceed(String response) {
                        switch (type) {
                            case GUIDANCE:
                                refreshGuidance(response);
                                break;
                            case MATERIAL:
                                refreshMaterial(response);
                                break;
                            case ONLINELEARN:
                                refreshOnLine(response);
                                break;
                            case ALL:
                                refreshAll(response);
                                break;
                            default:
                                break;
                        }
                        materialPager.setMaterialPagerBean(materialPagerBean);
                        materialPager.initData();

                        onLineLearnPager.setmOnlinepagerDatas(onLineLearnBean.getData());
                        onLineLearnPager.initData();

                        guidanceAdapter.notifyDataSetChanged();
                        searchComplete();
                    }
                });
    }



    private void refreshAll(String response) {
        try {
            JSONObject object = new JSONObject(response);
            if(object.getString("code").equals("200")){
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    if(item.getInt("type")==0){
                        CourseDetail courseDetailBean = gson.fromJson(item.toString(), CourseDetail.class);
                        courseDetailList.add(courseDetailBean);
                    }else if(item.getInt("type")==1){
                            MaterialPagerBean.MaterialPager materialPager = new Gson().fromJson(item.toString(), MaterialPagerBean.MaterialPager.class);
                            materialPagerBean.data.add(materialPager);
                    }else if(item.getInt("type")==2){
                            OnlinePagerBean.OnlinePager onlinePager = new Gson().fromJson(item.toString(), OnlinePagerBean.OnlinePager.class);
                            onLineLearnBean.getData().add(onlinePager);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void refreshGuidance(String response) {
        CourseDetailBean courseDetailBean = gson.fromJson(response, CourseDetailBean.class);
        if (courseDetailBean != null && courseDetailBean.getCode() == Constants.STATUS_SUCCESS
                && courseDetailBean.getData() != null) {
            courseDetailList.addAll(courseDetailBean.getData());
        }
    }

    private void refreshMaterial(String response) {
        materialPagerBean = gson.fromJson(response, MaterialPagerBean.class);
    }
    private void refreshOnLine(String response) {
        onLineLearnBean = gson.fromJson(response, OnlinePagerBean.class);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //课前指导被点击
        if(parent.getId()==R.id.lv_guidance){
            CourseDetail courseDetail = courseDetailList.get(position);
            if(courseDetail.isVideo()){
                Intent intent = new Intent(this,GuidanceDetailActivity.class);
                intent.putExtra("vid", courseDetail.getVideoId());
                intent.putExtra("course",courseDetail);
                startActivity(intent);
            }
        }
    }

    @Override
    public void textEmpty() {
        typeLayout.setVisibility(View.VISIBLE);
        emptyTip.setVisibility(View.GONE);
        guidanceView.setVisibility(View.GONE);
        materialView.setVisibility(View.GONE);
        onLineLearnView.setVisibility(View.GONE);
        spaceView1.setVisibility(View.GONE);
        spaceView2.setVisibility(View.GONE);
    }
    private void searchComplete(){
        typeLayout.setVisibility(View.GONE);
        emptyTip.setVisibility(View.GONE);
        if(courseDetailList.size()>0){
            guidanceView.setVisibility(View.VISIBLE);
        }
        if(materialPagerBean.data.size()>0){
            materialView.setVisibility(View.VISIBLE);
        }
        if(onLineLearnBean.getData().size()>0){
            onLineLearnView.setVisibility(View.VISIBLE);
        }
        if(courseDetailList.size()>0&&materialPagerBean.data.size()>0){
            spaceView1.setVisibility(View.VISIBLE);
        }
        if(onLineLearnBean.getData().size()>0&&materialPagerBean.data.size()>0){
            spaceView2.setVisibility(View.VISIBLE);
        }
        if(courseDetailList.size()==0&&materialPagerBean.data.size()==0&&onLineLearnBean.getData().size()==0){
            emptyTip.setVisibility(View.VISIBLE);
        }
    }
}
