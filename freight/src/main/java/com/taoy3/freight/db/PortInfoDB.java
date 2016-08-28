package com.taoy3.freight.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.taoy3.freight.bean.PortsInfo;

import java.util.ArrayList;
import java.util.List;

public class PortInfoDB {
    private static final String TABLE_NAME = "ports";
    private static final String ID = "id";

    private static final String VOYAGE_ID = "voyageId";
    private static final String PORT_NAME = "port_name";
    private static final String TT = "tt";
    private static final String NO = "no";
    private static final String VIA = "via";

    private static final String CSI = "csi";
    private static final String CLS = "cls";
    private static final String ETA = "eta";
    private static final String ETD = "etd";
    public static final String SQL = "create table " + TABLE_NAME +
            "(" + ID + " integer key, " + VOYAGE_ID + " integer key, " + PORT_NAME
            + " varchar(20), " + TT + " integer, " + NO + " integer," + VIA + " integer,"
            +CSI+" integer,"+ CLS + " integer," + ETA + " integer," + ETD + " integer)";
    private static ExternalDbHelper helper;
    private static PortInfoDB dbAccess;

    private PortInfoDB() {
        helper = ExternalDbHelper.getInstance();
    }

    public static PortInfoDB getInstance() {
        if (dbAccess == null) {
            synchronized (PortInfoDB.class) {
                if (dbAccess == null) {
                    dbAccess = new PortInfoDB();
                }
            }
        }
        return dbAccess;
    }

    public void insert(PortsInfo portsInfo) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, portsInfo.getPort_id());
        values.put(VOYAGE_ID, portsInfo.getVoyageId());
        values.put(PORT_NAME, portsInfo.getPort_name());
        values.put(TT, portsInfo.getTt());
        values.put(NO, portsInfo.getNo());
        values.put(VIA, portsInfo.getVia());
        values.put(CSI, portsInfo.getCsi());
        values.put(CLS, portsInfo.getCls());
        values.put(ETA, portsInfo.getEta());
        values.put(ETD, portsInfo.getEtd());
        db.insert(TABLE_NAME, null, values);
//        db.close();
    }

    /**
     * 删除指定name的person
     *
     * @param voyageId
     */
    public void delete(int voyageId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        db.delete(TABLE_NAME,  VOYAGE_ID + "=?", new String[]{ voyageId + ""});
//        db.close();
    }

    public List<PortsInfo> query() {
        SQLiteDatabase db = helper.getReadableDatabase();
        return getCursor(db.query(TABLE_NAME, null, null, null, null, null, null, null));
    }

    public void update(PortsInfo portsInfo) {
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(PORT_NAME, portsInfo.getPort_name());
        values.put(TT, portsInfo.getTt());
        values.put(NO, portsInfo.getNo());
        values.put(VIA, portsInfo.getVia());
        values.put(CSI, portsInfo.getCsi());
        values.put(CLS, portsInfo.getCls());
        values.put(ETA, portsInfo.getEta());
        values.put(ETD, portsInfo.getEtd());
        db.update(TABLE_NAME, values, ID + "=? and " + VOYAGE_ID + "=?"
                , new String[]{portsInfo.getPort_id() + "", portsInfo.getVoyageId() + ""});
//        db.close();
    }
    public void update(List<PortsInfo> portsInfos) {
        SQLiteDatabase db = helper.getReadableDatabase();
        for(PortsInfo portsInfo:portsInfos){
            ContentValues values = new ContentValues();
            values.put(PORT_NAME, portsInfo.getPort_name());
            values.put(TT, portsInfo.getTt());
            values.put(NO, portsInfo.getNo());
            values.put(VIA, portsInfo.getVia());
            values.put(CSI, portsInfo.getCsi());
            values.put(CLS, portsInfo.getCls());
            values.put(ETA, portsInfo.getEta());
            values.put(ETD, portsInfo.getEtd());
            db.update(TABLE_NAME, values, ID + "=? and " + VOYAGE_ID + "=?"
                    , new String[]{portsInfo.getPort_id() + "", portsInfo.getVoyageId() + ""});
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
    public List<PortsInfo> query(int currentPage, int size) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return getCursor(db.query(TABLE_NAME, null, null, null, null, null,
                ID + " desc", (currentPage - 1) * size + "," + size));
    }

    @NonNull
    private List<PortsInfo> getCursor(Cursor cursor) {
        List<PortsInfo> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            PortsInfo info = new PortsInfo();
            list.add(info);
            info.setPort_name(cursor.getString(cursor.getColumnIndex(PORT_NAME)));
            info.setCls(cursor.getLong(cursor.getColumnIndex(CLS)));
            info.setCsi(cursor.getLong(cursor.getColumnIndex(CSI)));
            info.setEta(cursor.getLong(cursor.getColumnIndex(ETA)));
            info.setEtd(cursor.getLong(cursor.getColumnIndex(ETD)));
            info.setNo(cursor.getInt(cursor.getColumnIndex(NO)));
            info.setPort_id(cursor.getInt(cursor.getColumnIndex(ID)));
            info.setVoyageId(cursor.getInt(cursor.getColumnIndex(VOYAGE_ID)));
            info.setTt(cursor.getInt(cursor.getColumnIndex(TT)));
            info.setVia(cursor.getInt(cursor.getColumnIndex(VIA)));
        }
        return list;
    }

    public void insert(List<PortsInfo> ports,long id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        for (PortsInfo portsInfo : ports) {
            ContentValues values = new ContentValues();
            values.put(ID, portsInfo.getPort_id());
            values.put(VOYAGE_ID, id);
            values.put(PORT_NAME, portsInfo.getPort_name());
            values.put(TT, portsInfo.getTt());
            values.put(NO, portsInfo.getNo());
            values.put(VIA, portsInfo.getVia());
            values.put(CSI, portsInfo.getCsi());
            values.put(CLS, portsInfo.getCls());
            values.put(ETA, portsInfo.getEta());
            values.put(ETD, portsInfo.getEtd());
            db.insert(TABLE_NAME, null, values);
        }
//        db.close();
    }

    public List<PortsInfo> query(int voyageId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return getCursor(db.query(TABLE_NAME,null,VOYAGE_ID+"=?"
                ,new String[]{voyageId+""},null,null,ID));
    }
}
