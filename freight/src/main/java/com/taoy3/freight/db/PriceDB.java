package com.taoy3.freight.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.taoy3.freight.bean.PriceEntity;

import java.util.ArrayList;
import java.util.List;

public class PriceDB {
	private static final String TABLE_NAME = "price";
	private static final String ID="id";
	private static final String SC_NAME="sc_name";
	private static final String CLS="cls";//截关日期
	private static final String ETD="etd";//开船日期
	private static final String START_PORT = "startPort";//起运港
	private static final String DEST_PORT = "destPort";//起运港
	private static final String START_ID ="startId";//起运港
	private static final String DEST_ID="destId";//起运港
	private static final String LINE = "line";//航线代码
	private static final String TT="tt";
	private static final String REMARKS="remarks";
	private static final String VIA="via";
	private static final String SVAL_ID="svalid";
	private static final String EVAL_ID="evalid";
	private static final String SC_ID="sc_id";
	public static final String SQL="create table "+ TABLE_NAME + "("+ID+" integer primary key autoincrement, " +
			SC_NAME+" varchar(20), "+CLS+" integer, "+ETD+" integer, "+SC_ID+" varchar(20), "+DEST_PORT
			+" varchar(20), "+START_PORT +" varchar(20), "+START_ID+" integer, "+DEST_ID+" integer, "
			+TT+" integer, "+REMARKS +" varchar(20), "+VIA+" integer, "+SVAL_ID+" integer, "+EVAL_ID+" integer, "
			+SC_ID+" integer, "+LINE+" integer)";
	private static ExternalDbHelper helper;
	private static PriceDB dbAccess;
	private static SurchargesDB surchargesDB;
	private static BoxDB boxDB;

	private PriceDB() {
		helper = ExternalDbHelper.getInstance();
	}

	public static PriceDB getInstance() {
		if (dbAccess == null) {
			synchronized (PriceDB.class) {
				if (dbAccess == null) {
					dbAccess = new PriceDB();
					surchargesDB = SurchargesDB.getInstance();
					boxDB = BoxDB.getInstance();
				}
			}
		}
		return dbAccess;
	}
	/**
	 * 删除指定name的person
	 * @param id
	 */
	public void delete(int id){
		SQLiteDatabase db = helper.getReadableDatabase();
		db.delete(TABLE_NAME, ID+"=?", new String[]{id+""});
		surchargesDB.delete(id);
		boxDB.delete(id);
		db.close();
	}
	
	public List<PriceEntity> query(){
		SQLiteDatabase db = helper.getReadableDatabase();
		return getCursor(db.query(TABLE_NAME, null, null, null, null, null, null, null));
	}
	
	public void update(List<PriceEntity> priceEntities,int freightId){
		SQLiteDatabase db = helper.getReadableDatabase();
		for (PriceEntity priceEntity:priceEntities) {
			ContentValues values = new ContentValues();
			values.put(SC_NAME,priceEntity.getSc_name());
			values.put(CLS,priceEntity.getCls());
			values.put(ETD,priceEntity.getEtd());
			values.put(START_PORT,priceEntity.getStartPort());
			values.put(DEST_PORT,priceEntity.getDestPort());
			values.put(START_ID,priceEntity.getStartId());
			values.put(DEST_ID,priceEntity.getDestId());
			values.put(LINE,priceEntity.getLine());
			values.put(TT,priceEntity.getTt());
			values.put(REMARKS,priceEntity.getRemarks());
			values.put(VIA,priceEntity.getVia());
			values.put(SVAL_ID,priceEntity.getSvalid());
			values.put(EVAL_ID,priceEntity.getEvalid());
			values.put(SC_ID,priceEntity.getSc_id());
			db.update(TABLE_NAME, values, ID+"=?", new String[]{priceEntity.getId()+""});
			surchargesDB.update(priceEntity.getSurcharges(),priceEntity.getId());
			boxDB.update(priceEntity.getBoxBeans(),priceEntity.getId());
		}

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
	public List<PriceEntity> query(int currentPage, int size){
		SQLiteDatabase db = helper.getReadableDatabase();
		return getCursor(db.query(TABLE_NAME, null, null, null, null, null,
				ID+" desc", (currentPage - 1) * size + "," + size));
	}
	@NonNull
	private List<PriceEntity> getCursor(Cursor cursor) {
		List<PriceEntity> list = new ArrayList<>();

		while (cursor.moveToNext()){
			PriceEntity priceEntity = new PriceEntity();
			priceEntity.setId(cursor.getInt(cursor.getColumnIndex(ID)));
			priceEntity.setSc_name(cursor.getString(cursor.getColumnIndex(SC_NAME)));
			priceEntity.setCls(cursor.getInt(cursor.getColumnIndex(CLS)));
			priceEntity.setEtd(cursor.getInt(cursor.getColumnIndex(ETD)));
			priceEntity.setStartPort(cursor.getString(cursor.getColumnIndex(START_PORT)));
			priceEntity.setDestPort(cursor.getString(cursor.getColumnIndex(DEST_PORT)));
			priceEntity.setStartId(cursor.getInt(cursor.getColumnIndex(START_ID)));
			priceEntity.setDestId(cursor.getInt(cursor.getColumnIndex(DEST_ID)));
			priceEntity.setLine(cursor.getString(cursor.getColumnIndex(LINE)));
			priceEntity.setTt(cursor.getInt(cursor.getColumnIndex(TT)));
			priceEntity.setRemarks(cursor.getString(cursor.getColumnIndex(REMARKS)));
			priceEntity.setVia(cursor.getInt(cursor.getColumnIndex(VIA)));
			priceEntity.setSvalid(cursor.getInt(cursor.getColumnIndex(SVAL_ID)));
			priceEntity.setEvalid(cursor.getInt(cursor.getColumnIndex(EVAL_ID)));
			priceEntity.setSc_id(cursor.getInt(cursor.getColumnIndex(SC_ID)));
			priceEntity.setSurcharges(surchargesDB.query(priceEntity.getId()));
			priceEntity.setBoxBeans(boxDB.query(priceEntity.getId()));
			list.add(priceEntity);
		}
		return list;
	}

	public void insert(List<PriceEntity> priceEntities,long freightId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		for (PriceEntity priceEntity:priceEntities) {
			ContentValues values = new ContentValues();
			values.put(SC_NAME,priceEntity.getSc_name());
			values.put(CLS,priceEntity.getCls());
			values.put(ETD,priceEntity.getEtd());
			values.put(START_PORT,priceEntity.getStartPort());
			values.put(DEST_PORT,priceEntity.getDestPort());
			values.put(START_ID,priceEntity.getStartId());
			values.put(DEST_ID,priceEntity.getDestId());
			values.put(LINE,priceEntity.getLine());
			values.put(TT,priceEntity.getTt());
			values.put(REMARKS,priceEntity.getRemarks());
			values.put(VIA,priceEntity.getVia());
			values.put(SVAL_ID,priceEntity.getSvalid());
			values.put(EVAL_ID,priceEntity.getEvalid());
			values.put(SC_ID,priceEntity.getSc_id());
			db.update(TABLE_NAME, values, ID+"=?", new String[]{priceEntity.getId()+""});
			surchargesDB.update(priceEntity.getSurcharges(),priceEntity.getId());
			boxDB.update(priceEntity.getBoxBeans(),priceEntity.getId());
		}
		db.close();
	}

//	public PriceEntity query(int id) {
//		SQLiteDatabase db = helper.getReadableDatabase();
//		List<PriceEntity> voyages = getCursor(db.query(TABLE_NAME, null, ID + "=?"
//				, new String[]{id + ""}, null, null, ID));
//		db.close();
//		if(voyages.size()==0){
//			return null;
//		}
//		return voyages.get(0);
//	}

	public List<PriceEntity> query(int scID,int startPortId,int destPortId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<PriceEntity> voyages = getCursor(db.query(TABLE_NAME, null, SC_ID + "=? and "+START_ID
				+"=? and "+DEST_ID+"=?", new String[]{scID + "",startPortId+"",
				destPortId+""}, null, null, ID));
		db.close();
		return voyages;
	}
}
