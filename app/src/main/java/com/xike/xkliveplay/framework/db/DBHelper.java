package com.xike.xkliveplay.framework.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xike.xkliveplay.framework.tools.LogUtil;

public class DBHelper extends SQLiteOpenHelper{
	
	private static final String LOG_TAG = "DBHelper";
	
	private static final String DATABASE_NAME = "newxkliveplay.db";
    private static final int DATABASE_VERSION = 1;
    public static final String NAME_CHANNEL = "channel";
    public static final String NAME_CATEGORY = "category";
    public static final String SQL_CREATE_CHANNEL = "create table channel (id integer primary key autoincrement,contentId text, channelNumber text, name text,callSign text,"
					+ "timeShift text,timeShiftDuration text,description text,country text,state text,city text,zipCode text,playURL text,logoURL text,language text,timeShiftURL text,serialVersionUID text)";
    public static final String SQL_CREATE_CATEGORY = "create table category(cid integer primary key autoincrement,id text,type text,name text,code text,imgurl text,isLeaf text,position text,sequence text,description text,pfile text,pname text,childs text,displaytype text,parentid text,serialVersionUID text)";
    

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	public void onCreate(SQLiteDatabase db) {
		
		db.beginTransaction();
		
        try {
            db.execSQL("create table lastContentChannels (id integer primary key autoincrement , " +
            		"contentId text , channelNumber text , name text, callSign text," +
            		"timeShift text , timeShiftDuration text , description text, country text," +
            		"state text , city text , zipCode text, playURL text," +
            		"logoURL text , language text)");
			db.execSQL(SQL_CREATE_CHANNEL);
			db.execSQL(SQL_CREATE_CATEGORY);
			db.execSQL("create table xunfei (id integer primary key autoincrement , " +
            		"name text , no text)");
        } catch (Exception e) {
            LogUtil.e(LOG_TAG,"onCreate", e.toString());
        }
		
		db.setTransactionSuccessful();
		db.endTransaction();
		
	}

	/*
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion!=newVersion) 
		{
			if (!isTableExists(db, "xunfei", true)) 
			{
				db.execSQL("create table xunfei (id integer primary key autoincrement , " +
	            		"name text , no text)");
			}
		}
	}
	
	public boolean isTableExists(SQLiteDatabase mDatabase,String tableName, boolean openDb) {
	    if(openDb) {
	        if(mDatabase == null || !mDatabase.isOpen()) {
	            mDatabase = getWritableDatabase();
	        }

//	        if(!mDatabase.isReadOnly()) {
//	            mDatabase.close();
//	            mDatabase = getReadableDatabase();
//	        }
	    }

	    Cursor cursor = mDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
	    if(cursor!=null) {
	        if(cursor.getCount()>0) {
	                            cursor.close();
	            return true;
	        }
	                    cursor.close();
	    }
	    return false;
	}

}
