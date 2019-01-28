package com.shandong.shandonglive.zengzhi.ui;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xike.xkliveplay.framework.entity.gd.GDOrderPlayAuthCMHWRes;
import com.xike.xkliveplay.framework.tools.AbSharedUtil;
import com.xike.xkliveplay.gd.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * The programer: created by Mernake on 2018/8/1 10:08
 * Main Function:
 */
public class ZZFileTools
{
    private static ZZFileTools instance;

    private static final String DIRECTORY_NAME = "/ZZ";
    private static final String SUFFIX  = "zz";

    private String qrcodepath = "";


    public String getQRCodePath(Context context)
    {
        File dir = new File(context.getCacheDir() + DIRECTORY_NAME);
        //有些盒子不能创建ZZ文件夹，所以这里判断一下，不存在的话，就创建这个ZZ
        if (!dir.exists() || !dir.isDirectory()){
            dir.mkdirs();
        }
        if (qrcodepath.equals(""))qrcodepath = context.getCacheDir() + DIRECTORY_NAME + "/qrcode.jpg";
        System.out.println("目录是：" + qrcodepath);
        return qrcodepath;
    }
    public static ZZFileTools getInstance()
    {
        if (instance == null) instance = new ZZFileTools();
        return instance;
    }

    public void printlnDIR(Context context)
    {
//        System.out.println(Environment.getDataDirectory());
//        System.out.println(Environment.getDownloadCacheDirectory());
//        System.out.println(Environment.getExternalStorageDirectory());
//        System.out.println(Environment.getRootDirectory());
        System.out.println(context.getCacheDir());
//        System.out.println(context.getFilesDir());
//        saveZZData(context,"",null);
    }

    /**
     * 存储频道的playAuth结果
     * @param context
     * @param contentId
     * @param res
     */
    public void saveZZData(Context context,String contentId,GDOrderPlayAuthCMHWRes res)
    {
        File dir = new File(context.getCacheDir() + DIRECTORY_NAME);
        if (!dir.exists() || !dir.isDirectory()){
            dir.mkdirs();
        }

        System.out.println(dir.getPath());
        String fileName = dir.getPath() + "/" + contentId + "." + SUFFIX;
        File file = new File(fileName);
        if (file.exists()) file.delete();
        String str = new Gson().toJson(res);
        System.out.println("res="+str);
        savePackageFile(context,fileName,str);
        setSavedZZTime(context, contentId);

    }
    public void setSavedZZTime(Context context, String contentId){
        String key = "zzsavetime" + contentId;
        Long savedTime = System.currentTimeMillis()/1000;
        AbSharedUtil.putLong(context, key, savedTime);
    }

    public long  getSavedZZTime(Context context, String contentId) {
        String key = "zzsavetime" + contentId;
        return  AbSharedUtil.getLong(context, key);
    }

    public void clearSavedZZTime(Context context, String contentId) {
        String key = "zzsavetime" + contentId;
        AbSharedUtil.removeLong(context,key);

    }

    private void savePackageFile(Context context,String fileName,String content)
    {
        FileOutputStream outputStream = null;
        try {
            File file = new File(fileName);
            outputStream = new FileOutputStream(file);
            byte[] bytes = content.getBytes();
            outputStream.write(bytes);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read some channel playauth result cache
     * @param context
     * @param contentid
     * @return
     */
    public GDOrderPlayAuthCMHWRes queryChannelPlayAuth(Context context, String contentid)
    {
            String fileName = context.getCacheDir() + DIRECTORY_NAME + "/" + contentid + "." + SUFFIX;
            File file = new File(fileName);
            if (file==null || !file.exists()) return null;
        try {
            FileInputStream fis = new FileInputStream(file);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            String str = new String(buffer);
            LogUtils.i("xumin", "queryChannelPlayAuth: " + str);
            System.out.println(str);
            GDOrderPlayAuthCMHWRes res = new Gson().fromJson(str, GDOrderPlayAuthCMHWRes.class);
            return res;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * clear every vip channel's playauth cache
     * @param context
     */
    public void clearZZCache(Context context)
    {
            File dirPath = new File(context.getCacheDir() + DIRECTORY_NAME);
            if (dirPath==null||!dirPath.exists() || !dirPath.isDirectory()) return;
            File[] list = dirPath.listFiles();
            for (File file:list){
                if (file.isFile()&&file.exists()) file.delete();
            }
    }

    /**
     * clear every vip channel's playauth cache
     * @param context
     */
    public void clearZZCache(Context context,String channelId)
    {
        File file = new File(context.getCacheDir() + DIRECTORY_NAME + "/" + channelId + "." + SUFFIX);
        if (file.isFile()){
            file.delete();
        }
        clearSavedZZTime(context, channelId);
    }

    public boolean isTimeToClearCache(Context context, String channelId)
    {
        LogUtils.i("xumin", "isTimeToClearCache channelId: " + channelId);
        long zzlasttime = getSavedZZTime(context, channelId);
        LogUtils.i("xumin", "isTimeToClearCache zzlasttime: " + zzlasttime);
        if (zzlasttime == 0) return false; //如果取出的时间点是0，说明从没存过或者人工置0了，这种情况下，不清除缓存
        long nowtime = System.currentTimeMillis() / 1000;
        LogUtils.i("xumin", "isTimeToClearCache nowtime: " + nowtime);
        LogUtils.i("xumin", "isTimeToClearCache getTIMEOUT_SECONDS: " + getTIMEOUT_SECONDS(context));
        if (nowtime-zzlasttime > getTIMEOUT_SECONDS(context))
        {
            Toast.makeText(context,"删除缓存",Toast.LENGTH_LONG).show();
            clearZZCache(context, channelId);
            return true;
        }
        return false;
    }


    public boolean isTimeToClearCache(Context context)
    {
        long zzlasttime = AbSharedUtil.getInt(context,"zztimeout");
        if (zzlasttime == 0) return false; //如果取出的时间点是0，说明从没存过或者人工置0了，这种情况下，不清除缓存
        long nowtime = System.currentTimeMillis() / 1000;
        if (nowtime-zzlasttime > getTIMEOUT_SECONDS(context))
        {
            Toast.makeText(context,"删除缓存",Toast.LENGTH_LONG).show();
            clearZZCache(context);
            saveTime(context,0);
            return true;
        }
        return false;
    }

    public void saveTime(Context context,int seconds)
    {
        AbSharedUtil.putInt(context,"zztimeout", seconds);
    }


    public void savePayUrls(Context context,String productID,String url,long ms){
        AbSharedUtil.putString(context,productID,url+"###"+ms);
    }
    public String getPayUrls(Context context,String productId)
    {
        System.out.println("Product ID = " + productId);
        String res = AbSharedUtil.getString(context,productId);
        System.out.println("Res = " + res);
        if (res==null) return null;
        String[] strs = res.split("###");
        if (strs.length != 2) return null;
        long time = Long.parseLong(strs[1]);
        System.out.println("time1="+time + " time2=" + System.currentTimeMillis());
        if (System.currentTimeMillis()-time > 260 * 1000)
        {
            System.out.println("二维码图片已经存放超过260秒了，要重新刷新了");
            //二维码图片已经存放超过260秒了，要重新刷新了
            return null;
        }
        return strs[0];
    }

    private int TIMEOUT_SECONDS = -1;

    public int getTIMEOUT_SECONDS(Context context)
    {
        if (TIMEOUT_SECONDS == -1){
            TIMEOUT_SECONDS = AbSharedUtil.getInt(context,"authtime");
        }
        if (TIMEOUT_SECONDS == 0) TIMEOUT_SECONDS = 8 * 3600;
        return TIMEOUT_SECONDS;
    }

    public void setTIMEOUT_SECONDS(Context context,int TIMEOUT_SECONDS)
    {
        AbSharedUtil.putInt(context,"authtime",TIMEOUT_SECONDS);
        this.TIMEOUT_SECONDS = TIMEOUT_SECONDS;
    }
}
