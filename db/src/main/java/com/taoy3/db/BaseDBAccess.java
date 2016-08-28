package com.taoy3.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class BaseDBAccess {
	private final String DB_NAME = "person";
	private final String NAME = "name";
	private final String AGE = "age";
	private final String SEX = "sex";
	private final String _ID="_id";//SQLite默认,不可更改
	private MySqliteOpenHelper helper;

	public BaseDBAccess(Context context){
		helper = new MySqliteOpenHelper(context, 1,DB_NAME,"create table "+ DB_NAME +"("+_ID+" integer primary key autoincrement, " +
				NAME+" varchar(20) not null, "+SEX+" varchar(2), "+AGE+" integer)");
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
		String sql = "insert into "+DB_NAME+"("+NAME+", "+SEX+", "+AGE+") values(?, ?, ?)";
		db.execSQL(sql, new String[]{person.getName(), person.getSex(), person.getAge() + ""});
		db.close();//关闭数据库
	}

	/**
	 * 删除指定name的person
	 * @param id
	 */
	public void deletePerson(int id){
		String sql = "delete from "+DB_NAME+" where "+_ID+"=?";
		SQLiteDatabase db = helper.getReadableDatabase();
//		db.beginTransaction();
		db.execSQL(sql, new String[]{id+""});
//		db.setTransactionSuccessful();
//		db.close();
	}
	public List<Person> queryAll(){
		String sql = "select * from "+DB_NAME;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		return getCursor(cursor);
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

	public void updatePerson(Person person,int id){
		String sql = "update "+DB_NAME+" set "+NAME+"=?, "+AGE+"=?, "+SEX+"=? where "+_ID+"=?";
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL(sql, new String[]{person.getName(), person.getAge()+"", person.getSex(), id+""});
		db.close();
	}

	/**
	 * 根据制定的页数和每页的记录数来查询数据，并返回查询到的cursor
	 * @param currentPage 当前的页数
	 * @param size  每页的记录数
	 * @return  查询的cursor
	 */
	public List<Person> queryPerson(int currentPage, int size){
		//  limit a, b  a代表是从数据中的第几条数据开始查询， b查询多条数据
		String sql = "select * from "+DB_NAME+" limit ?, ?";  //
		SQLiteDatabase db = helper.getReadableDatabase();
		//size = 5    0   5  10
		return getCursor(db.rawQuery(sql, new String[]{(currentPage - 1) * size + "" ,size+""}));
	}

	public List<Person> searchPerson(String query) {
		String sql= "select * from "+DB_NAME+" where "+AGE+">";
		SQLiteDatabase db = helper.getReadableDatabase();
		return getCursor(db.rawQuery(sql, new String[]{query}));
	}
}
