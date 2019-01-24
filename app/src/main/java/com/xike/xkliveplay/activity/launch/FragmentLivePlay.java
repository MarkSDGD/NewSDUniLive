package com.xike.xkliveplay.activity.launch;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.View;

import com.xike.xkliveplay.activity.fragment.FragmentLivePlayBase;
import com.xike.xkliveplay.activity.fragment.IFragmentJump;

/**
 * @author LiWei <br>
 * CreateTime: 2014年11月3日 上午9:29:58<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
@SuppressLint("ValidFragment")
public class FragmentLivePlay extends FragmentLivePlayBase
{

	/**
	 * @param _fJump
	 */
	public FragmentLivePlay(IFragmentJump _fJump) {
		super(_fJump);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		
		if (keyCode == KeyEvent.KEYCODE_HOME) 
		{
			onStop();
			return true;
		}
		
		super.onKeyDown(keyCode, event);
		
		return true;
	}

}
