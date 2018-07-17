package com.luminroy.component.util.common;

import java.util.UUID;

/**
 * UUID的工具类
 * 
 * @author hunagtf
 * @date 2016/09/22
 *
 */
public class UUIDUtil {
	
	/**
	 * 32位去掉"-"的uuid值 
	 * @return
	 */
	public static String uuid32(){
		String serialNum=UUID.randomUUID().toString();//32位去掉"-"的uuid值 
    	serialNum=serialNum.replaceAll("-", "");
    	return serialNum;
	}
	
	
	public static void main(String[] args) {
		System.out.println(uuid32());
	}

}
