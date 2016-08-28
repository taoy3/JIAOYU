package com.taoy3.freight.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.taoy3.freight.bean.PriceBean;

import java.util.ArrayList;
import java.util.List;

public class FreightDB {
    private static final String TABLE_NAME = "freight";
    private static final String ID = "id";
    private static final String SC_NAME = "sc_name";
    private static final String SC_ID = "sc_id";
    private static final String START_PORT = "startPort";//起运港
    private static final String DEST_ID = "destId";
    private static final String START_ID="startId";
    private static final String LINE = "line";//航线代码
    public static final String SQL = "create table " + TABLE_NAME + "(" + ID + " integer primary key autoincrement, " +
            START_ID+" integer,"+DEST_ID+" integer,"+ SC_NAME + " varchar(20), " + SC_ID + " varchar(20), "
            + START_PORT + " varchar(20), " + LINE + " integer)";
    private static ExternalDbHelper helper;
    private static FreightDB dbAccess;
    private static PriceDB priceDB;

    private FreightDB() {
        helper = ExternalDbHelper.getInstance();
    }

    public static FreightDB getInstance() {
        if (dbAccess == null) {
            synchronized (FreightDB.class) {
                if (dbAccess == null) {
                    dbAccess = new FreightDB();
                    priceDB = PriceDB.getInstance();
                }
            }
        }
        return dbAccess;
    }

    public void insert(PriceBean priceBean) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(SC_NAME, priceBean.getSc_name());
        values.put(START_PORT, priceBean.getStartPort());
        values.put(LINE, priceBean.getLine());
        values.put(SC_ID, priceBean.getSc_id());
        values.put(START_ID,priceBean.getStartPortId());
        values.put(DEST_ID,priceBean.getDestPortId());
        long id = db.insert(TABLE_NAME, null, values);
        priceDB.insert(priceBean.getItems(), id);
        db.close();
    }

    /**
     * 删除指定name的person
     *
     * @param id
     */
    public void delete(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        db.delete(TABLE_NAME, ID + "=?", new String[]{id + ""});
        priceDB.delete(id);
        db.close();

    }

    public List<PriceBean> query() {
        SQLiteDatabase db = helper.getReadableDatabase();
        return getCursor(db.query(TABLE_NAME, null, null, null, null, null, null, null));
    }

    public void update(PriceBean priceBean) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(SC_NAME, priceBean.getSc_name());
        values.put(START_PORT, priceBean.getStartPort());
        values.put(LINE, priceBean.getLine());
        values.put(SC_ID, priceBean.getSc_id());
        values.put(START_ID,priceBean.getStartPortId());
        values.put(DEST_ID,priceBean.getDestPortId());
        db.update(TABLE_NAME, values, ID + "=?", new String[]{priceBean.getId() + ""});
        priceDB.update(priceBean.getItems(), priceBean.getId());
        db.close();
    }

    /**
     * 根据制定的页数和每页的记录数来查询数据，并返回查询到的cursor
     *
     * @return 查询的cursor
     * <p/>
     * a,
     */
    public List<PriceBean> query(int startId, int destId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return getCursor(db.query(TABLE_NAME, null, START_ID + "=? and "+DEST_ID+"=?"
                , new String[]{startId + "",destId+""}, null, null, ID));
    }

    @NonNull
    private List<PriceBean> getCursor(Cursor cursor) {
        List<PriceBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            PriceBean object = new PriceBean();
            object.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            object.setStartPort(cursor.getString(cursor.getColumnIndex(START_PORT)));
            object.setItems(priceDB.query(object.getSc_id(),object.getStartPortId(),object.getDestPortId()));
            object.setSc_id(cursor.getInt(cursor.getColumnIndex(SC_ID)));
            object.setLine(cursor.getString(cursor.getColumnIndex(LINE)));
            object.setSc_name(cursor.getString(cursor.getColumnIndex(SC_NAME)));
            object.setStartPortId(cursor.getInt(cursor.getColumnIndex(START_ID)));
            object.setDestPortId(cursor.getInt(cursor.getColumnIndex(DEST_ID)));
            list.add(object);
        }
        return list;
    }

    public void insert(List<PriceBean> priceBeans) {
        SQLiteDatabase db = helper.getReadableDatabase();
        for (PriceBean priceBean : priceBeans) {
            ContentValues values = new ContentValues();
            values.put(SC_NAME, priceBean.getSc_name());
            values.put(START_PORT, priceBean.getStartPort());
            values.put(LINE, priceBean.getLine());
            values.put(SC_ID, priceBean.getSc_id());
            values.put(START_ID,priceBean.getStartPortId());
            values.put(DEST_ID,priceBean.getDestPortId());
            long id = db.insert(TABLE_NAME, null, values);

            priceDB.insert(priceBean.getItems(), id);
        }
        db.close();
    }

    public PriceBean query(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<PriceBean> voyages = getCursor(db.query(TABLE_NAME, null, ID + "=?"
                , new String[]{id + ""}, null, null, ID));
        db.close();
        if (voyages.size() == 0) {
            return null;
        }
        return voyages.get(0);
    }

    public List<PriceBean> query(int startPortId, int destPortId, int scID) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<PriceBean> voyages = getCursor(db.query(TABLE_NAME, null, SC_ID + "=? and " + START_ID
                + "=? and " + DEST_ID + "=?", new String[]{scID + "", startPortId + "",
                destPortId + ""}, null, null, ID));
        db.close();
        return voyages;
    }
}
