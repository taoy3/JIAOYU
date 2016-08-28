package com.cnst.wisdom.ui.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cnst.wisdom.R;
import com.cnst.wisdom.model.bean.Userinfo;
import com.cnst.wisdom.ui.adapter.CheckboxDialogAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 带复选框的对话框
 * 创建时需要传已选中的值
 */
public class CheckboxDialog extends Dialog implements AdapterView.OnItemClickListener, View.OnClickListener, DialogInterface.OnShowListener
{
    private List<Userinfo.DataEntity.ClassListEntity> list;
    private List<Userinfo.DataEntity.ClassListEntity> checked;
    private List<Userinfo.DataEntity.ClassListEntity> result = new ArrayList<>();

    private final AppCompatActivity mActivity;
    private Button confirmBt;
    private ListView mListView;
    private int selIndex;
    private int selPosition = -1;
    private CheckboxDialogAdapter adapter;
    private LinearLayout.LayoutParams params;

    public CheckboxDialog(AppCompatActivity activity, List<Userinfo.DataEntity.ClassListEntity> list, List<Userinfo.DataEntity.ClassListEntity> checked)
    {
        super(activity, R.style.no_title_dialog);
        this.list = list;
        this.checked = checked;
        this.mActivity = activity;
        this.result.addAll(checked);
        init();
    }

    private void init()
    {
        setContentView(R.layout.dialog_list);
        Window window = getWindow();
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        window.setBackgroundDrawableResource(R.drawable.bg_dialog);
        window.setLayout((int)( width*0.8 ), ViewGroup.LayoutParams.WRAP_CONTENT);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            window.setClipToOutline(true);
        }
        mListView = (ListView)findViewById(R.id.sel_list);
        adapter = new CheckboxDialogAdapter(mActivity, list, checked);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(this);
        params = (LinearLayout.LayoutParams)mListView.getLayoutParams();
        findViewById(R.id.cancel).setOnClickListener(this);
        confirmBt = (Button)findViewById(R.id.confirm);
        confirmBt.setOnClickListener(this);
        setOnShowListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        RelativeLayout rl = (RelativeLayout)view;
        CheckBox cb = (CheckBox)rl.findViewById(R.id.checkbox_classname);
        if(cb.isChecked())
        {
            checked.remove(list.get(position));
            cb.setChecked(false);
        }else
        {
            checked.add(list.get(position));
            cb.setChecked(true);
        }

    }

    public CheckboxDialogAdapter getAdapter()
    {
        return adapter;
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.confirm:
                result = checked;
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
            default:
                break;

        }
    }

    public int getSelIndex()
    {
        return selIndex;
    }

    public void setSelIndex(int selText)
    {
        this.selIndex = selText;
    }

    public int getSelPositon()
    {
        return selPosition;
    }

    public void setSelPositon(int selPosition)
    {
        this.selPosition = selPosition;
    }

    @Override
    public void onShow(DialogInterface dialog)
    {
        if(list.size()>8)
        {
            setListMaxHeight(8);
        }else
        {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    public void setListMaxHeight(int max)
    {
        if(adapter == null)
        {
            return;
        }
        int totalHeight = 0;
        for(int i = 0; i<max; i++)
        {
            View mView = adapter.getView(i, null, mListView);
            mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
        }
        params.height = totalHeight;
        mListView.setLayoutParams(params);
        mListView.requestLayout();
    }

    public List<Userinfo.DataEntity.ClassListEntity> getResult()
    {
        Log.i("retuurn", result.toString());
        return result;
    }
}
