/**
 * 
 */
package com.luhao.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.luhao.bean.User;
import com.luhao.seivice.impl.UserServiceImpl;
import com.luhao.service.IUserService;
import com.luhao.util.ReceiveUtil;
import com.luhao.util.MessageUtil;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class ServerReceiveThread implements Runnable{
	private IUserService ius=new UserServiceImpl();
	private Map<Integer,ServerReceiveThread> map;
	private BufferedReader br;
	public PrintWriter out;
	public Socket socket;
	private User user;
	public ServerReceiveThread(Socket socket,Map<Integer,ServerReceiveThread> map) {
		this.socket=socket;
		this.map=map;
		try {
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String str="";
			while((str=br.readLine())!=null) {
				receiveData(str);
			}
		} catch (IOException e) {
			//System.out.println("客户端非正常退出");
			//e.printStackTrace();
		}
		
	}
	//判断并执行操作
	 private void receiveData(String data) {
		 String str=data.trim();
		 if(str.startsWith("<LOGIN>")&&str.endsWith("<LOGIN>")) {
			 String loginStr=str.trim().replace("<LOGIN>", "");
			 user=ius.login(ReceiveUtil.toUser(loginStr));
			 if(user!=null&&user.getId()>0) {
				 System.out.println(user+"已登陆!");
				 out.println("<LOGIN>"+user.toStreamString()+"<LOGIN>");
				 //System.out.println(user.getId());
				 map.put(user.getId(), this);
				 MessageUtil.sendMessageToAll(map, user+"上线了!");
				 MessageUtil.sendUserListToAll(map);
			 }else {
				 System.out.println(user+"登陆失败!");
				 out.println(MessageUtil.LoginFailed);
			 }
		 }
		 if(str.startsWith("<MESSAGE>")&&str.endsWith("<MESSAGE>")) {
			 String loginStr=str.trim().replace("<MESSAGE>", "");
			 MessageUtil.sendMessageToAll(map, user+":"+loginStr);
		 }
	 }

}
