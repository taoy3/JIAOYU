package com.taoy3.db;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.List;


public class DbActivity extends AppCompatActivity {

    private DBAccess dba;
    private List<Person> persons;  //给ListView提供数据
    private ListView listView;
    private DbAdapter adapter;

    private int currentPage = 1;
    private int size = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_activity_main);
        dba = new DBAccess(this);

        listView = (ListView) findViewById(R.id.listView1);
        persons = dba.queryPerson(currentPage, size);
        adapter = new DbAdapter(this, persons);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person person = DbActivity.this.persons.get(position);
                person.setName(person.getName() + 1);
                dba.updatePerson(person, person.getId());
                persons.clear();
                persons.addAll(dba.queryPerson(currentPage, size));
                adapter.notifyDataSetChanged();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dba.deletePerson(DbActivity.this.persons.get(position).getId());
                persons.clear();
                persons.addAll(dba.queryPerson(currentPage, size));
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.mState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mState == SCROLL_STATE_IDLE) {
                    if (firstVisibleItem == 0) {
                        dba.insertPerson(new Person(DbActivity.this));
                        persons.clear();
                        persons.addAll(dba.queryPerson(currentPage, size));
                        adapter.notifyDataSetChanged();
            /*
             * 更换cursor，会自动的通知ListView去刷新视图，
			 * 并且可以自动关闭旧的cursor对象
			 * 而不需要手动去调用Adapter的notifyData...
			 */
                        adapter.notifyDataSetChanged();
                    } else if (firstVisibleItem + visibleItemCount == totalItemCount) {
                        currentPage++;
                        persons.clear();
                        persons.addAll(dba.queryPerson(currentPage, size));
                        adapter.notifyDataSetChanged();
                        if (persons.size() > 0) {
                            adapter.notifyDataSetChanged();
                        } else {
                            currentPage--;
                        }
                    }
                }

            }
        });

        final SearchView searchView = (SearchView) findViewById(R.id.searchView1);
        //设置右下角的按钮按下的时候的动作的监听器
        searchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                persons.clear();
                persons.addAll(dba.searchPerson(query));
                adapter.notifyDataSetChanged();
                //获得输入法管理器，然后再去隐藏输入法
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }
}
