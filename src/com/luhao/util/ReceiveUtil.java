/**
 * 
 */
package com.luhao.util;

import com.luhao.bean.User;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class ReceiveUtil {
	public static User toUser(String str) {
		User user=new User();
		String[] udata=str.split("&");
		user.setId(Integer.parseInt(udata[0]));
		user.setUsername(udata[1]);
		user.setPassword(udata[2]);
		user.setNickname(udata[3]);
		return user;
	}
}
