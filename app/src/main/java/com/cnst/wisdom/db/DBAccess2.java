package com.cnst.wisdom.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAccess2 {
	private MySqliteOpneHelper helper;

	public DBAccess2(Context context){
		helper = new MySqliteOpneHelper(context, 1);
	}
	
	public void insertPerson(Person person){
		SQLiteDatabase db = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", person.getName());
		values.put("age", person.getAge());
		values.put("sex", person.getSex());
		db.insert("person", null, values);
		db.close();
	}
	/**
	 * 删除指定name的person
	 * @param name
	 */
	public void deletePerson(String name){
		SQLiteDatabase db = helper.getReadableDatabase();
		db.delete("person", "name=?", new String[]{name});
		db.close();
		
	}
	
	public Cursor queryAllPerson(){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		return db.query("person", null, null, null, null, null, null, null);
	}
	
	public void updatePerson(Person person, int age){
		SQLiteDatabase db = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", person.getName());
		values.put("age", person.getAge());
		values.put("sex", person.getSex());
		db.update("person", values, "age=?", new String[]{age+""});
		db.close();
	}
	/**
	 * 根据制定的页数和每页的记录数来查询数据，并返回查询到的cursor
	 * @param currentPage 当前的页数
	 * @param size  每页的记录数
	 * @return  查询的cursor
	 * 
	 * a, 
	 */
	public Cursor queryPerson(int currentPage, int size){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		return db.query("person", null, null, null, null, null, "age desc", (currentPage - 1) * size + "," + size);
	}

	public Cursor searchPerson(String query) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.query("person", null, "age>?", new String[]{query}, null, null, null);
		
	}
	
	

}
