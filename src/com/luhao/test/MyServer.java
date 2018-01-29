/**
 * 
 */
package com.luhao.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.luhao.thread.ServerReceiveThread;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class MyServer {
	private ServerSocket ss=null;
	public Map<Integer,ServerReceiveThread> map=new HashMap<Integer,ServerReceiveThread>();
	public Set<Integer> fightSet=new HashSet<Integer>();
	
	public static void main(String[] args) {
		new MyServer().run();
	}
	
	public void run() {
		try {
			ss=new ServerSocket(60702);
			for(int i=0;i<100;i++) {
				Socket s=ss.accept();
				Thread t1=new Thread(new ServerReceiveThread(s,this));
				t1.start();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
