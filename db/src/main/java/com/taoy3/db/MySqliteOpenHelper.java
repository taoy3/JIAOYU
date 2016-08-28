package com.taoy3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqliteOpenHelper extends SQLiteOpenHelper {
	private final String TAG = "MySqliteOpenHelper";
	private String dbName;
	private String sql;
	public MySqliteOpenHelper(Context context, int version, String dbName,String sql) {
		/**
		 * 创建或者打开相应的数据库
		 * 1、上下文对象
		 * 2、数据库的文件名  一般用.db
		 * 3、CursorFactory  一般都是用null来表示使用系统默认的
		 * 4、版本号
		 */
		super(context, dbName+".db", null, version);
		this.dbName = dbName;
		this.sql = sql;
	}
	/**
	 * 只有当数据库文件不存在的时候，才会调用这个方法。
	 * 写我们的建表的操作
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

		Log.e("MySqliteOpenHelper", "onCreate...."+dbName);
		db.execSQL(sql);  //使用传来的SQLiteDatabase数据库对象，来执行sql语句。
//		db.close();
		
	}

	/**
	 * 当数据库版本更新的时候，会自动调用这个方法。
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e(TAG, dbName+oldVersion+"onUpgrade...."+newVersion);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		Log.e(TAG, "onOpen...."+dbName);
		super.onOpen(db);
	}
	
}
