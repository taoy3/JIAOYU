package com.taoy3.freight.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.taoy3.freight.bean.SurchargesEntity;

import java.util.ArrayList;
import java.util.List;

public class SurchargesDB {
    private static final String TABLE_NAME = "ports";
    private static final String ID = "id";
    private static final String PRICE_ID = "priceId";
    private static final String GP40="gp40";
    private static final String HQ40="hq40";
    private static final String FEE_CODE="fee_code";
    private static final String GP20="gp20";
    private static final String NAME="name";
    private static final String FEE_ID="feeId";
    private static final String PAY_TYPE="payType";
    private static final String USING_TYPE="usingType";
    private static final String CURR="curr";
    private static final String TYPE="type";
    private static final String T_PRICE="tPrice";
    public static final String SQL = "create table " + TABLE_NAME + "(" + ID + " integer key, "
            + PRICE_ID + " integer key, " + NAME + " varchar(20), "+ CURR + " varchar(20), "
            + FEE_CODE + " varchar(20), " + FEE_ID + " integer, " + PAY_TYPE + " integer,"
            + USING_TYPE + " integer," +TYPE+" integer,"+ T_PRICE + " integer," + GP40 + " double,"
            + HQ40 + " double,"+ GP20 + " double)";
    private static ExternalDbHelper helper;
    private static SurchargesDB dbAccess;

    private SurchargesDB() {
        helper = ExternalDbHelper.getInstance();
    }

    public static SurchargesDB getInstance() {
        if (dbAccess == null) {
            synchronized (SurchargesDB.class) {
                if (dbAccess == null) {
                    dbAccess = new SurchargesDB();
                }
            }
        }
        return dbAccess;
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

    public List<SurchargesEntity> query() {
        SQLiteDatabase db = helper.getReadableDatabase();
        return getCursor(db.query(TABLE_NAME, null, null, null, null, null, null, null));
    }
    public void update(List<SurchargesEntity> surchargesEntities,int priceId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        for(SurchargesEntity entity:surchargesEntities){
            ContentValues values = new ContentValues();
            values.put(PRICE_ID, priceId);
            values.put(NAME, entity.getName());
            values.put(CURR, entity.getCurr());
            values.put(FEE_CODE, entity.getFee_code());
            values.put(FEE_ID, entity.getFee_id());
            values.put(PAY_TYPE, entity.getPay_type());
            values.put(USING_TYPE, entity.getUsingType());
            values.put(TYPE, entity.getType());
            values.put(T_PRICE, entity.getTprice());
            values.put(GP40, entity.getGp40());
            values.put(GP20, entity.getGp20());
            values.put(HQ40, entity.getHq40());
            db.update(TABLE_NAME, values, ID + "=? and " + PRICE_ID + "=?"
                    , new String[]{entity.getId() + "", priceId + ""});
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
    public List<SurchargesEntity> query(int currentPage, int size) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return getCursor(db.query(TABLE_NAME, null, null, null, null, null,
                ID + " desc", (currentPage - 1) * size + "," + size));
    }

    @NonNull
    private List<SurchargesEntity> getCursor(Cursor cursor) {
        List<SurchargesEntity> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            SurchargesEntity entity = new SurchargesEntity();
            list.add(entity);
            entity.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            entity.setCurr(cursor.getString(cursor.getColumnIndex(CURR)));
            entity.setFee_code(cursor.getString(cursor.getColumnIndex(FEE_CODE)));
            entity.setFee_id(cursor.getInt(cursor.getColumnIndex(FEE_ID)));
            entity.setPay_type(cursor.getInt(cursor.getColumnIndex(PAY_TYPE)));
            entity.setUsingType(cursor.getInt(cursor.getColumnIndex(USING_TYPE)));
            entity.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
            entity.setTprice(cursor.getInt(cursor.getColumnIndex(T_PRICE)));
            entity.setGp40(cursor.getFloat(cursor.getColumnIndex(GP40)));
            entity.setGp20(cursor.getFloat(cursor.getColumnIndex(GP20)));
            entity.setHq40(cursor.getFloat(cursor.getColumnIndex(HQ40)));
        }
        return list;
    }

    public void insert(List<SurchargesEntity> entities,long priceId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        for (SurchargesEntity entity : entities) {
            ContentValues values = new ContentValues();
            values.put(PRICE_ID, priceId);
            values.put(NAME, entity.getName());
            values.put(CURR, entity.getCurr());
            values.put(FEE_CODE, entity.getFee_code());
            values.put(FEE_ID, entity.getFee_id());
            values.put(PAY_TYPE, entity.getPay_type());
            values.put(USING_TYPE, entity.getUsingType());
            values.put(TYPE, entity.getType());
            values.put(T_PRICE, entity.getTprice());
            values.put(GP40, entity.getGp40());
            values.put(GP20, entity.getGp20());
            values.put(HQ40, entity.getHq40());
            db.insert(TABLE_NAME, null, values);
        }
//        db.close();
    }

    public List<SurchargesEntity> query(int priceId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return getCursor(db.query(TABLE_NAME,null,PRICE_ID+"=?"
                ,new String[]{priceId+""},null,null,ID));
    }

}
