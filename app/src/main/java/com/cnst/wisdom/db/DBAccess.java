package com.cnst.wisdom.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBAccess {
	private MySqliteOpneHelper helper;

	public DBAccess(Context context){
		helper = new MySqliteOpneHelper(context, 1);
	}
	
	public void insertPerson(Person person){
	
		/*
		 * 返回一个可读写的数据库，但是当磁盘满或者一些别的原因导致的数据库无法写入的时候，则会抛出异常。
		 
		helper.getWritableDatabase();*/
		
		/*
		 * 获得sqliteDatabase对象
		 * 返回一个可读写的数据库，但是当磁盘满或者一些别的原因导致的数据库无法写入的时候，则返回的是一个只读的数据库。
		 */
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "insert into person(name, sex, age) values(?, ?, ?)";
		db.execSQL(sql, new String[]{person.getName(), person.getSex(), person.getAge() + ""});
		db.close();//关闭数据库
	}

	/**
	 * 删除指定name的person
	 * @param name
	 */
	public void deletePerson(String name){
		String sql = "delete from person where name=?";
		SQLiteDatabase db = helper.getReadableDatabase();
		db.beginTransaction();
		db.execSQL(sql, new String[]{name});
		db.setTransactionSuccessful();
		db.close();
	}
	public Cursor queryAll(){
		String sql = "select * from person";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		return cursor;
	}
	public List<Person> queryAllPerson(){
		List<Person> persons = new ArrayList<>();
		
		String sql = "select * from person";
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		//移动cursor到下一条记录，如果移动成功，则返回true 否则返回false
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String sex = cursor.getString(cursor.getColumnIndex("sex"));
			int age = cursor.getInt(cursor.getColumnIndex("age"));
			
			persons.add(new Person(age, name, sex));
		}
		return persons;
	}
	
	public void updatePerson(Person person, int age){
		String sql = "update person set name=?, age=?, sex=? where age=?";
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL(sql, new String[]{person.getName(), person.getAge()+"", person.getSex(), age+""});
		db.close();
	}

	/**
	 * 根据制定的页数和每页的记录数来查询数据，并返回查询到的cursor
	 * @param currentPage 当前的页数
	 * @param size  每页的记录数
	 * @return  查询的cursor
	 */
	public Cursor queryPerson(int currentPage, int size){
		//  limit a, b  a代表是从数据中的第几条数据开始查询， b查询多条数据
		String sql = "select * from person limit ?, ?";  //
		SQLiteDatabase db = helper.getReadableDatabase();
		//size = 5    0   5  10

		return db.rawQuery(sql, new String[]{(currentPage - 1) * size + "" ,size+""});
	}

	public Cursor searchPerson(String query) {
		return null;
	}
}
