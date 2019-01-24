/**
 * @Title: TestActivity.java
 * @Package com.xike.xkliveplay.activity
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2017年7月18日 下午5:07:39
 * @version V1.0
 */
package com.xike.xkliveplay.activity.launch;

import com.xike.xkliveplay.R;
import com.xike.xkliveplay.framework.httpclient.VarParam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;

/**
  * @ClassName: TestActivity
  * @Description: TODO
  * @author Mernake
  * @date 2017年7月18日 下午5:07:39
  *
  */
public class TestActivity extends Activity
{
	
	private EditText et = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		et = (EditText) findViewById(R.id.et_test);
		findViewById(R.id.btn_test).setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				VarParam.url = et.getText().toString().trim();
				Intent intent = new Intent();
				intent.setClass(TestActivity.this, ActivityLaunch.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(intent);
			}
		});
	}
}
