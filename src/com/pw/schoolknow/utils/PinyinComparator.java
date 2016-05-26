package com.pw.schoolknow.utils;

import java.util.Comparator;

import android.annotation.SuppressLint;

import com.pw.schoolknow.bean.UserBean;


public class PinyinComparator implements Comparator<Object>{

	@Override
	public int compare(Object o1, Object o2) {
		String str1 = PinyinUtils.getPingYin(getPy(o1));
        String str2 = PinyinUtils.getPingYin(getPy(o2));
        return str1.compareTo(str2);
	}
	
	/**
	 * ����ǳƵ�ƴ��
	 * @param obj
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public String getPy(Object obj){
		UserBean u=(UserBean) obj;
		return PinyinUtils.getAlpha(u.getNick().toUpperCase());
	}

}
