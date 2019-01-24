/**
 * @author ��ΰ
 * @date:2014-2-24 ����08:51:34
 * @version : V1.0
 *
 */

package com.xike.xkliveplay.framework.http;

/**
 * @author Administrator
 *
 */
public interface IRequestCallback {

    void requestDidFinished(String method,String body);
    
    void requestDidFailed(String method,String body);
}
