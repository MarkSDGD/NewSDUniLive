/**
 * @Title: XunfeiProvider.java
 * @Package com.xike.xkliveplay.xunfei
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2017年3月13日 下午4:55:07
 * @version V1.0
 */
package com.xike.xkliveplay.xunfei;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.xike.xkliveplay.framework.db.DBHelper;

/**
  * @ClassName: XunfeiProvider
  * @Description: TODO
  * @author Mernake
  * @date 2017年3月13日 下午4:55:07
  *
  */
public class XunfeiProvider extends ContentProvider
{
	public static final String tag = "XunFeiVoice";
	private static final boolean isLogON = true;
	
	private DBHelper mDBHelper = null;
	
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int ALLCHANNELS = 99;
	private static final int UNICHANNEL = 100;
	private static final String path = "livechannels";
	private static final String uniPath = "livechannels/{no}";
	public static final String CODE = "sdmgd";
	private static String strURI = "com.iflytek.xiri.provider."+CODE;
	
	private static final String MIME_ALLCHANNELS = "vnd.android.cursor.dir/vnd.com.iflytek.xiri.provider."+CODE+".livechannels";
	private static final String MIME_UNICHANNEL = "vnd.android.cursor.item/vnd.com.iflytek.xiri.provider."+CODE+".livechannels";
	static
	{
		matcher.addURI(strURI, path, ALLCHANNELS);
		matcher.addURI(strURI, uniPath, UNICHANNEL);
	}
	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) 
	{
		return 0;
	}

	@Override
	public String getType(Uri uri) 
	{
		switch (matcher.match(uri)) 
		{
		case ALLCHANNELS:
			return MIME_ALLCHANNELS;
		case UNICHANNEL:
			return MIME_UNICHANNEL;
		default:
			showLog("unknown uri is "+uri.toString(), "query");
			break;
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) 
	{
		return null;
	}

	@Override
	public boolean onCreate() 
	{
		showLog("XunfeiContentProvider is created!", "onCreate()");
		mDBHelper = new DBHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) 
	{
		showLog("Start xunfei channels query!", "query");
		SQLiteDatabase db = mDBHelper.getWritableDatabase();
		
		switch (matcher.match(uri)) 
		{
		case ALLCHANNELS:
		case UNICHANNEL:
			showLog("Searching xunfei database!!!", "query");
			return db.query("xunfei", projection, selection, selectionArgs, null, null, sortOrder);
		default:
			showLog("unknown uri is "+uri.toString(), "query");
			break;
		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) 
	{
		return 0;
	}
	
	
	private void showLog(String content,String method)
	{
		if (isLogON) 
		{
			System.out.println(tag+"::"+method+"::"+content);
		}
	}
}
