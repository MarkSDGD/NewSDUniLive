/**
 * @author ��ΰ
 * @date:2014-3-31 ����11:57:46
 * @version : V1.0
 *
 */

package com.xike.xkliveplay.activity.fragment;

import android.os.Bundle;

/**
 * @author Administrator
 *
 */
public interface IFragmentJump {

	/**
	 * 
	 * @param fragmentType : ��ǰҪ��ת������
	 * @param curIndex �� ��ǰ��index
	 */
	public void jumpToFramgment(int fragmentType,int curIndex);
	public void jumpToFragmentWithBundles(int fragmentType,int curIndex,Bundle bundle);
	public void jumpToVod(Bundle bundle);
}
