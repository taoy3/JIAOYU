package com.cnst.wisdom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqliteOpneHelper extends SQLiteOpenHelper {
	
	public MySqliteOpneHelper(Context context, int version) {
		/**
		 * 帮组我们去创建或者打开相应的数据库
		 * 1、上下文对象
		 * 2、数据库的文件名  一般用.db
		 * 3、CursorFactory  一般都是用null来表示使用系统默认的
		 * 4、版本号
		 */
		super(context, "a1504.db", null, version);  //
	}
	/**
	 * 只有当数据库文件不存在的时候，才会调用这个方法。
	 * 写我们的建表的操作
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.e("MySqliteOpneHelper", "onCreate....");
		
		String sql = "create table person(_id invvteger primary key autoincrement, " +
				"name varchar(20) not null, sex varchar(2), age integer)";
		db.execSQL(sql);  //使用传来的SQLiteDatabase数据库对象，来执行sql语句。
		
	}
	/**
	 * 当数据库版本更新的时候，会自动调用这个方法。
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("MySqliteOpneHelper", "onUpgrade....");
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		Log.e("MySqliteOpneHelper", "onOpen....");
		super.onOpen(db);
	}
	
}
