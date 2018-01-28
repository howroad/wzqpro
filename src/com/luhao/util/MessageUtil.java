/**
 * 
 */
package com.luhao.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.luhao.bean.User;
import com.luhao.thread.ServerReceiveThread;

/**
 * @author howroad
 * @Date 2018年1月28日
 * @version 1.0
 */
public class MessageUtil {
	public static String LoginFailed = "<LOGINFAILED>";
	public static User toUser(String str) {
		User user=new User();
		String[] udata=str.split("&");
		user.setId(Integer.parseInt(udata[0]));
		user.setUsername(udata[1]);
		user.setPassword(udata[2]);
		user.setNickname(udata[3]);
		return user;
	}
	public static int[] StringToUserIds(String msg){
		String[] udata=msg.split("&");
		int[] userId=new int[udata.length];
		for(int i=0;i<udata.length;i++) {
			userId[i]=Integer.parseInt(udata[i]);
		}
		return userId;
	}
	public static void sendMessageToAll(Map<Integer, ServerReceiveThread> map, String msg) {
		for (Integer i : map.keySet()) {
			map.get(i).out.println("<MESSAGE>"+msg+"<MESSAGE>");
			System.out.println("发送的是:<MESSAGE>"+msg+"<MESSAGE>");
		}
	}
	public static void sendUserListToAll(Map<Integer, ServerReceiveThread> map) {
		String list="";
		for(Integer in:map.keySet()) {
			if(in>0) {
				list+=in+"&";
			}
		}
		list=list.substring(0, list.length()-1);
		//System.out.println(list);
		for (Integer i : map.keySet()) {
			map.get(i).out.println("<USERLIST>"+list+"<USERLIST>");
		}
	}
}
