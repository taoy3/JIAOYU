package com.cnst.wisdom.ui.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.cnst.wisdom.R;
import com.cnst.wisdom.ui.adapter.EditListDialogAdapter;

import java.util.List;

/**
 * Created by Jonas on 2016/3/8.
 */
public class EditListDialog extends Dialog implements AdapterView.OnItemClickListener, View.OnClickListener, DialogInterface.OnShowListener
{
    private final List<String> list;
    private final AppCompatActivity mActivity;
    private Button confirmBt;
    private ListView mListView;
    private int selIndex;
    private int selPosition = -1;
    private EditListDialogAdapter adapter;
    private LinearLayout.LayoutParams params;
    private EditText mEditText;
    private String name;
    private String result;

    public EditListDialog(AppCompatActivity activity, List<String> list, String name)
    {
        super(activity, R.style.no_title_dialog);
        this.list = list;
        this.mActivity = activity;
        this.name = name;
        this.result = name;
        init();
    }

    private void init()
    {
        setContentView(R.layout.dialog_editlist);
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
        setOnShowListener(this);


        findViewById(R.id.cancel).setOnClickListener(this);
        confirmBt = (Button)findViewById(R.id.confirm);
        confirmBt.setOnClickListener(this);

        mEditText = (EditText)findViewById(R.id.editText_post);
        if(!list.contains(name))
        {
            mEditText.setText(name);
        }

        mListView = (ListView)findViewById(R.id.sel_list);
        adapter = new EditListDialogAdapter(mActivity, list, name);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        params = (LinearLayout.LayoutParams)mListView.getLayoutParams();

        mEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                name = s.toString();
                adapter.setIndex(name);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s)
            {


            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        RelativeLayout relativeLayout = (RelativeLayout)view;
        RadioButton radioButton = (RadioButton)relativeLayout.findViewById(R.id.radio_post);
        radioButton.setChecked(true);
        name = list.get(position);
    }

    public EditListDialogAdapter getAdapter()
    {
        return adapter;
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.confirm:
                result = name;
                dismiss();
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

    public String getInput()
    {
        return mEditText.getText().toString().trim();
    }


    public void setPost(String post)
    {
        mEditText.setText(post);
    }

    public String getResult()
    {
        return result;
    }

}
