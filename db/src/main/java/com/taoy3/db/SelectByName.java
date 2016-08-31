package com.taoy3.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by taoy3 on 16/8/20.
 */
public class SelectByName {
    public static Cursor selectByName(List<String> keys, String dbName, Context context){
        // 先获得一个SqliteDatabase对象
        File path = context.getDatabasePath(dbName+".db");
        path.getParentFile().mkdirs();
        try {
            path.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(path.getAbsolutePath());
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);
		/*String sql = "create table  user(id integer primary key, name varchar(20), sex varchar(3))";
		db.execSQL(sql);*/
	/*
		String insertSql = "insert into user(id, name, sex) values(2, 'aa', 'cc')";
		db.execSQL(insertSql);*/  //这个方法只用增、删、改、建表

        String query = "select * from "+dbName;
        Cursor cursor = db.rawQuery(query, new String[]{});  //执行查询语句，返回一个cursor对象   游标
       return cursor;
    }
}
