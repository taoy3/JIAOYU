package com.cnst.wisdom.db;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleCursorAdapter;

import com.cnst.wisdom.R;

import java.util.List;

public class DbActivity extends Activity implements OnClickListener {

	private DBAccess dba;
	private List<Person> persons;  //给ListView提供数据
	private ListView listView;
	private SimpleCursorAdapter adapter;
	
	private int currentPage = 1;
	private int size = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.db_activity_main);
		
		dba = new DBAccess(this);

		/*for(int i = 0; i < 20; i++){
			dba.insertPerson(new Person(20 + i, "凤姐" + i, "女"));
		}*/
		Button insertBtn = (Button) findViewById(R.id.insert);
		Button deleteBtn = (Button) findViewById(R.id.delete);
		Button updateBtn = (Button) findViewById(R.id.update);
		insertBtn.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);
		updateBtn.setOnClickListener(this);
		
		listView = (ListView) findViewById(R.id.listView1);
		adapter = new SimpleCursorAdapter(
				this,   
				R.layout.db_item_layout,
				dba.queryPerson(currentPage, size), //数据源
				new String[]{"name", "age", "sex"},
				new int[]{R.id.name, R.id.age, R.id.sex}, 
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		listView.setAdapter(adapter);
		
		Button lastPage = (Button) findViewById(R.id.lastPage);
		Button nextPage = (Button) findViewById(R.id.nextPage);
		lastPage.setOnClickListener(this);
		nextPage.setOnClickListener(this);
		
		final SearchView searchView = (SearchView) findViewById(R.id.searchView1);
		//设置右下角的按钮按下的时候的动作的监听器
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				Cursor cursor = dba.searchPerson(query);
				adapter.changeCursor(cursor);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.insert:
			Person person1 = new Person(300, "范伟360", "女");
			dba.insertPerson(person1);
			Cursor cursor = dba.queryAll();
			/*
			 * 更换cursor，会自动的通知ListView去刷新视图，
			 * 并且可以自动关闭旧的cursor对象
			 * 而不需要手动去调用Adapter的notifyData...
			 */
			adapter.changeCursor(cursor);
			break;
		case R.id.delete:
			dba.deletePerson("范伟360");
			adapter.changeCursor(dba.queryAll());
			break;
		case R.id.update:
			Person person = new Person(10, "志玲", "女");
			int age = 300;
			dba.updatePerson(person, age);
			adapter.changeCursor(dba.queryAll());
			break;
			//上一页
		case R.id.lastPage:
			currentPage--;
			if(currentPage >= 1){
				Cursor cursor2 = dba.queryPerson(currentPage, size);
				if(cursor2.getCount() > 0){
					adapter.changeCursor(cursor2);
				}
			}else{
				currentPage++;
			}
			break;
		    //下一页
		case R.id.nextPage:
			currentPage++;
			Cursor cursor2 = dba.queryPerson(currentPage, size);
			if(cursor2.getCount() > 0){
				adapter.changeCursor(cursor2);
			}else{
				currentPage--;
			}
			break;
		

		default:
			break;
		}

	}
}
