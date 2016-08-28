package com.cnst.wisdom.logic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cnst.wisdom.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImgFileListActivity extends Activity implements OnItemClickListener
{

    ListView listView;
    Util util;
    com.cnst.wisdom.logic.ImgFileListAdapter listAdapter;
    List<FileTraversal> locallist;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imgfilelist);
        listView = (ListView)findViewById(R.id.listView1);
        util = new Util(this);
        locallist = util.LocalImgFileList();
        List<HashMap<String,String>> listdata = new ArrayList<HashMap<String,String>>();
        Bitmap bitmap[] = null;
        if(locallist != null)
        {
            bitmap = new Bitmap[locallist.size()];
            for(int i = 0; i<locallist.size(); i++)
            {
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("filecount", locallist.get(i).filecontent.size()+"张");
                map.put("imgpath",
                        locallist.get(i).filecontent.get(0) == null ? null : ( locallist.get(i).filecontent.get(0) ));
                map.put("filename", locallist.get(i).filename);
                listdata.add(map);
            }
        }
        listAdapter = new com.cnst.wisdom.logic.ImgFileListAdapter(this, listdata);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        Intent intent = new Intent(this, com.cnst.wisdom.logic.ImgsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", locallist.get(arg2));
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(0, data);
        finish();
    }
}
