/**
 * 
 */
package com.luhao.thread;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.luhao.bean.User;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class ThreadSingleton {
	private ThreadSingleton instance;
	private static final String URL="192.168.1.112";
	private static final int PORT=60702;
	private static Socket socket;
	private static Thread thread;
	private static User user;
	private ThreadSingleton() {}
	
	public static Socket getSocket() {
		if(socket==null) {
			try {
				socket=new Socket(URL,PORT);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return socket;
	}
	//public static Socket getUser() {}
	private class MyThread implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
