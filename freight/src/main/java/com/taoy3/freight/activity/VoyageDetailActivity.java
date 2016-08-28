package com.taoy3.freight.activity;

import android.graphics.Color;
import android.widget.ListView;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.BaseListAdapter;
import com.taoy3.freight.adapter.ViewHolderHelper;
import com.taoy3.freight.adapter.VoyageDetailAdapter;
import com.taoy3.freight.bean.PortsInfo;
import com.taoy3.freight.bean.Voyage;
import com.taoy3.freight.db.VoyageDB;
import com.taoy3.freight.util.DateUtils;


public class VoyageDetailActivity extends BaseActivity {
    private TextView companyNameTv;
    private TextView startPortZh;
    private TextView startPortTv;
    private TextView clsTv;
    private TextView etdTv;
    private TextView destPortTv;
    private TextView etaTv;
    private TextView ttTv;
    private ListView listView;
    private Voyage voyage;


    @Override
    protected void setView() {
        setContentView(R.layout.activity_voyage_detail);
    }

    @Override
    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
        listView = (ListView) findViewById(R.id.port_list);
        companyNameTv = (TextView) findViewById(R.id.company_name);
        startPortZh = (TextView) findViewById(R.id.start_port_zh);
        startPortTv = (TextView) findViewById(R.id.start_port);
        clsTv = (TextView) findViewById(R.id.cls);
        etdTv = (TextView) findViewById(R.id.etd);
        destPortTv = (TextView) findViewById(R.id.dest_port);
        etaTv = (TextView) findViewById(R.id.eta);
        ttTv = (TextView) findViewById(R.id.tt);
    }

    @Override
    protected void initData() {
        voyage = VoyageDB.getInstance().query(getIntent().getIntExtra(VOYAGE, 0));
        companyNameTv.setText(voyage.getSc_name());
        startPortZh.setText(voyage.getPorts().get(0).getPort_name());
        startPortTv.setText(voyage.getPorts().get(0).getPort_name());
        clsTv.setText("CLS:" + DateUtils.changeDateFormat(voyage.getPorts().get(0).getCls()));
        etdTv.setText("ETD:" + DateUtils.changeDateFormat(voyage.getPorts().get(0).getEtd()));
        destPortTv.setText(voyage.getPorts().get(voyage.getPorts().size() - 1).getPort_name());
        etaTv.setText("ETA:" + DateUtils.changeDateFormat(voyage.getPorts().get(0).getEta()));
        ttTv.setText("TT:" + (voyage.getPorts().get(voyage.getPorts().size()
                - 1).getTt() - voyage.getPorts().get(0).getTt()) + "天");
        listView.setAdapter(new VoyageDetailAdapter(this, voyage.getPorts(),
                voyage.getPorts().get(0).getPort_name(),
                voyage.getPorts().get(voyage.getPorts().size() - 1).getPort_name()));
        listView.setAdapter(new BaseListAdapter<PortsInfo>(this, voyage.getPorts(), R.layout.adapter_voyage_detail) {
            @Override
            protected void convert(ViewHolderHelper helper, PortsInfo item) {
                if (item.getPort_id() == voyage.getStart_id()) {
                    helper.setImageRes(R.id.icon, R.mipmap.start_port);
                    helper.setTextColor(R.id.port_name, Color.GREEN);
                    helper.setTextColor(R.id.eta, Color.GREEN);
                    helper.setTextColor(R.id.etd, Color.GREEN);
                    helper.setTextColor(R.id.tt, Color.GREEN);
                } else if (item.getPort_id() == voyage.getDest_id()) {
                    helper.setImageRes(R.id.icon, R.mipmap.dest_port);
                    helper.setTextColor(R.id.port_name, Color.RED);
                    helper.setTextColor(R.id.eta, Color.RED);
                    helper.setTextColor(R.id.etd, Color.RED);
                    helper.setTextColor(R.id.tt, Color.RED);
                } else {
                    helper.setImageRes(R.id.icon, R.mipmap.pass_port);
                    helper.setTextColor(R.id.port_name, Color.GRAY);
                    helper.setTextColor(R.id.eta, Color.GRAY);
                    helper.setTextColor(R.id.etd, Color.GRAY);
                    helper.setTextColor(R.id.tt, Color.GRAY);
                }
                helper.setText(R.id.port_name, item.getPort_name());
                helper.setText(R.id.eta, "ETA:" + DateUtils.getMD(item.getEta(),0));
                if (item.getEtd() == 0) {
                    helper.setText(R.id.etd, "ETD:" + DateUtils.getMD(item.getEta(), item.getTt()));
                } else {
                    helper.setText(R.id.etd, "ETD:" + DateUtils.getMD(item.getEtd(),0));
                }
                helper.setText(R.id.tt, "TT:" + item.getTt() + "天");
            }
        });
    }
}
