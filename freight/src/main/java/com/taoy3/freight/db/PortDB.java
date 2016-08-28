package com.taoy3.freight.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.taoy3.freight.bean.Port;

import java.util.ArrayList;
import java.util.List;

public class PortDB {
    private final static String TABLE_NAME = "port";

    private final static String NAME_ZH = "name_zh";
    private final static String CODE = "code";
    private final static String PORT_TYPE = "porttype";
    private final static String COUNTRY_NAME = "countryname";
    private final static String ID = "id";
    private final static String STATE = "state";
    private final static String COUNTRY_ID = "countryid";
    private final static String NAME_EN = "name_en";
    private final static String LOCATION_TYPE = "locationtype";
    private String TAG = "PortDbAccess";
    static final String SQL = "create table " + TABLE_NAME
            + "(" + ID + " integer primary key, "
            + NAME_ZH + " varchar(70), "
            + CODE + " varchar(70), "
            + PORT_TYPE + " integer, "
            + COUNTRY_NAME + " varchar(70), "
            + STATE + " integer, "
            + COUNTRY_ID + " varchar(70), "
            + NAME_EN + " varchar(70), "
            + LOCATION_TYPE + " varchar)";
    private static SQLiteOpenHelper helper;
    private static PortDB dbAccess;

    private PortDB() {
        helper = BaseDbHelper.getInstance();
    }

    public static PortDB getInstance() {
        if (dbAccess == null) {
            synchronized (PortDB.class) {
                if (dbAccess == null) {
                    dbAccess = new PortDB();
                }
            }
        }
        return dbAccess;
    }

    public void insert(Port port) {

		/*
		 * 返回一个可读写的数据库，但是当磁盘满或者一些别的原因导致的数据库无法写入的时候，则会抛出异常。
		 
		helper.getWritableDatabase();*/
		
		/*
		 * 获得sqliteDatabase对象
		 * 返回一个可读写的数据库，但是当磁盘满或者一些别的原因导致的数据库无法写入的时候，则返回的是一个只读的数据库。
		 */
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "insert into " + TABLE_NAME + "(" + ID + ", " + NAME_ZH + ", " + CODE + ", " + PORT_TYPE + ", " + COUNTRY_NAME +
                ", " + STATE + ", " + COUNTRY_ID + ", " + NAME_EN + ", " + LOCATION_TYPE + ") values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        db.execSQL(sql, new String[]{port.getId() + "", port.getName_zh(), port.getCode(), port.getPorttype() + ""
                , port.getCountryname(), port.getState() + "", port.getCountryid(), port.getName_en(), port.getLocationtype()});
        db.close();//关闭数据库
    }

    /**
     * 删除指定name的person
     *
     * @param id
     */
    public void delete(int id) {
        String sql = "delete from " + TABLE_NAME + " where " + ID + "=?";
        SQLiteDatabase db = helper.getReadableDatabase();
//		db.beginTransaction();
        db.execSQL(sql, new String[]{id + ""});
//		db.setTransactionSuccessful();
        db.close();
    }

    public List<Port> queryAll() {
        String sql = "select * from " + TABLE_NAME;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        return getCursor(cursor);
    }

    @NonNull
    private List<Port> getCursor(Cursor cursor) {
        List<Port> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Port port = new Port();
            port.setCode(cursor.getString(cursor.getColumnIndex(CODE)));
            port.setCountryid(cursor.getString(cursor.getColumnIndex(COUNTRY_ID)));
            port.setId(cursor.getInt(cursor.getColumnIndex(ID)));

            port.setCountryname(cursor.getString(cursor.getColumnIndex(COUNTRY_NAME)));
            port.setLocationtype(cursor.getString(cursor.getColumnIndex(LOCATION_TYPE)));
            port.setName_en(cursor.getString(cursor.getColumnIndex(NAME_EN)));
            port.setName_zh(cursor.getString(cursor.getColumnIndex(NAME_ZH)));
            port.setPorttype(cursor.getInt(cursor.getColumnIndex(PORT_TYPE)));
            port.setState(cursor.getInt(cursor.getColumnIndex(STATE)));
            list.add(port);
        }
        cursor.close();
        return list;
    }

    public void update(Port port) {
        String sql = "update " + TABLE_NAME + " set " + CODE + "=?, " + COUNTRY_ID + "=?, " + COUNTRY_NAME + "=?, "
                + LOCATION_TYPE + "=?, " + NAME_EN + "=?, " + NAME_ZH + "=?, " + PORT_TYPE + "=?, " + STATE + "=? where " + ID + "=?";
        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL(sql, new String[]{port.getCode(), port.getCountryid(), port.getCountryname()
                , port.getLocationtype(), port.getName_en(), port.getName_zh(), port.getPorttype() + ""
                , port.getState() + "", port.getId() + ""});
        db.close();
    }

    /**
     * 根据制定的页数和每页的记录数来查询数据，并返回查询到的cursor
     *
     * @param currentPage 当前的页数
     * @param size        每页的记录数
     * @return 查询的cursor
     */
    public List<Port> query(int currentPage, int size) {
        String sql = "select * from " + TABLE_NAME + " limit ?, ?";  //
        SQLiteDatabase db = helper.getReadableDatabase();
        //size = 5    0   5  10
        return getCursor(db.rawQuery(sql, new String[]{(currentPage - 1) * size + "", currentPage*size + ""}));
    }

    //	public  List<Port> search(String query) {
//		query=new String("%"+query+"%");
////		String sql= "select * from "+ TABLE_NAME +" where "+NAME_ZH +" like "+query+" or "+NAME_EN+" like "+query+" or "
////				+CODE+" like "+query;
//		String sql= "select * from "+ TABLE_NAME +" where "+NAME_EN+" like ?";
//		Log.i(TAG,sql);
//		SQLiteDatabase db = helper.getReadableDatabase();
//		return getCursor(db.rawQuery(sql,new String[]{query}));
//	}
    public List<Port> search(String query) {
        query = new String("%" + query + "%");
        SQLiteDatabase db = helper.getReadableDatabase();
        String selection = NAME_EN + " like ?";
        return getCursor(db.query(TABLE_NAME, null, selection, new String[]{query}, null, null, ID));
    }

    public void insert(List<Port> ports) {
        Log.i(TAG, "init port db");
        SQLiteDatabase db = helper.getReadableDatabase();
        for (int i = 0; i < ports.size(); i++) {
            Port port = ports.get(i);
            String sql = "insert into " + TABLE_NAME + "(" + ID + ", " + NAME_ZH + ", " + CODE + ", " + PORT_TYPE + ", " + COUNTRY_NAME +
                    ", " + STATE + ", " + COUNTRY_ID + ", " + NAME_EN + ", " + LOCATION_TYPE + ") values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            db.execSQL(sql, new String[]{port.getId() + "", port.getName_zh(), port.getCode(), port.getPorttype() + ""
                    , port.getCountryname(), port.getState() + "", port.getCountryid(), port.getName_en(), port.getLocationtype()});
        }
        db.close();//关闭数据库
    }

    public String queryName(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + NAME_ZH + " from " + TABLE_NAME
                + " where " + ID + "=?", new String[]{id + ""});
        String name = null;
        if(cursor.moveToNext()){
            name = cursor.getString(cursor.getColumnIndex(NAME_ZH));
        }
        return name;
    }

    public Port query(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME
                + " where " + ID + "=?", new String[]{id + ""});
        List<Port> ports = getCursor(cursor);
        if (ports.size() == 0) {
            return null;
        }
        return ports.get(0);
    }

    public int queryIdByName(String name) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + ID + " from " + TABLE_NAME
                + " where " + NAME_ZH + "=?", new String[]{name});
        return cursor.getInt(cursor.getColumnIndex(ID));
    }
}
