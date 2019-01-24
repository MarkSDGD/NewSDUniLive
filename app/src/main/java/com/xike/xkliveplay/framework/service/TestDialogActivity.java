/**
 * @Title: TestDialogActivity.java
 * @Package com.xike.xkliveplay.framework.service
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2016年6月6日 下午2:21:14
 * @version V1.0
 */
package com.xike.xkliveplay.framework.service;

import com.xike.xkliveplay.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @ClassName: TestDialogActivity
 * @Description: TODO
 * @author Mernake
 * @date 2016年6月6日 下午2:21:14
 *
 */
public class TestDialogActivity extends Activity 
{ 
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_update_ac);
		startService(new Intent(this,LivePlayBackService.class));
	}
	
	@Override
	protected void onStop() {
		stopService(new Intent(this,LivePlayBackService.class));
		super.onStop();
	}
}
