package com.taintflow.TaintFlowDataBaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.taintflow.Service.Main_activityService;
import com.taintflow.Service.MyTaintDroidNotifyService;

public class TaintInfo extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "Taint_Info";
	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "Taint_Info_table";
	private static SQLiteDatabase db;

	public TaintInfo(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		if (db == null) {
			db = this.getWritableDatabase();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + TABLE_NAME + "("
				+ MyTaintDroidNotifyService.KEY_APPNAME + " varchar,"
				+ MyTaintDroidNotifyService.KEY_IPADDRESS + " varchar,"
				+ MyTaintDroidNotifyService.KEY_TAINT + " varchar,"
				+ MyTaintDroidNotifyService.KEY_DATA + " varchar,"
				+ MyTaintDroidNotifyService.KEY_TIMESTAMP + " varchar,"
				+ MyTaintDroidNotifyService.KEY_ID + " varchar)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	

	public Cursor select_package_name_Taint(String package_name, String taint) {
		synchronized (this) {
			Cursor cursor = db.query(TABLE_NAME, null, 
						MyTaintDroidNotifyService.KEY_APPNAME + " like ? and "+
						MyTaintDroidNotifyService.KEY_TAINT + "=? and "+
						MyTaintDroidNotifyService.KEY_TIMESTAMP +" like ?",
						new String[] {"%"+package_name+"%", taint ,
					    "%"+Main_activityService.dayString+"%"},
						MyTaintDroidNotifyService.KEY_ID,
						null,MyTaintDroidNotifyService.KEY_TIMESTAMP);
		return cursor;
		}
		
		
		/*Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "
				+ MyTaintDroidNotifyService.KEY_APPNAME + " like ? and "
				+ MyTaintDroidNotifyService.KEY_TAINT + "=?", new String[] {
				"%"+package_name+"%", taint });
		return cursor;*/
	}
	public Cursor select_package_name(String package_Name) {
		synchronized (this) {
			Cursor cursor = db.query(TABLE_NAME, null, 
				MyTaintDroidNotifyService.KEY_APPNAME + " like ? and "+
				MyTaintDroidNotifyService.KEY_TIMESTAMP +" like ?",
				new String[] {"%"+package_Name+"%" ,
					"%"+Main_activityService.dayString+"%"},
				MyTaintDroidNotifyService.KEY_ID,
				null, null);
		return cursor;
		}
		

		/*Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "
				+ MyTaintDroidNotifyService.KEY_APPNAME + " like ?",
				new String[] { "%"+package_Name+"%" });
		return cursor;*/
	}

	public Cursor select_taint(String taint) {
		synchronized (this) {
			Cursor cursor = db.query(TABLE_NAME, null, 
				MyTaintDroidNotifyService.KEY_TAINT + "=? and "+
						MyTaintDroidNotifyService.KEY_TIMESTAMP +" like ?",
				new String[] {taint ,
					"%"+Main_activityService.dayString+"%"},
				MyTaintDroidNotifyService.KEY_ID,
				null, null);
		return cursor;
		}
		
		

		/*Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "
				+ MyTaintDroidNotifyService.KEY_TAINT + "=?",
				new String[] { taint });
		return cursor;*/
	}
	
	public Cursor select_time(String time) {
		synchronized (this) {
			Cursor cursor = db.query(TABLE_NAME, null, 
				MyTaintDroidNotifyService.KEY_TIMESTAMP + " like ?",
				new String[] {"%"+ time+"%" },
				MyTaintDroidNotifyService.KEY_ID,
				null, null);
		return cursor;
		}
		

		/*Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "
				+ MyTaintDroidNotifyService.KEY_TIMESTAMP + " like ?",
				new String[] {"%"+ time+"%" });
		return cursor;*/
	}

	// 增加操作
	public  long insert(Bundle bundle) {
		
		if (bundle == null) {
			return  -1;
		}
		
		/*Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where "
				+ MyTaintDroidNotifyService.KEY_TIMESTAMP + "=? and "
				+ MyTaintDroidNotifyService.KEY_APPNAME + "=? and "
				+ MyTaintDroidNotifyService.KEY_TAINT + "=?",
				new String[] {
				bundle.getString(MyTaintDroidNotifyService.KEY_TIMESTAMP),
				bundle.getString(MyTaintDroidNotifyService.KEY_APPNAME)
				,bundle.getString(MyTaintDroidNotifyService.KEY_TAINT)});
		if (cursor.moveToNext()) {
			return -1;
		}*/
		
		/* ContentValues */
		ContentValues cv = new ContentValues();

		cv.put(MyTaintDroidNotifyService.KEY_APPNAME,
				bundle.getString(MyTaintDroidNotifyService.KEY_APPNAME));

		cv.put(MyTaintDroidNotifyService.KEY_TAINT,
				bundle.getString(MyTaintDroidNotifyService.KEY_TAINT));

		cv.put(MyTaintDroidNotifyService.KEY_DATA,
				bundle.getString(MyTaintDroidNotifyService.KEY_DATA));

		cv.put(MyTaintDroidNotifyService.KEY_TIMESTAMP,
				bundle.getString(MyTaintDroidNotifyService.KEY_TIMESTAMP));

		cv.put(MyTaintDroidNotifyService.KEY_IPADDRESS,
				bundle.getString(MyTaintDroidNotifyService.KEY_IPADDRESS));
		cv.put(MyTaintDroidNotifyService.KEY_ID,
				bundle.getString(MyTaintDroidNotifyService.KEY_ID));
		

		//SQLiteDatabase db = getWritableDatabase();
		synchronized (this) {
			long row = db.insert(TABLE_NAME, null, cv);
			return row;
		}
		
		//db.close();
		
	}

	// 删除操作
	public void delete(int id) {
		/*
		 * SQLiteDatabase db = this.getWritableDatabase(); String where =
		 * BOOK_ID + " = ?"; String[] whereValue = { Integer.toString(id) };
		 * db.delete(TABLE_NAME, where, whereValue);
		 */
	}

	// 修改操作
	public void update(int id, String bookname, String author) {
		/*
		 * SQLiteDatabase db = this.getWritableDatabase(); String where =
		 * BOOK_ID + " = ?"; String[] whereValue = { Integer.toString(id) };
		 * 
		 * ContentValues cv = new ContentValues(); cv.put(BOOK_NAME, bookname);
		 * cv.put(BOOK_AUTHOR, author); db.update(TABLE_NAME, cv, where,
		 * whereValue);
		 */
	}
	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
	}
	
	public void closeDatabase(){
		db.close();
	}

	

}
