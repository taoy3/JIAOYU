package com.taoy3.freight.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.taoy3.freight.BaseApp;

public class BaseDbHelper extends SQLiteOpenHelper {
	private static final String TAG = "BaseDbHelper";
	private static BaseDbHelper helper = null;
	private static final String dbName = "base.db";
	private BaseDbHelper() {
		super(BaseApp.getApp(),"base.db" , null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "onCreate...."+dbName);
		db.execSQL(PortDB.SQL);
		db.execSQL(CompanyDB.SQL);
	}

	/**
	 * 当数据库版本更新的时候，会自动调用这个方法。
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, oldVersion+"onUpgrade...."+newVersion);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		Log.i(TAG, "onOpen...."+dbName);
		super.onOpen(db);
	}

	public static BaseDbHelper getInstance() {
		if(helper==null){
			synchronized (BaseDbHelper.class){
				if(helper==null){
					helper = new BaseDbHelper();
				}
			}
		}
		return helper;
	}
}
