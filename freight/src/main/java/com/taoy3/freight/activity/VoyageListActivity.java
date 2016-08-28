package com.taoy3.freight.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.taoy3.freight.R;
import com.taoy3.freight.adapter.BaseListAdapter;
import com.taoy3.freight.adapter.ViewHolderHelper;
import com.taoy3.freight.bean.Company;
import com.taoy3.freight.bean.Port;
import com.taoy3.freight.bean.Voyage;
import com.taoy3.freight.db.CompanyDB;
import com.taoy3.freight.db.PortDB;
import com.taoy3.freight.db.VoyageDB;
import com.taoy3.freight.util.DateUtils;
import com.taoy3.freight.webInterface.VoyageInterface;

import java.util.ArrayList;
import java.util.List;

public class VoyageListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private BaseListAdapter<Voyage> adapter;
    private List<Voyage> voyages = new ArrayList<>();
    private int page = 1;
    private int total;
    private TextView startPortTv;
    private TextView destPortTv;
    private Company company;
    private Port startPort;
    private Port destPort;
    private PortDB dbAccess = PortDB.getInstance();
    private CompanyDB companyDBAccess = CompanyDB.getInstance();


    @Override
    protected void setView() {
        setContentView(R.layout.activity_voyage_list);
    }
    @Override
    protected void initView(TextView leftView,TextView titleView,TextView rightView) {
        Intent intent = getIntent();
        company = companyDBAccess.query(intent.getIntExtra(SCID,0));
        startPort = dbAccess.query(intent.getIntExtra(STARTPORT,0));
        destPort = dbAccess.query(intent.getIntExtra(DESTPORT,0));
        startPortTv = (TextView) findViewById(R.id.start_port);
        startPortTv.setText(startPort.getName_zh());
        destPortTv = (TextView) findViewById(R.id.dest_port);
        destPortTv.setText(destPort.getName_zh());
        listView = (ListView) findViewById(R.id.activity_search_voyage_list);
//        adapter = new VoyageAdapter(this, voyages);
        adapter = new BaseListAdapter<Voyage>(this,voyages,R.layout.adapter_voyage) {
            @Override
            protected void convert(ViewHolderHelper helper, Voyage item) {
                helper.setText(R.id.company_name,item.getSc_name());
                helper.setText(R.id.start_port_zh,item.getSchema());
                helper.setText(R.id.start_port,item.getPorts().get(0).getPort_name());
                helper.setText(R.id.cls,"CLS:" + DateUtils.changeDateFormat(item.getPorts().get(0).getCls()));
                helper.setText(R.id.etd,"ETD:" + DateUtils.changeDateFormat(item.getPorts().get(0).getEtd()));
                helper.setText(R.id.dest_port,item.getPorts().get(item.getPorts().size() - 1).getPort_name());
                helper.setText(R.id.eta,"ETA:" + DateUtils.changeDateFormat(item.getPorts().get(0).getEta()));
                helper.setText(R.id.tt,"TT:" + (item.getPorts().get(item.getPorts().size() - 1).getTt()
                        - item.getPorts().get(0).getTt()) + "天");
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }
    @Override
    protected void initData() {
        voyages = VoyageDB.getInstance().query(startPort, destPort, company);
        if(voyages.size()==0){
            VoyageInterface.create(startPort,destPort,company);
            voyages = VoyageDB.getInstance().query(startPort, destPort, company);
        }
        adapter.addDataAll(voyages);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(this, VoyageDetailActivity.class);
            intent.putExtra(VOYAGE, voyages.get(i).getId());
            startActivityForResult(intent, LOCKCODE);
    }
}
