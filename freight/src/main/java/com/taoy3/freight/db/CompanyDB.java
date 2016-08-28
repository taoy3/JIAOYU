package com.taoy3.freight.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.taoy3.freight.bean.Company;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDB {
	private final static String TABLE_NAME = "company";

	private final static  String NAME_ZH="name_zh";
	private final static  String CODE="code";
	private final static  String TYPE_BK="type_bk";
	private final static  String DESC_SUT="desc_sut";
	private final static  String TEL_SUTC="tel_sutc";
	private final static  String NAME_SUTC="name_sutc";
	private final static  String VALID="valid";
	private final static  String TYPE_SUT="type_sut";
	private final static  String ID="id";
	private final static  String DESC_BK="desc_bk";
	private final static  String TEL_BKC="tel_bkc";
	private final static  String NAME_EN="name_en";
	private final static  String NAME_BKC="name_bkc";
	 final static String SQL = "create table "+ TABLE_NAME
			+"("+ID+" integer primary key, "
			+ NAME_ZH+" varchar(70), "
			+ CODE+" varchar(70), "
			+ TYPE_BK+" integer, "
			+ DESC_SUT+" varchar(70), "
			+ TEL_SUTC+" integer, "
			+ NAME_SUTC+" varchar(70), "
			+ VALID+" integer, "
			+ TYPE_SUT+" integer,"
			+ DESC_BK+" varchar(70), "
			+ TEL_BKC+" varchar(70), "
			+ NAME_EN+" varchar(70), "
			+ NAME_BKC+" varchar(70))";
	private static BaseDbHelper helper = null;
	private static CompanyDB dbAccess  = null;
	private  String TAG = "CompanyDB";
	private CompanyDB() {
		helper = BaseDbHelper.getInstance();
	}
	public static CompanyDB getInstance() {
		if(dbAccess==null){
			synchronized (CompanyDB.class){
				if(dbAccess==null){
					dbAccess = new CompanyDB();
				}
			}
		}
		return dbAccess;
	}
	public void insert(Company company){
	
		/*
		 * 返回一个可读写的数据库，但是当磁盘满或者一些别的原因导致的数据库无法写入的时候，则会抛出异常。
		 
		helper.getWritableDatabase();*/
		
		/*
		 * 获得sqliteDatabase对象
		 * 返回一个可读写的数据库，但是当磁盘满或者一些别的原因导致的数据库无法写入的时候，则返回的是一个只读的数据库。
		 */
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "insert into "+ TABLE_NAME +"("+ID+", "+NAME_ZH+", "+CODE+", "+TYPE_BK+", "+DESC_SUT+
				", "+TEL_SUTC+", "+NAME_SUTC+", "+VALID+", "+TYPE_SUT+", "+DESC_BK+", "+TEL_BKC+", "+
				NAME_EN+", "+NAME_BKC+") values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		db.execSQL(sql, new String[]{company.getId()+"", company.getName_zh(), company.getCode(),company.getType_bk()+""
		,company.getDesc_sut(),company.getTel_sutc(),company.getName_sutc(),company.getValid()+"",company.getType_sut()+""
		,company.getDesc_bk(),company.getTel_bkc(),company.getName_en(),company.getName_bkc()});
		db.close();//关闭数据库
	}

	/**
	 * 删除指定name的person
	 * @param id
	 */
	public void delete(int id){
		String sql = "delete from "+ TABLE_NAME +" where "+ID+"=?";
		SQLiteDatabase db = helper.getReadableDatabase();
//		db.beginTransaction();
		db.execSQL(sql, new String[]{id+""});
//		db.setTransactionSuccessful();
		db.close();
	}
	public List<Company> queryAll(){
		String sql = "select * from "+ TABLE_NAME;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		return getCursor(cursor);
	}

	@NonNull
	private  List<Company> getCursor(Cursor cursor) {
		List<Company> list = new ArrayList<>();
		while (cursor.moveToNext()){
			Company company= new Company();
			company.setId(cursor.getInt(cursor.getColumnIndex(ID)));
			company.setCode(cursor.getString(cursor.getColumnIndex(CODE)));
			company.setDesc_bk(cursor.getString(cursor.getColumnIndex(DESC_BK)));
			company.setDesc_sut(cursor.getString(cursor.getColumnIndex(DESC_SUT)));
			company.setName_bkc(cursor.getString(cursor.getColumnIndex(NAME_BKC)));
			company.setName_en(cursor.getString(cursor.getColumnIndex(NAME_EN)));
			company.setName_zh(cursor.getString(cursor.getColumnIndex(NAME_ZH)));
			company.setName_sutc(cursor.getString(cursor.getColumnIndex(NAME_SUTC)));
			company.setTel_bkc(cursor.getString(cursor.getColumnIndex(TEL_BKC)));
			company.setTel_sutc(cursor.getString(cursor.getColumnIndex(TEL_SUTC)));
			company.setType_bk(cursor.getInt(cursor.getColumnIndex(TYPE_BK)));
			company.setType_sut(cursor.getInt(cursor.getColumnIndex(TYPE_SUT)));
			company.setValid(cursor.getInt(cursor.getColumnIndex(VALID)));
			list.add(company);
		}
		cursor.close();
		return list;
	}

	public  void update(Company company){
		String sql = "update "+ TABLE_NAME +" set "+CODE+"=?, "+DESC_BK+"=?, "+DESC_SUT+"=?, "
				+NAME_BKC+"=?, " +NAME_EN+"=?, "+NAME_ZH+"=?, "+NAME_SUTC+"=?, "+TEL_BKC+"=?, "
				+TEL_SUTC+"=?, "+TYPE_BK+"=?, "+TYPE_SUT+"=?, "+VALID+"=? where "+ID+"=?";
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL(sql, new String[]{company.getCode(), company.getDesc_bk(), company.getDesc_sut()
				, company.getName_bkc(),company.getName_en(),company.getName_zh(),company.getName_sutc()
				,company.getTel_bkc(),company.getType_sut()+"",company.getType_bk()+"",company.getType_sut()+""
				,company.getValid()+"",company.getId()+""});
		db.close();
	}

	/**
	 * 根据制定的页数和每页的记录数来查询数据，并返回查询到的cursor
	 * @param currentPage 当前的页数
	 * @param size  每页的记录数
	 * @return  查询的cursor
	 */
	public  List<Company> query(int currentPage, int size){
		//  limit a, b  a代表是从数据中的第几条数据开始查询， b查询多条数据
		String sql = "select * from "+ TABLE_NAME +" limit ?, ?";  //
		SQLiteDatabase db = helper.getReadableDatabase();
		//size = 5    0   5  10
		return getCursor(db.rawQuery(sql, new String[]{(currentPage - 1) * size + "" ,size+""}));
	}

	public  List<Company> search(String query) {
		try {
			query=new String(("'%"+query+"%'").getBytes(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String sql= "select * from "+ TABLE_NAME +" where "+NAME_ZH +" like "+query+" or "+NAME_EN+" like "+query+" or "
				+CODE+" like "+query;
		Log.i(TAG,sql);
		SQLiteDatabase db = helper.getReadableDatabase();
		return getCursor(db.rawQuery(sql,new String[]{}));
	}

	public  void insert(List<Company> companyEntities) {
		Log.i(TAG,"init port db");
		SQLiteDatabase db = helper.getReadableDatabase();
		for (int i = 0;i<companyEntities.size();i++) {
			Company company = companyEntities.get(i);
			String sql = "insert into "+ TABLE_NAME +"("+ID+", "+NAME_ZH+", "+CODE+", "+TYPE_BK+", "+DESC_SUT+
					", "+TEL_SUTC+", "+NAME_SUTC+", "+VALID+", "+TYPE_SUT+", "+DESC_BK+", "+TEL_BKC+", "+
					NAME_EN+", "+NAME_BKC+") values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			db.execSQL(sql, new String[]{company.getId()+"", company.getName_zh(), company.getCode(),company.getType_bk()+""
					,company.getDesc_sut(),company.getTel_sutc(),company.getName_sutc(),company.getValid()+"",company.getType_sut()+""
					,company.getDesc_bk(),company.getTel_bkc(),company.getName_en(),company.getName_bkc()});
		}
		db.close();//关闭数据库
	}
	public Company query(int id) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
				+ " where " + ID + "=?", new String[]{id + ""});
		List<Company> companies = getCursor(cursor);
		if(companies.size()==0){
			return null;
		}
		return companies.get(0);
	}

	public int queryIdByName(String name) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select " + ID + " from " + TABLE_NAME
				+ " where " + NAME_ZH + "=?", new String[]{name});
		return cursor.getInt(cursor.getColumnIndex(ID));
	}
	public String queryName(int id) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select " +NAME_ZH + " from " + TABLE_NAME
				+ " where " + ID + "=?", new String[]{id + ""});
		String name = null;
		if(cursor.moveToNext()){
			name = cursor.getString(cursor.getColumnIndex(NAME_ZH));
		}
		return name;
	}
//	public List<String> searchName(String s) {
//		String sql= "select "+N+" from "+TABLE_NAME+" where "+NAME_ZH +"match "+query+"or"+NAME_EN+"match"
//				+query+"or"+CODE+"match"+query;
//		SQLiteDatabase db = helper.getReadableDatabase();
//		return getCursor(db.rawQuery(sql, new String[]{query}));
//	}
}
