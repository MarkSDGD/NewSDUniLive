package com.xike.xkliveplay.framework.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * @author LiWei <br>
 * CreateTime: 2015年1月14日 上午11:00:40<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 *   <b>Public:</b> <br>
 *   <b>Private:</b> <br>
 */
public class CheckNetStatus extends Thread
{
	private Context context = null;
	private Handler hd = null;
	
	private boolean isStop = false;
	private String[]  ips = {"www.baidu.com","www.163.com","www.qq.com"};
	private int index = 0;
	
	
	public CheckNetStatus(Context context, Handler hd) 
	{
		super();
		System.out.println("--------------------------------------------------afadfafafsdf");
		this.context = context;
		this.hd = hd;
	}

	@Override
	public void run() 
	{
		System.out.println("------ping-----start : -1" );
		while (!isStop) 
		{
			System.out.println("------ping-----start : -0" );
			ping();
			
			try {
				sleep(1000 * 15);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}

	/**
     * @author suncat
     * @category 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     * @return
     */ 
    public  boolean ping() { 
   
        String result = null; 
        try { 
        	System.out.println("------ping-----start : 1" ); 
                String ip = ips[index];// ping 的地址，可以换成任何一种可靠的外网 
                System.out.println("------ping-----start : 2" );
                Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次 
                System.out.println("------ping-----start : 3" );
                // 读取ping的内容，可以不加 
                InputStream input = p.getInputStream(); 
                System.out.println("------ping-----start : 4" );
                BufferedReader in = new BufferedReader(new InputStreamReader(input));
                System.out.println("------ping-----start : 5" );
                StringBuffer stringBuffer = new StringBuffer(); 
                System.out.println("------ping-----start : 6" );
                String content = ""; 
                System.out.println("------ping-----start : 7" );
                while ((content = in.readLine()) != null) 
                {
                	System.out.println("------ping-----start : 8" );
                        stringBuffer.append(content); 
                } 
                System.out.println("------ping-----result content : " + stringBuffer.toString()); 
                if (stringBuffer.toString().equals("")) 
                {
                	hd.sendEmptyMessage(MessageType.MSG_NET_DISCONNECTED);
                	p.destroy();
                	return false;
				}
            
                // ping的状态 
                int status = p.waitFor(); 
                if (status == 0) 
                { 
                    result = "success";
                    index = 0;
                    hd.sendEmptyMessage(MessageType.MSG_NET_CONNECTED);
                    return true; 
                } else { 
                        result = "failed";
                        index++;
                        if (index > 2) 
                        {
                        	index = 0;
                        	hd.sendEmptyMessage(MessageType.MSG_NET_DISCONNECTED);
                        	return false;
						}
                        ping();
                } 
        } catch (IOException e) { 
                result = "IOException"; 
        } catch (InterruptedException e) { 
                result = "InterruptedException"; 
        } finally { 
        	System.out.println("----result---result = " + result); 
        } 
        return false; 
}
}
