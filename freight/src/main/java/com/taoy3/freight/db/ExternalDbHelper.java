package com.taoy3.freight.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.taoy3.freight.BaseApp;

public class ExternalDbHelper extends SQLiteOpenHelper {
	private final String TAG = "ExternalDbHelper";
	private static ExternalDbHelper helper = null;
	private final String dbName = "external.db";
	private ExternalDbHelper() {
		super(BaseApp.getApp(),"external.db" , null, 1);
	}
	public static ExternalDbHelper getInstance() {
		if(helper==null){
			synchronized (BaseDbHelper.class){
				if(helper==null){
					helper = new ExternalDbHelper();
				}
			}
		}
		return helper;
	}
	/**
	 * 只有当数据库文件不存在的时候，才会调用这个方法。
	 * 写我们的建表的操作
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("ExternalDbHelper", "onCreate...."+dbName);
		db.execSQL(VoyageDB.SQL);
		db.execSQL(PortInfoDB.SQL);
		db.execSQL(FreightDB.SQL);
		db.execSQL(PriceDB.SQL);
		db.execSQL(BoxDB.SQL);
		db.execSQL(SurchargesDB.SQL);
//		db.close();
		
	}

	/**
	 * 当数据库版本更新的时候，会自动调用这个方法。
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, dbName+oldVersion+"onUpgrade...."+newVersion);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		Log.i(TAG, "onOpen...."+dbName);
		super.onOpen(db);
	}
}
