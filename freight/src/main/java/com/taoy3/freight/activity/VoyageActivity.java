package com.taoy3.freight.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.AdPagerAdapter;
import com.taoy3.freight.bean.Company;
import com.taoy3.freight.bean.Port;
import com.taoy3.freight.db.CompanyDB;
import com.taoy3.freight.db.PortDB;
import com.taoy3.freight.util.PagerRadioUtils;
import com.taoy3.freight.view.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VoyageActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private TextView startPortTv;
    private TextView destPortTv;
    private TextView companyNameTv;
    private AutoScrollViewPager viewPager;
    private PortDB dbAccess = PortDB.getInstance();
    private CompanyDB companyDBAccess = CompanyDB.getInstance();
    private AdPagerAdapter adPagerAdapter;
    private ArrayList<String> urls = new ArrayList<>();
    private Company company;
    private Port startPort;
    private Port destPort;
    private ListView historyLv;
    private ArrayAdapter adapter;
    private List<String> histories = new ArrayList<>();

    @Override
    protected void setView() {
        setContentView(R.layout.activity_voyage);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        startPortTv = (TextView) findViewById(R.id.start_port);
        destPortTv = (TextView) findViewById(R.id.dest_port);
        companyNameTv = (TextView) findViewById(R.id.ship_company);
        viewPager = (AutoScrollViewPager) findViewById(R.id.ad);
        urls.add("fj1.jpg");
        urls.add("fj2.jpg");
        urls.add("mv1.jpg");
        adPagerAdapter = new AdPagerAdapter(urls, getLayoutInflater());
        viewPager.setAdapter(adPagerAdapter);
        viewPager.startAutoScroll(1000);
        new PagerRadioUtils(viewPager, (RadioGroup) findViewById(R.id.group)).setChangeListener();
        findViewById(R.id.start_port).setOnClickListener(this);
        findViewById(R.id.dest_port).setOnClickListener(this);
        findViewById(R.id.ship_company).setOnClickListener(this);
        findViewById(R.id.search_voyage).setOnClickListener(this);
        historyLv = (ListView) findViewById(R.id.search_history_list);
    }

    @Override
    protected void initData() {
        getHistoriesData();
        adapter = new ArrayAdapter(this, R.layout.adapter_history, R.id.text, histories);
        historyLv.setAdapter(adapter);
        historyLv.setOnItemClickListener(this);
    }

    private void getHistoriesData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SEARCHVOYAGEHISTORY, Context.MODE_PRIVATE);
        Set<String> set = sharedPreferences.getStringSet(SEARCHVOYAGEHISTORY, null);
        if (set != null && histories.size() == 0) {
            histories.addAll(set);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        startPortTv.setText(startPort);
//        destPortTv.setText(destPort);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_port:
                startActivityForResult(new Intent(this, PortActivity.class), STARTPORTCODE);
                break;
            case R.id.dest_port:
                startActivityForResult(new Intent(this, PortActivity.class), DESTPORTCODE);
                break;
            case R.id.ship_company:
                startActivityForResult(new Intent(this, SearchScActivity.class), SHIPCOMPANYCODE);
                break;
            case R.id.search_voyage:
                if (startPort.getId()>0&&destPort.getId()>0&&company.getId()>0) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(dbAccess.queryName(startPort.getId())+"-")
                    .append(dbAccess.queryName(destPort.getId())+"-")
                    .append(companyDBAccess.queryName(company.getId()));
                    if (!histories.contains(buffer.toString())) {
                        if (histories.size() > 2) {
                            histories.remove(0);
                        }
                        histories.add(buffer.toString());
                        adapter.notifyDataSetChanged();
                    }
                    startQuery();
                } else {
                    Toast.makeText(this, R.string.please_enter_the_port_information, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int id = data.getIntExtra(ID, 0);
        if (data != null) {
            switch (requestCode) {
                case STARTPORTCODE:
                    startPort = dbAccess.query(id);
                    startPortTv.setText(startPort.getName_zh());
                    break;
                case DESTPORTCODE:
                    destPort = dbAccess.query(id);
                    destPortTv.setText(destPort.getName_zh());
                    break;
                case SHIPCOMPANYCODE:
                    company = companyDBAccess.query(id);
                    companyNameTv.setText(company.getName_zh());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SharedPreferences sharedPreferences = getSharedPreferences(SEARCHVOYAGEHISTORY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        HashSet<String> set = new HashSet<>();
        set.addAll(histories);
        editor.putStringSet(SEARCHVOYAGEHISTORY, set);
        editor.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String[] strings = histories.get(i).split("-");
        company.setId(companyDBAccess.queryIdByName(strings[0]));
        startPort.setId(dbAccess.queryIdByName(strings[1]));
        destPort.setId(dbAccess.queryIdByName(strings[2]));
        startQuery();
    }

    private void startQuery() {
        Intent intent = new Intent(this, VoyageListActivity.class);
        intent.putExtra(SCID, company.getId());
        intent.putExtra(STARTPORT, startPort.getId());
        intent.putExtra(DESTPORT, destPort.getId());
        startActivityForResult(intent, LOCKCODE);
    }
}
