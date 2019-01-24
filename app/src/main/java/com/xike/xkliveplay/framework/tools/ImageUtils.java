package com.xike.xkliveplay.framework.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by wangfangxu on 2017/8/30.
 * 图片加载工具类，二级缓存
 */

public class ImageUtils {
    private static String cache_path = Environment.getExternalStorageDirectory() + "/shandong/live/image/";
    private static long lastErrorTime = 0;
    private static long nowTime;
    private static long saveBackImageTime = 0;
    private static String backImagePath = "";
    private static String[] backImage;
    private static Handler handler = new Handler();

    //加载图片
    public static void setImage(final String url, final Context context, final ImageView imageView, final int placeholder, String className) {
        if (imageView == null) {
            return;
        }
        //默认图片
        String imageName;
        nowTime = System.currentTimeMillis();
        Log.e("shandongLive", "ImageUtils," + "setImage: " + nowTime + "," + lastErrorTime);
        if (nowTime - lastErrorTime > 60000) {//如果与上一次Glide异常超过一分钟，就尝试再使用Glide
            if (url != null && (!url.equals(""))) {
                @SuppressLint("HandlerLeak")
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 1) {
                            if (imageView.getDrawable() == null) {
                                //网络加载
                                Log.e("shandongLive", "ImageUtils," + "onBindViewHolder: 本地加载失败，网络加载2:" + url);
                                lastErrorTime = System.currentTimeMillis();//异常发生的时间
                                Log.e("shandongLive", "ImageUtils," + "handleMessage: 本地加载失败后，Glide加载框架异常");
//                                MainActivity.getInstances().sharedImageLoader().displayImage(url, imageView);
                            } else {
                                Log.e("shandongLive", "ImageUtils," + "onBindViewHolder: 加载成功");
                            }
                        }
                    }
                };
                imageName = (String) url.subSequence(url.lastIndexOf("/") + 1, url.length());
                if (imageName != null && (!imageName.equals("")) && isLocalPic(imageName)) {//本地含有
                    //本地取
                    Log.e("shandongLive", "ImageUtils," + "onBindViewHolder: 本地加载" + cache_path + imageName);
                    try {
                        //尝试不通过glide加载
//                        File f = new File(cache_path + imageName);
//                        imageView.setImageURI(Uri.fromFile(f));

                        Glide.with(context)
                                .load(cache_path + imageName)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .placeholder(placeholder)
                                .error(placeholder)
                                .dontAnimate()
                                .into(imageView);

                    } catch (Exception e) {
                        Log.e("shandongLive", "ImageUtils," + "onBindViewHolder: Glide异常捕获");
                    }
                } else {
                    //网络加载
                    Log.e("shandongLive", "ImageUtils," + "onBindViewHolder: 网络加载" + url);
                    Glide.with(context)
                            .load(url)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(placeholder)
                            .into(imageView);
                    ImageUtils.getFromNet(url, context, className);
                }
                handler.sendEmptyMessageDelayed(1, 1000);
            } else {
                Log.e("shandongLive", "ImageUtils," + "setImage: 获取图片地址为空");
                Glide.with(context)
                        .load(placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(placeholder)
                        .into(imageView);
            }
        } else {//启动异常处理模式
            Log.e("shandongLive", "ImageUtils," + "setImage: Glide加载异常处理模式");
            if (url != null && (!url.trim().equals(""))) {
                imageView.setImageResource(0);
//                BaseApplication.getInstances().sharedImageLoader().displayImage(url, imageView);
            } else {
                Log.e("shandongLive", "ImageUtils," + "setImage: 获取图片地址为空");
                imageView.setImageResource(placeholder);
            }
        }
    }

    //网络获取
    public static void getFromNet(final String url, final Context context, String className) {
        if (url != null && !isLocalPic((String) url.subSequence(url.lastIndexOf("/") + 1, url.length()))) {//如果本地没有
            ImageDownloadTask imageDownloadTask = new ImageDownloadTask(context, className);
            imageDownloadTask.execute(url);
        } else if (url != null && isLocalPic((String) url.subSequence(url.lastIndexOf("/") + 1, url.length()))) {//如果本地有
            ImageDownloadTask imageDownloadTask = new ImageDownloadTask(context, className);
            imageDownloadTask.execute(url);
        }
    }

    //写入本地
    public static boolean writeLocal(Bitmap bitmap, String url) {
        //缓存文件夹
        Boolean flag = false;
        final File cacheFile = new File(cache_path);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        // 缓存文件
        File file = new File(cacheFile, url.subSequence(url.lastIndexOf("/") + 1, url.length()) + "_1");
        try {
            // 写入图片  compress为耗时操作
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    new FileOutputStream(file));
            flag = true;
            if (file.length() == 0) {
                //图片错误
                file.delete();//删除错误图片
                Log.e("shandongLive", "ImageUtils," + "setImage: 删除错误图片");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (int i = 0; i < cacheFile.listFiles().length; i++) {
                            if (i < cacheFile.listFiles().length) {
                                cacheFile.listFiles()[i].delete();
                            }
                        }
                    }
                }.start();
                flag = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return flag;
    }

    //判断本地有没有此图片
    public static boolean isLocalPic(String name) {
        File cacheFile = new File(cache_path);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        // 缓存文件
        File file = new File(cacheFile, name);
        if (file.exists() && file.length() != 0) {
            return true;
        } else if (file.exists() && file.length() == 0) {
            file.delete();
            return false;
        } else {
            return false;
        }
    }

    // 获取SD卡缓存图片文件总大小
    public static long getSDCacheSize() {
        long size = 0;
        try {
            //取得SD卡缓存图片的文件路径
            File file = new File(Environment.getExternalStorageDirectory() + "/shandong/live/image/");
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[1].exists()) {
                            size = size + files[1].length();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 图片下载缓存任务
     */
    public static class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {
        private Context context;
        private String className;

        public ImageDownloadTask(Context context, String className) {
            this.context = context;
            this.className = className;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.e("shandongLive", "ImageUtils,doInBackground try to download");
            String url = params[0];
            Log.e("shandongLive", "ImageUtils,doInBackground try to download url:"+url);
            try {
                if (ActivityState.isActivityRunning(context, className)) {
                    Bitmap myBitmap = Glide.with(context)
                            .load(url)
                            .asBitmap() //必须
                            .centerCrop()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    if (myBitmap != null && !myBitmap.isRecycled()) {
                        //写入本地
                        Boolean flag = writeLocal(myBitmap, url);
                        if (flag) {
                            File cacheFile = new File(cache_path);
                            if (!cacheFile.exists()) {
                                cacheFile.mkdirs();
                            }
                            File file = new File(cacheFile, url.subSequence(url.lastIndexOf("/") + 1, url.length()) + "_1");
                            file.renameTo(new File(cacheFile, (String) url.subSequence(url.lastIndexOf("/") + 1, url.length())));
                        }
                        return myBitmap;
                    }
                } else {
                    Log.e("shandongLive", "ImageUtils,getFromNet: 终止缓存");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.e("shandongLive", "ImageUtils,getFromNet: download finish");
            super.onPostExecute(bitmap);
            final Bitmap[] bitmaps = new Bitmap[]{bitmap};
            //onPostExecute works on main thread
            new Thread(){
                @Override
                public void run() {
                    if(bitmaps[0]!=null && !bitmaps[0].isRecycled()){
                        Log.e("shandongLive", "ImageUtils,getFromNet: download finish, try to recycle bitmap");
                        bitmaps[0].recycle();
                        bitmaps[0] = null;
                    }

                }
            }.start();
        }
    }

}
