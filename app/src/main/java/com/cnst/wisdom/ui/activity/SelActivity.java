package com.cnst.wisdom.ui.activity;

import android.widget.TextView;

import com.android.volley.VolleyError;
import com.cnst.wisdom.R;
import com.cnst.wisdom.base.BaseListAdapter;
import com.cnst.wisdom.base.BaseNetActivity;
import com.cnst.wisdom.model.bean.BaseBean;
import com.cnst.wisdom.model.net.NetResult;
import com.cnst.wisdom.ui.adapter.ViewHolderHelper;
import com.cnst.wisdom.ui.widget.InnerGridView;

import java.util.List;
import java.util.Map;

public class SelActivity extends BaseNetActivity {
    private TextView item1View;
    private TextView item2View;
    private TextView item3View;
    private InnerGridView grid1View;
    private InnerGridView grid2View;
    private InnerGridView grid3View;
    
    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_sel);
    }

    @Override
    protected void initData() {
        String url = getIntent().getStringExtra("url");
        volleyManager.getString(url, null, null, new NetResult<Map<String,List<BaseBean>>>() {
            @Override
            public void onFailure(VolleyError error) {
                noData(error.getMessage());
            }

            @Override
            public void onSucceed(Map<String,List<BaseBean>> response) {
                if(response.keySet().size()==3){
                    String[] keys = (String[]) response.keySet().toArray();
                    item1View.setText(keys[0]);
                    item2View.setText(keys[1]);
                    item3View.setText(keys[2]);

                    grid1View.setAdapter(new BaseListAdapter<BaseBean>(SelActivity.this,response.get(keys[0]),R.layout.item_sel) {
                        @Override
                        protected void convert(ViewHolderHelper helper, BaseBean item) {
                            ((TextView)helper.getRootView().findViewById(R.id.sel_text)).setText(item.getName());
                        }
                    });

                    grid2View.setAdapter(new BaseListAdapter<BaseBean>(SelActivity.this,response.get(keys[1]),R.layout.item_sel) {
                        @Override
                        protected void convert(ViewHolderHelper helper, BaseBean item) {
                            ((TextView)helper.getRootView().findViewById(R.id.sel_text)).setText(item.getName());
                        }
                    });

                    grid2View.setAdapter(new BaseListAdapter<BaseBean>(SelActivity.this,response.get(keys[2]),R.layout.item_sel) {
                        @Override
                        protected void convert(ViewHolderHelper helper, BaseBean item) {
                            ((TextView)helper.getRootView().findViewById(R.id.sel_text)).setText(item.getName());
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initView() {
        item1View = (TextView) findViewById(R.id.item1);
        item2View  = (TextView) findViewById(R.id.item2);
        item3View = (TextView) findViewById(R.id.item3);
        
        grid1View = (InnerGridView) findViewById(R.id.grid1);
        grid2View = (InnerGridView) findViewById(R.id.grid2);
        grid3View = (InnerGridView) findViewById(R.id.grid3);
    }
}
