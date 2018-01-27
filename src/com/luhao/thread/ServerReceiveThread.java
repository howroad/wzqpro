/**
 * 
 */
package com.luhao.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.luhao.bean.User;
import com.luhao.seivice.impl.UserServiceImpl;
import com.luhao.service.IUserService;
import com.luhao.util.ReceiveUtil;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class ServerReceiveThread implements Runnable{
	private IUserService ius=new UserServiceImpl();
	private BufferedReader br;
	private PrintWriter out;
	private Socket socket;
	private User user;
	public ServerReceiveThread(Socket socket) {
		this.socket=socket;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//判断并执行操作
	 private void receiveData(String data) {
		 String str=data.trim();
		 if(str.startsWith("<LOGIN>")&&str.endsWith("<LOGIN>")) {
			 String loginStr=str.trim().replace("<LOGIN>", "");
			 user=ius.login(ReceiveUtil.toUser(loginStr));
			 if(user!=null) {
				 System.out.println(user+"已登陆");
				 out.println("<LOGIN>"+user.toStreamString()+"<LOGIN>");
			 }
			 
		 }
	 }

}
