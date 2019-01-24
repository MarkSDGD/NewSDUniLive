package com.xike.xkliveplay.framework.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xike.xkliveplay.framework.entity.ContentChannel;
import com.xike.xkliveplay.framework.tools.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DBManager {
	
	private static final String LOG_TAG = "DBManager";
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Context context) {  
        helper = new DBHelper(context);  
        db = helper.getWritableDatabase();  
    } 
	
	private ContentValues genContentValues(ContentChannel emoji) {
        ContentValues cv = new ContentValues();
        cv.put("contentId", emoji.getContentId());
        cv.put("channelNumber", emoji.getChannelNumber());
        cv.put("name", emoji.getName());
        cv.put("callSign", emoji.getCallSign());
        cv.put("timeShift", emoji.getTimeShift());
        cv.put("timeShiftDuration", emoji.getTimeShiftDuration());
        cv.put("description", emoji.getDescription());
        cv.put("country", emoji.getCountry());
        cv.put("state", emoji.getState());
        cv.put("city", emoji.getCity());
        cv.put("zipCode", emoji.getZipCode());
        cv.put("playURL", emoji.getPlayURL());
        cv.put("logoURL", emoji.getLogoURL());
        cv.put("language", emoji.getLanguage());
                
        return cv;
    }
	
    public void addLastContentChannel(ContentChannel emoji){
		ContentValues values = genContentValues(emoji);;
		db.insert("lastContentChannels", null, values);
		LogUtil.i(LOG_TAG, "addLastContentChannel","insert ok...");
    }
    
    public int add(String tableName, Object obj){
    	try{
    		if(obj == null){
    			return 0;
    		}
//    		LogUtil.i(LOG_TAG, "add",obj.getClass().getName());
			ContentValues values = convertObjectToContentValues(obj);
			db.insert(tableName, null, values);
			LogUtil.i(LOG_TAG, "add","insert ok...");
			
    	}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
    	return 1;
    }
	
    public void addMulti(String tableName, List<Object> objs){   	
    	try{
    		if(objs == null || objs.size()==0){
    			return;
    		}
    		for (Object object : objs) {
				ContentValues values = convertObjectToContentValues(object);
				db.insert(tableName, null, values);
			}

    	}catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void insert(String tableName, Object obj){
    	try{
    		if(obj == null){
    			return;
    		}
			ContentValues values = convertObjectToContentValues(obj);
			db.insert(tableName, null, values);
    	}catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public boolean tabIsExist(String tabName){  
        boolean result = false;  
        if(tabName == null){  
                return false;  
        }  
        Cursor cursor = null;  
        try {  
                String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";  
                cursor = db.rawQuery(sql, null);  
                if(cursor.moveToNext()){  
                        int count = cursor.getInt(0);  
                        if(count>0){  
                                result = true;  
                        }  
                }  
                  
        } catch (Exception e) 
        {
        	 
        }                  
        return result;  
} 
    
    /**
     * 删除全部
     */
    public int deleteAll(String tableName)
    {
    	if (tabIsExist(tableName)) 
    	{
    		return db.delete(tableName, null, null);
		}
		return 0;
    }
    
    
    public void dropTable(String tableName)
    {
		db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }
    
    public int delete(String tableName, Object whereObj) {
        if (whereObj == null) {
            return 0;
        }

        String where;
        int count = 0;
        try {
            where = generateWhereSql(whereObj);
            count = db.delete(tableName, where, null);
            return count;
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

        return count;
    }
    
    
    public List<?> queryAll(String tableName, Class<?> cls, String orderBy) {
        return get(tableName, cls, null, null, null, orderBy);
    }
    
    public List<?> query(String tableName, Class<?> cls, String[] projection,
            Object whereObj, String orderBy) {

        try {
            String where = generateWhereSql(whereObj);
            LogUtil.i(LOG_TAG, "query","select " + toString(projection) + " from " + cls.getSimpleName()
                    + " where " + where);
            return get(tableName, cls, projection, where, null, orderBy);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }
      
    public List<?> queryBySQL(String tableName, Class<?> cls, String sql) {
        List<Object> objs = new ArrayList<Object>();
        try {
            Cursor cursor = db.query(tableName, null, "sql://" + sql, null,null,null,null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Object obj = convertCursorToObject(cursor, cls);
                    if (obj == null) {
                        continue;
                    }
                    objs.add(obj);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        return objs;
    }
    
    public int update(String tableName, Object obj, Object whereObj) {
        if (obj == null) {
            return 0;
        }

        ContentValues values;
        int count = 0;
        try {
            values = convertObjectToContentValues(obj);
            String where = generateWhereSql(whereObj);
            count = db.update(tableName, values, where, null);
        } catch (IllegalArgumentException e) {
        	 return 0;
        } catch (IllegalAccessException e) {
        	 return 0;
        } catch (Exception e) {
			e.printStackTrace();
			 return 0;
		}

        return count;
    }
    
    public int getCount(String tableName, Class<?> cls){
        int count = 0;
        Cursor cursor =db.query(tableName, null, "sql://select count(*) from " + cls.getSimpleName(), null, null, null, null);
        if (null != cursor){
           count = cursor.getCount();
           cursor.close();
        }
        return count;
    }
      
    /** 
     * close database 
     */  
    public void closeDB() {  
        try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }  	
    
    @Override
	protected void finalize() throws Throwable {
    	super.finalize();
    	if (db != null && db.isOpen()) {
			db.close();
		}
		
	}

    public List<Object> get(String tableName, Class<?> cls, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        String[] columns = projection;
        if (columns == null || columns.length == 0) {
            columns = generateQueryCols(cls);
        }

        List<Object> objs = new ArrayList<Object>();
        try {
            Cursor cursor = db.query(tableName, columns, selection,
                    selectionArgs, null, null, sortOrder);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Object obj = convertCursorToObject(cursor, cls);
                    if (obj == null) {
                        continue;
                    }
                    objs.add(obj);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        return objs;
    }
    
    /**
     * 
     */
    static String toString(String[] arr) {
        if (arr == null) {
            return "*";
        }
        StringBuffer sb = new StringBuffer();
        for (String str : arr) {
            sb.append(str).append(",");
        }
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }
    
    public static String[] generateQueryCols(Class<?> cls) {
        Field[] fields = cls.getDeclaredFields();
        String[] projection = new String[fields.length];
        int index = 0;
        for (Field field : fields) {
            projection[index] = field.getName();
            index++;
        }
        return projection;
    }
    
    public static ContentValues convertObjectToContentValues(Object obj)
            throws Exception {
        if (obj == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        Class<?> cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            String key = field.getName();

            
            if (type == Integer.class || type==int.class){
                Object temp = field.get(obj);
                if (temp != null) {
                    Integer value = (Integer)temp;
                    values.put(key, value);
                }
            } else if (type == String.class) {
                Object temp = field.get(obj);
                String value = temp == null ? "" : (String)temp;
                values.put(key, value);
            } else if (type == Float.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Float value = (Float)temp;
                    values.put(key, value);
                }
            } else if (type == Double.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Double value = (Double)temp;
                    values.put(key, value);
                }
            } else if (type == Short.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Short value = (Short)temp;
                    values.put(key, value);
                }
            } else if (type == Boolean.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Boolean value = (Boolean)temp;
                    values.put(key, value);
                }
            } else if (type == Byte.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Byte value = (Byte)temp;
                    values.put(key, value);
                }
            } else if (type == Byte[].class) {
                Object temp = field.get(obj);
                byte[] value = temp == null ? null : (byte[])temp;
                values.put(key, value);
            } else if (type == Date.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Date tempValue = (Date)temp;
                    long value = tempValue.getTime();
                    values.put(key, value);
                }
            }
        }

        return values;
    }
    
    public static String generateWhereSql(Object obj) throws IllegalArgumentException,
            IllegalAccessException {
        if (obj == null) {
            return null;
        }
        Class<?> cls = obj.getClass();
        StringBuffer where = new StringBuffer();
        Field[] fields = cls.getDeclaredFields();
        
        String and = " and ";
        String eq = "=";
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            String key = field.getName();

            if (type == Integer.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Integer value = (Integer)temp;
                    where.append(and).append(key).append(eq).append(value);
                }
            } else if (type == String.class) {
                Object temp = field.get(obj);
                String value = temp == null ? "" : (String)temp;
                if (value != null && !"".equals(value)) {
                    where.append(and + key + "='" + value + "'");
                }
            } else if (type == Float.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Float value = (Float)temp;
                    where.append(and).append(key).append(eq).append(value);
                }
            } else if (type == Double.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Double value = (Double)temp;
                    where.append(and).append(key).append(eq).append(value);
                }
            } else if (type == Short.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Short value = (Short)temp;
                    where.append(and).append(key).append(eq).append(value);
                }
            } else if (type == Boolean.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Boolean tempValue = (Boolean)temp;
                    int value = 0;
                    if (tempValue) {
                        value = 1;
                    }
                    where.append(and).append(key).append(eq).append(value);
                }
            } else if (type == Byte.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Byte value = (Byte)temp;
                    where.append(and).append(key).append(eq).append(value);
                }
            } else if (type == Date.class) {
                Object temp = field.get(obj);
                if (temp != null) {
                    Date tempValue = (Date)temp;
                    long value = tempValue.getTime();
                    where.append(and).append(key).append(eq).append(value);
                }
            }
        }
        String where2 = where.toString();
        if (where2.startsWith(" and")) {
            where2 = where2.substring(" and".length());
        }
        return where2;
    }
    
    /**
     * <数据结果集转换为相应的对�? <方便操作,即操作对象即�?将�?都赋到对象的属�?�?
     * 
     * @param cursor 结果集游�?
     * @param cls 要查询返回的类对�?
     * @throws IllegalArgumentException
     * @throws IllegalAccessException [参数说明]
     * @return Object [返回赋�?后的对象结果]
     * @exception throws [违例类型] [违例说明]
     */
    public static Object convertCursorToObject(Cursor cursor, Class<?> cls)
            throws IllegalArgumentException, IllegalAccessException {
        Object obj = null;
        try {
            obj = cls.newInstance();
        } catch (InstantiationException e) {
            return null;
        }
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();// 属�?类型
            String colName = field.getName();// 属�?�?
            int index = cursor.getColumnIndex(colName);// 列索�?
            if (index < 0){
                continue;
            }
            if (type == Integer.class) {
                int colValue = cursor.getInt(index);
                field.set(obj, colValue);
            } else if (type == int.class) {
                int colValue = cursor.getInt(index);
                field.set(obj, colValue);
            }else if (type == String.class) {
                String colValue = cursor.getString(index);
                field.set(obj, colValue);
            } else if (type == Float.class) {
                float colValue = cursor.getFloat(index);
                field.set(obj, colValue);
            } else if (type == Long.class) {
                long colValue = cursor.getLong(index);
                field.set(obj, colValue);
            } else if (type == Double.class) {
                double colValue = cursor.getDouble(index);
                field.set(obj, colValue);
            } else if (type == Byte.class) {
                int colValue = cursor.getInt(index);
                byte value = Byte.parseByte(colValue + "");
                field.set(obj, value);
            } else if (type == Byte[].class) {
                byte[] colValue = cursor.getBlob(index);
                field.set(obj, colValue);
            } else if (type == Short.class) {
                short colValue = cursor.getShort(index);
                field.set(obj, colValue);
            } else if (type == Boolean.class) {
                int colValue = cursor.getInt(index);
                Boolean value = false;
                if (colValue == 1) {
                    value = true;
                }
                field.set(obj, value);
            } else if (type == Date.class) {
                long tempValue = cursor.getLong(index);
                Date value = new Date(tempValue);
                field.set(obj, value);
            }
        }
        return obj;
    }

	/**
	 * function: 
	 * @param
	 * @return
	 */
	public void createTable() 
	{
		db.execSQL(DBHelper.SQL_CREATE_CHANNEL);
		db.execSQL(DBHelper.SQL_CREATE_CATEGORY);
	}
 
    public boolean isDBOpen()
    {
    	if (db != null) 
    	{
    		return db.isOpen();
		}
    	return false;
    }
    
}
