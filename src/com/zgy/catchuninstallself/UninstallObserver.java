package com.zgy.catchuninstallself;

/**
 * 
 * @Author ZGY
 * @Date:2013-12-13
 * @version 
 * @since
 * @function ��������ж��
 */

public class UninstallObserver {

	static{
		System.loadLibrary("observer");
	}
	
	/**
	 * 
	 * @param path  data/data/[packageNmae]
	 * @param url   ��ת��ҳ�� ��Ҫhttp��https��ͷ
	 * @param version
	 * @return
	 */
	public static native String startWork(String path, String url, int version);

}
