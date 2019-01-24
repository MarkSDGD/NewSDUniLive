package com.xike.xkliveplay.activity.launch;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.KeyEvent;

import com.xike.xkliveplay.activity.fragment.FragmentRecommendBase;
import com.xike.xkliveplay.activity.fragment.IFragmentJump;

/**
 * @author LiWei <br>
 * CreateTime: 2014年11月3日 上午9:29:58<br>
 * <b>Info:</b><br>
 *<br>
 *   <b>Method:</b> <br>
 */
@SuppressLint("ValidFragment")
public class FragmentRecommend extends FragmentRecommendBase
{
	private IFragmentJump iFragmentJump;

	public FragmentRecommend(){

	}

	public FragmentRecommend(IFragmentJump _fJump){
		super(_fJump);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		Log.e("shandongLive", "onKeyDown: FragmentRecommend" );
		return true;
	}
}
