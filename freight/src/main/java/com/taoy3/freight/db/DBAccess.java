package com.taoy3.freight.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.taoy3.db.MySqliteOpenHelper;
import com.taoy3.db.Person;

import java.util.ArrayList;
import java.util.List;

public class DBAccess {
	private MySqliteOpenHelper helper;
	private final String DB_NAME = "person";
	private final String NAME = "name";
	private final String AGE = "age";
	private final String SEX = "sex";
	private final String _ID="_id";//SQLite默认,不可更改
	public DBAccess(Context context){
		helper = new MySqliteOpenHelper(context, 1,DB_NAME,"create table "+ DB_NAME +
				"(_id integer primary key autoincrement, " +
				NAME+" varchar(20) not null, "+SEX+" varchar(2), "+AGE+" integer)");
	}
	
	public void insertPerson(Person person){
		SQLiteDatabase db = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, person.getName());
		values.put(AGE, person.getAge());
		values.put(SEX, person.getSex());
		db.insert(DB_NAME, null, values);
		db.close();
	}
	/**
	 * 删除指定name的person
	 * @param id
	 */
	public void deletePerson(int id){
		SQLiteDatabase db = helper.getReadableDatabase();
		db.delete(DB_NAME, _ID+"=?", new String[]{id+""});
		db.close();
		
	}
	
	public List<Person> queryAllPerson(){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		return getCursor(db.query(DB_NAME, null, null, null, null, null, null, null));
	}
	
	public void updatePerson(Person person, int id){
		SQLiteDatabase db = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(NAME, person.getName());
		values.put(AGE, person.getAge());
		values.put(SEX, person.getSex());
		db.update(DB_NAME, values, _ID+"=?", new String[]{id+""});
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
	public List<Person> queryPerson(int currentPage, int size){
		SQLiteDatabase db = helper.getReadableDatabase();
		
		return getCursor(db.query(DB_NAME, null, null, null, null, null,
				_ID+" desc", (currentPage - 1) * size + "," + size));
	}

	public List<Person> searchPerson(String query) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return getCursor(db.query(DB_NAME, null, AGE+">?", new String[]{query}, null, null, null));
	}
	@NonNull
	private List<Person> getCursor(Cursor cursor) {
		cursor.moveToFirst();
		List<Person> list = new ArrayList<>();
		while (!cursor.isAfterLast()){
			list.add(new Person(cursor.getInt(0)
					,cursor.getInt(cursor.getColumnIndex(AGE))
					,cursor.getString(cursor.getColumnIndex(NAME))
					,cursor.getString(cursor.getColumnIndex(SEX))));
			cursor.moveToNext();
		}
		return list;
	}
}
