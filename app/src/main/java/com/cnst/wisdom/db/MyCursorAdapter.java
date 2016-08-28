package com.cnst.wisdom.db;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.cnst.wisdom.R;

public class MyCursorAdapter extends CursorAdapter{

	public MyCursorAdapter(Context context, Cursor c) {
		super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	}
	/**
	 * 创建新的view对象，是ListView的每个Item显示的
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.db_item_layout, parent, false);
		return view;
	}
	/**
	 * 给每个item中的控件绑定数据
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		
		TextView etName = (TextView) view.findViewById(R.id.name);
		TextView etSex = (TextView) view.findViewById(R.id.sex);
		TextView etAge = (TextView) view.findViewById(R.id.age);

		etName.setText(cursor.getString(cursor.getColumnIndex("name")));
		etSex.setText(cursor.getString(cursor.getColumnIndex("sex")));
		etAge.setText("" + cursor.getInt(cursor.getColumnIndex("age")));
	}

}
