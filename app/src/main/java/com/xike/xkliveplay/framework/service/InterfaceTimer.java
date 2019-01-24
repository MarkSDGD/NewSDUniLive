/**
 * @Title: InterfaceTimer.java
 * @Package com.xike.xkliveplay.framework.service
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2016年5月31日 下午1:55:22
 * @version V1.0
 */
package com.xike.xkliveplay.framework.service;

import android.os.Handler;

/**
 * @ClassName: InterfaceTimer
 * @Description: 接口计时器
 * @author Mernake
 * @date 2016年5月31日 下午1:55:22
 *
 */
public class InterfaceTimer 
{
	public static int INTERFACE_TIME_OUT = 1000 * 15;
	private static InterfaceTimer timer = null;
	private IRequestTimeout iRequestTimeout = null;
	
	private Handler handler = new Handler();
	
	public static InterfaceTimer getInstance()
	{
		if (timer == null) 
		{
			timer = new InterfaceTimer();
		}
		return timer;
	}
	
	public void setTimer(IRequestTimeout iRequestTimeout)
	{
		this.iRequestTimeout = iRequestTimeout;
		handler.postDelayed(mRunnable, INTERFACE_TIME_OUT);
	}
	
	public void removeTimer()
	{
		handler.removeCallbacks(mRunnable);
	}
	
	private Runnable mRunnable = new Runnable() 
	{
		@Override
		public void run() 
		{
			if (iRequestTimeout != null) 
			{
				iRequestTimeout.timeout();
			}
		}
	};
	
	public interface IRequestTimeout
	{
		void timeout();
	}
	
}
