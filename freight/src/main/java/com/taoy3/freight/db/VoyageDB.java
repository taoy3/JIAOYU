package com.taoy3.freight.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.taoy3.freight.bean.Company;
import com.taoy3.freight.bean.Port;
import com.taoy3.freight.bean.Voyage;

import java.util.ArrayList;
import java.util.List;

public class VoyageDB {
	private static final String TABLE_NAME = "voyage";
	private static final String ID="id";
	private static final String SC_NAME="sc_name";
	private static final String SCHEMA="schema";//航次
	private static final String VESSLE_NAME="vessle_name";
	private static final String SERVICE_NAME="service_name";
	private static final String SERVICE_ID="service_id";
	private static final String SC_ID="sc_id";
	private static final String START_ID ="start_id";
	private static final String DEST_ID="dest_id";
	private static final String STATE="state";
	private static final String VIA="via";
	public static final String SQL="create table "+ TABLE_NAME + "("+ID+" integer primary key autoincrement, " +
			SC_NAME+" varchar(20), "+SCHEMA+" varchar(20), "+VESSLE_NAME+" varchar(20), "+START_ID+" integer, "
			+SERVICE_NAME+" varchar(20), " +SERVICE_ID+" varchar(20), " +SC_ID+" integer, "+DEST_ID+" integer, "
			+VIA+" integer, "+STATE+" integer)";
	private static ExternalDbHelper helper;
	private static VoyageDB dbAccess;
	private static PortInfoDB portInfoDB;

	private VoyageDB() {
		helper = ExternalDbHelper.getInstance();
	}

	public static VoyageDB getInstance() {
		if (dbAccess == null) {
			synchronized (VoyageDB.class) {
				if (dbAccess == null) {
					dbAccess = new VoyageDB();
					portInfoDB = PortInfoDB.getInstance();
				}
			}
		}
		return dbAccess;
	}
	public void insert(Voyage voyage){
		SQLiteDatabase db = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(SC_NAME,voyage.getSc_name());
		values.put(VESSLE_NAME,voyage.getVessle_name());
		values.put(SCHEMA,voyage.getSchema());
		values.put(SERVICE_NAME,voyage.getService_name());
		values.put(SERVICE_ID,voyage.getService_id());
		values.put(SC_ID,voyage.getSc_id());
		values.put(START_ID,voyage.getStart_id());
		values.put(DEST_ID,voyage.getDest_id());
		values.put(VIA,voyage.getVia());
		values.put(STATE,voyage.getState());
		long id = db.insert(TABLE_NAME, null, values);
		portInfoDB.insert(voyage.getPorts(),id);
		db.close();
	}
	/**
	 * 删除指定name的person
	 * @param id
	 */
	public void delete(int id){
		SQLiteDatabase db = helper.getReadableDatabase();
		db.delete(TABLE_NAME, ID+"=?", new String[]{id+""});
		portInfoDB.delete(id);
		db.close();
		
	}
	
	public List<Voyage> query(){
		SQLiteDatabase db = helper.getReadableDatabase();
		return getCursor(db.query(TABLE_NAME, null, null, null, null, null, null, null));
	}
	
	public void update(Voyage voyage){
		SQLiteDatabase db = helper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(SC_NAME,voyage.getSc_name());
		values.put(VESSLE_NAME,voyage.getVessle_name());
		values.put(SERVICE_NAME,voyage.getService_name());
		values.put(SERVICE_ID,voyage.getService_id());
		values.put(SC_ID,voyage.getSc_id());
		values.put(START_ID,voyage.getStart_id());
		values.put(DEST_ID,voyage.getDest_id());
		values.put(VIA,voyage.getVia());
		values.put(SCHEMA,voyage.getSchema());
		values.put(STATE,voyage.getState());
		db.update(TABLE_NAME, values, ID+"=?", new String[]{voyage.getId()+""});
		portInfoDB.update(voyage.getPorts());
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
	public List<Voyage> query(int currentPage, int size){
		SQLiteDatabase db = helper.getReadableDatabase();
		return getCursor(db.query(TABLE_NAME, null, null, null, null, null,
				ID+" desc", (currentPage - 1) * size + "," + size));
	}
	@NonNull
	private List<Voyage> getCursor(Cursor cursor) {
		List<Voyage> list = new ArrayList<>();
		while (cursor.moveToNext()){
			Voyage object = new Voyage();
			object.setId(cursor.getInt(cursor.getColumnIndex(ID)));
			object.setVia(cursor.getInt(cursor.getColumnIndex(VIA)));
			object.setPorts(portInfoDB.query(object.getId()));
			object.setSc_id(cursor.getInt(cursor.getColumnIndex(SC_ID)));
			object.setStart_id(cursor.getInt(cursor.getColumnIndex(START_ID)));
			object.setDest_id(cursor.getInt(cursor.getColumnIndex(DEST_ID)));
			object.setSc_name(cursor.getString(cursor.getColumnIndex(SC_NAME)));
			object.setSchema(cursor.getString(cursor.getColumnIndex(SCHEMA)));
			object.setService_id(cursor.getString(cursor.getColumnIndex(SERVICE_ID)));
			object.setService_name(cursor.getString(cursor.getColumnIndex(SERVICE_NAME)));
			object.setState(cursor.getInt(cursor.getColumnIndex(STATE)));
			object.setVessle_name(cursor.getString(cursor.getColumnIndex(VESSLE_NAME)));
			list.add(object);
		}
		return list;
	}

	public void insert(List<Voyage> voyages) {
		SQLiteDatabase db = helper.getReadableDatabase();
		for (Voyage voyage:voyages) {
			ContentValues values = new ContentValues();
			values.put(SC_NAME,voyage.getSc_name());
			values.put(VESSLE_NAME,voyage.getVessle_name());
			values.put(SCHEMA,voyage.getSchema());
			values.put(SERVICE_NAME,voyage.getService_name());
			values.put(SERVICE_ID,voyage.getService_id());
			values.put(SC_ID,voyage.getSc_id());
			values.put(VIA,voyage.getVia());
			values.put(STATE,voyage.getState());
			values.put(START_ID,voyage.getStart_id());
			values.put(DEST_ID,voyage.getDest_id());
			long id = db.insert(TABLE_NAME, null, values);
			portInfoDB.insert(voyage.getPorts(),id);
		}
		db.close();
	}

	public Voyage query(int id) {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Voyage> voyages = getCursor(db.query(TABLE_NAME, null, ID + "=?"
				, new String[]{id + ""}, null, null, ID));
		db.close();
		if(voyages.size()==0){
			return null;
		}
		return voyages.get(0);
	}

	public List<Voyage> query(Port startPort, Port destPort, Company company) {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Voyage> voyages = getCursor(db.query(TABLE_NAME, null, SC_ID + "=? and "+START_ID
				+"=? and "+DEST_ID+"=?", new String[]{company.getId() + "",startPort.getId()+"",
				destPort.getId()+""}, null, null, ID));
		db.close();
		return voyages;
	}
}
