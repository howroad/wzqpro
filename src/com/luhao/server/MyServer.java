/**
 * 
 */
package com.luhao.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.luhao.seivice.impl.UserServiceImpl;
import com.luhao.service.IUserService;
import com.luhao.thread.ServerReceiveThread;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class MyServer {
	private ServerSocket ss=null;
	private Map<Integer,ServerReceiveThread> map=new HashMap<Integer,ServerReceiveThread>();
	
	public static void main(String[] args) {
		new MyServer().run();
	}
	
	public void run() {
		try {
			ss=new ServerSocket(60702);
			for(int i=0;i<100;i++) {
				Socket s=ss.accept();
				Thread t1=new Thread(new ServerReceiveThread(s,map));
				t1.start();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
