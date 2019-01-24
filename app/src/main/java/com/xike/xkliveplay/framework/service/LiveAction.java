/**
 * @Title: LiveAction.java
 * @Package com.xike.xkliveplay.framework.service
 * @Description: TODO
 * Copyright: Copyright (c) 2015
 * Company:青岛博瑞立方网络科技有限公司
 * 
 * @author Mernake
 * @date 2016年5月30日 上午10:13:27
 * @version V1.0
 */
package com.xike.xkliveplay.framework.service;

/**
 * @ClassName: LiveAction
 * @Description: TODO
 * @author Mernake
 * @date 2016年5月30日 上午10:13:27
 *
 */
public class LiveAction 
{
	//要求服务请求
	
	/**请求检测升级**/
	public static final String ACTION_REQUEST_UPDATE = "action_request_update";
	/**请求根栏目ID**/
	public static final String ACTION_REQUEST_ROOT_ID = "action_request_root_id";
	/**请求全部类别ID**/
	public static final String ACTION_REQUEST_ALL_ID = "action_request_all_id";
	/**请求全部频道**/
	public static final String ACTION_REQUEST_ALL_CHANNEL = "action_request_all_channel";
	/**请求收藏频道**/
	public static final String ACTION_REQUEST_SAVE_CHANNEL = "action_request_save_channel";
	
	
	//通知主类接口访问成功
	
	/**通知升级接口访问成功**/
	public static final String ACTION_NOTIFY_REQUEST_UPDATE_SUCCESS = "action_notify_request_update_success";
	/**通知根栏目ID成功**/
	public static final String ACTION_NOTIFY_REQUEST_ROOT_ID_SUCCESS  = "action_notify_request_root_id_success";
	/**通知全部类别ID成功**/
	public static final String ACTION_NOTIFY_REQUEST_ALL_ID_SUCCESS  = "action_notify_request_all_id_success";
	/**通知全部频道成功**/
	public static final String ACTION_NOTIFY_REQUEST_ALL_CHANNEL_SUCCESS  = "action_notify_request_all_channel_success";
	/**通知获取收藏频道成功**/
	public static final String ACTION_NOTIFY_REQUEST_SAVE_CHANNEL_SUCCESS  = "action_notify_request_save_channel_success";
	
}
