package com.taoy3.freight.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Administrator on 2015/11/22.
 */
public class MyListView extends ListView implements AbsListView.OnScrollListener {
    private OnScrolling scrolling;
    public MyListView(Context context) {
        super(context);
        init();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
    public void setOnScrolling(OnScrolling scrolling){
        this.scrolling = scrolling;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(firstVisibleItem == 0&&scrolling!=null){
            scrolling.onScrollToTop();
        }else if(firstVisibleItem + visibleItemCount == totalItemCount&&scrolling!=null){
            scrolling.onScrollToBottom();
        }
    }
    public interface OnScrolling{
        void onScrollToBottom();
        void onScrollToTop();
    }
}
