//package com.taoy3.freight.activity;
//
//import android.content.Intent;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.taoy3.freight.BaseApp;
//import com.taoy3.freight.R;
//import com.taoy3.freight.bean.Port;
//import com.taoy3.freight.constant.CacheDataConstant;
//import com.taoy3.freight.util.Messaging;
//import com.taoy3.freight.view.MySearchView;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class SearchDestActivity extends BaseActivity implements AdapterView.OnItemClickListener {
//    @ViewInject(R.id.destPort_search)
//    private MySearchView searchView;
//    private List<Port> startPorts = CacheDataConstant.ports;
//    private List<String> data = new ArrayList<>();
//    private ArrayAdapter<String> adapter;
//    private List<String> hotList;
//    private ListView listView;
//
//    @Override
//    protected void setView() {
//        setContentView(R.layout.activity_dest);
//    }
//
//    @Override
//    protected void initView(TextView leftView, TextView titleView, TextView rightView) {
//        if(!BaseApp.companyDataIsOk){
//            showBufferDialog();
//            Messaging.register(this);
//        }
//        ViewUtils.inject(this);
//        searchView.setHint(R.string.start_port_tip);
//        searchView.setOnTextContentChange(new MySearchView.OnTextChanged() {
//            @Override
//            public void onTextChangedListener(CharSequence charSequence, int j, int i1, int i2) {
//                data.clear();
//                for (int i = 0; i < startPorts.size(); i++) {
//                    if ((startPorts.get(i).getName_en() != null && startPorts.get(i).getName_en().contains(charSequence.toString().trim().toUpperCase()))
//                            || (startPorts.get(i).getName_en() != null && startPorts.get(i).getName_en().contains(charSequence.toString().trim().toLowerCase()))
//                            || (startPorts.get(i).getName_zh() != null && startPorts.get(i).getName_zh().contains(charSequence.toString().trim()))
//                            || (startPorts.get(i).getCode() != null && startPorts.get(i).getCode().contains(charSequence.toString().trim()))) {
//                        data.add(startPorts.get(i).getName_en() + "（" + startPorts.get(i).getName_zh() + "）");
//                    }
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onSearchCancel() {
//                data.clear();
//                data.addAll(hotList);
//                adapter.notifyDataSetChanged();
//            }
//        });
//        listView = (ListView) findViewById(R.id.start_port);
//    }
//    public void onEvent(Integer event,Integer status) {
//        if(event== Messaging.PORTEVENT&&status==Messaging.OK){
//            bufferDialog.dismiss();
//        }
//    }
//    @Override
//    protected void initData() {
//        hotList = Arrays.asList(getResources().getStringArray(R.array.hot_dest_port));
//        data.addAll(hotList);
//        adapter = new ArrayAdapter(this, R.layout.adapter_array, R.id.text, data);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(this);
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        if(view instanceof TextView){
//        TextView textView = (TextView) view;
//        String[] names = textView.getText().toString().split("（");
//        Intent intent = new Intent();
//        intent.putExtra(ALLDESTPORT, textView.getText().toString());
//        intent.putExtra(DESTPORT, names[0].trim());
//        setResult(DESTPORTCODE, intent);
//        finish();}
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Messaging.unregister(this);
//    }
//}
