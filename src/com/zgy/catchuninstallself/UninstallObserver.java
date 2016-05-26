package com.zgy.catchuninstallself;

/**
 * 
 * @Author ZGY
 * @Date:2013-12-13
 * @version 
 * @since
 * @function 监听自身被卸载
 */

public class UninstallObserver {

	static{
		System.loadLibrary("observer");
	}
	
	/**
	 * 
	 * @param path  data/data/[packageNmae]
	 * @param url   跳转的页面 需要http或https开头
	 * @param version
	 * @return
	 */
	public static native String startWork(String path, String url, int version);

}
