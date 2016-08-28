package com.taoy3.freight.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.taoy3.freight.bean.BoxBean;

import java.util.ArrayList;
import java.util.List;

public class BoxDB {
    private static final String TABLE_NAME = "ports";
    private static final String ID = "id";
    private static final String PRICE_ID = "priceId";
    private static final String NAME="name";
    private static final String VALUE="value";
    private static final String NUMBER ="number";
    public static final String SQL = "create table " + TABLE_NAME + "(" + ID + " integer key, "
            + PRICE_ID + " integer key, " + NUMBER + " integer, "+ NAME + " varchar(20), "
            + VALUE + " double)";
    private static ExternalDbHelper helper;
    private static BoxDB dbAccess;

    private BoxDB() {
        helper = ExternalDbHelper.getInstance();
    }

    public static BoxDB getInstance() {
        if (dbAccess == null) {
            synchronized (BoxDB.class) {
                if (dbAccess == null) {
                    dbAccess = new BoxDB();
                }
            }
        }
        return dbAccess;
    }

    public void insert(BoxBean bean,int priceId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRICE_ID, priceId);
        values.put(NAME, bean.getName());
        values.put(VALUE, bean.getValue());
        values.put(NUMBER,bean.getNumber());
        db.insert(TABLE_NAME, null, values);
//        db.close();
    }

    /**
     * 删除指定name的person
     *
     * @param priceId
     */
    public void delete(int priceId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        db.delete(TABLE_NAME,  PRICE_ID + "=?", new String[]{ priceId + ""});
//        db.close();
    }

    public List<BoxBean> query() {
        SQLiteDatabase db = helper.getReadableDatabase();
        return getCursor(db.query(TABLE_NAME, null, null, null, null, null, null, null));
    }

    public void update(BoxBean bean,int priceId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRICE_ID, priceId);
        values.put(NAME, bean.getName());
        values.put(VALUE, bean.getValue());
        values.put(NUMBER,bean.getNumber());
        db.update(TABLE_NAME, values, ID + "=? and " + PRICE_ID + "=?"
                , new String[]{bean.getId() + "", priceId + ""});
//        db.close();
    }
    public void update(List<BoxBean> beans,int priceId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        for(BoxBean bean:beans){
            ContentValues values = new ContentValues();
            values.put(NAME, bean.getName());
            values.put(VALUE, bean.getValue());
            values.put(NUMBER,bean.getNumber());
            db.update(TABLE_NAME, values, ID + "=? and " + PRICE_ID + "=?"
                    , new String[]{bean.getId() + "", priceId + ""});
        }
//        db.close();
    }

    /**
     * 根据制定的页数和每页的记录数来查询数据，并返回查询到的cursor
     *
     * @param currentPage 当前的页数
     * @param size        每页的记录数
     * @return 查询的cursor
     * <p>
     * a,
     */
    public List<BoxBean> query(int currentPage, int size) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return getCursor(db.query(TABLE_NAME, null, null, null, null, null,
                ID + " desc", (currentPage - 1) * size + "," + size));
    }

    @NonNull
    private List<BoxBean> getCursor(Cursor cursor) {
        List<BoxBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            BoxBean info = new BoxBean();
            list.add(info);
            info.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            info.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            info.setValue((float) cursor.getDouble(cursor.getColumnIndex(VALUE)));
            info.setNumber(cursor.getInt(cursor.getColumnIndex(NUMBER)));
        }
        return list;
    }

    public void insert(List<BoxBean> beens,long priceId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        for (BoxBean bean : beens) {
            ContentValues values = new ContentValues();
            values.put(PRICE_ID, priceId);
            values.put(NAME, bean.getName());
            values.put(VALUE, bean.getValue());
            values.put(NUMBER,bean.getNumber());
            db.insert(TABLE_NAME, null, values);
        }
//        db.close();
    }

    public List<BoxBean> query(int priceId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return getCursor(db.query(TABLE_NAME,null,PRICE_ID+"=?"
                ,new String[]{priceId+""},null,null,ID));
    }
}
