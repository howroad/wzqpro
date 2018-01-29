/**
 * 
 */
package com.luhao.util;

import com.luhao.bean.User;
import com.luhao.test.MyServer;

/**
 * @author howroad
 * @Date 2018年1月28日
 * @version 1.0
 */
public class MessageUtil {
	public static String USER_OR_PASSWORD_FAILED = "<USER_OR_PASSWORD_FAILED>";
	public static String USER_ALLREADY_ONLINE = "<USER_ALLREADY_ONLINE>";
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
	public static void sendMessageToAll(MyServer myServer, String msg) {
		for (Integer i : myServer.map.keySet()) {
			myServer.map.get(i).out.println("<MESSAGE>"+msg+"<MESSAGE>");
			System.out.println("发送的是:<MESSAGE>"+msg+"<MESSAGE>");
		}
	}
	public static void sendUserListToAll(MyServer myServer) {
		String list="";
		for(Integer in:myServer.map.keySet()) {
			if(in>0) {
				list+=in+"&";
			}
		}
		list=list.substring(0, list.length()-1);
		for (Integer i : myServer.map.keySet()) {
			myServer.map.get(i).out.println("<USERLIST>"+list+"<USERLIST>");
		}
	}
	public static void fightWithUser(MyServer myServer,int user1,int user2) {
		myServer.map.get(user1).out.println("<OPENFIVE>"+user2+"&"+"1"+"<OPENFIVE>");
		myServer.map.get(user2).out.println("<OPENFIVE>"+user1+"&"+"2"+"<OPENFIVE>");
		myServer.fightSet.add(user1);
		myServer.fightSet.add(user2);
	}
	public static int[] StringToOtherUserAndColor(String message) {
		int[] result=new int[2];
		String[] udata=message.split("&");
		result[0]=Integer.parseInt(udata[0]);
		result[1]=Integer.parseInt(udata[1]);
		return result;
	}
	public static int[] StringToAnswer(String message) {
		return StringToOtherUserAndColor(message);
	}
	public static void downQi(MyServer myServer,String message) {
		String[] udata=message.split("&");
		int secondId=Integer.parseInt(udata[0]);
		String answerX=udata[1];
		String answerY=udata[2];
		myServer.map.get(secondId).out.println("<ANSWER>"+answerX+"&"+answerY+"<ANSWER>");
	}
}
