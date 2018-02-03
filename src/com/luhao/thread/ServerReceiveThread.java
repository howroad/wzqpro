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
import com.luhao.service.IFiveService;
import com.luhao.service.IUserService;
import com.luhao.service.impl.FiveServiceImpl;
import com.luhao.service.impl.UserServiceImpl;
import com.luhao.test.MyServer;
import com.luhao.util.MessageUtil;
import com.luhao.util.ReceiveUtil;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class ServerReceiveThread implements Runnable{
	private IUserService ius=new UserServiceImpl();
	private IFiveService ifs=new FiveServiceImpl();
	private BufferedReader br;
	public PrintWriter out;
	public Socket socket;
	private User user;
	private int otherId;
	private MyServer myServer;
	public ServerReceiveThread(Socket socket,MyServer myServer) {
		this.socket=socket;
		this.myServer=myServer;
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
			if(myServer.fightSet.contains(user.getId())) {
				System.out.println(user+"游戏中断线!");
				ifs.taopao(user.getId());
				myServer.fightSet.remove(user.getId());//从对战人中删除
				myServer.map.get(otherId).out.println("<TAOPAO>");//给对方发送逃跑消息
				return;
			}
			myServer.map.remove(user.getId());
			System.out.println(user+"客户端在大厅退出");
			MessageUtil.sendMessageToAll(myServer, user+"下线了!");
			MessageUtil.sendUserListToAll(myServer);
		}
		
	}
	//判断并执行操作
	 private void receiveData(String data) {
		 String str=data.trim();
		 System.out.println("接受的是:"+str);
		 //登陆
		 if(str.startsWith("<LOGIN>")&&str.endsWith("<LOGIN>")) {
			 String loginStr=str.trim().replace("<LOGIN>", "");
			 user=ius.login(ReceiveUtil.toUser(loginStr));
			 //用户名密码错误
			 if(user==null||user.getId()<1) {
				 out.println(MessageUtil.USER_OR_PASSWORD_FAILED);
				 return;
			 }
			 //已在线
			 if(myServer.map.keySet().contains(user.getId())) {
				 out.println(MessageUtil.USER_ALLREADY_ONLINE);
				 return;
			 }
			 //成功登陆
			 out.println("<LOGIN>"+user.toStreamString()+"<LOGIN>");
			 myServer.map.put(user.getId(), this);
			 MessageUtil.sendMessageToAll(myServer, user+"上线了!");
			 MessageUtil.sendUserListToAll(myServer);
			 return;
		 }
		 //消息
		 if(str.startsWith("<MESSAGE>")&&str.endsWith("<MESSAGE>")) {
			 String messageStr=str.trim().replace("<MESSAGE>", "");
			 MessageUtil.sendMessageToAll(myServer, user+":"+messageStr);
			 return;
		 }
		 //申请对战
		 if(str.startsWith("<FIGHT>")&&str.endsWith("<FIGHT>")) {
			 String fightStr=str.trim().replace("<FIGHT>", "");
			 otherId=Integer.parseInt(fightStr);
			 //玩家已经在对战中
			 if(myServer.fightSet.contains(otherId)) {
				 out.println("<FIGHT>ONFIGHT<FIGHT>");
				 return;
			 }
			 //申请对战
			 this.myServer.map.get(otherId).out.println("<FIGHT>"+user.toStreamString()+"<FIGHT>");
		 }
		 //打开窗口
		 if(str.startsWith("<STARTFIVE>")&&str.endsWith("<STARTFIVE>")) {
			 String messageStr=str.trim().replace("<STARTFIVE>", "");
			 //发送打开窗口信息,并把这两个玩家添加到set
			 otherId=Integer.parseInt(messageStr);
			 MessageUtil.fightWithUser(myServer, otherId, user.getId());
			 return;
		 }
		 //接受下棋坐标
		 if(str.startsWith("<ANSWER>")&&str.endsWith("<ANSWER>")) {
			 String messageStr=str.trim().replace("<ANSWER>", "");
			 //给对手发送坐标
			 MessageUtil.downQi(myServer, messageStr);
			 return;
		 }
		 //逃跑
		 if(str.startsWith("<TAOPAO>")&&str.endsWith("<TAOPAO>")) {
			 ifs.taopao(user.getId());//记录逃跑
			 ifs.win(otherId);//记录对方胜利
			 myServer.fightSet.remove(user.getId());//从对战人中删除
			 myServer.map.get(otherId).out.println("<TAOPAO>");//给对方发送逃跑消息
			 return;
		 }
		 //关闭五子棋
		 if(str.startsWith("<EXIT_FIVE_GAME>")&&str.endsWith("<EXIT_FIVE_GAME>")) {
			 myServer.fightSet.remove(user.getId());//从对战人中删除
			 if(myServer.fightSet.contains(otherId)) {
				 myServer.map.get(otherId).out.println("<EXIT_FIVE_GAME>");//给对方发送退出消息
			 }
			 return;
		 }
		 //胜利
		 if(str.startsWith("<WIN>")&&str.endsWith("<WIN>")) {
			 ifs.win(user.getId());
			 return;
		 }
		 //失败
		 if(str.startsWith("<LOST>")&&str.endsWith("<LOST>")) {
			 ifs.lost(user.getId());
			 return;
		 }
		 //接受对战中聊天
		 if(str.startsWith("<FIGHTMESSAGE>")&&str.endsWith("<FIGHTMESSAGE>")) {
			 String messageStr=str.trim().replace("<FIGHTMESSAGE>", "");
			 MessageUtil.sendMessageToFight(user.getId(),otherId,myServer, "[对战]"+user+":"+messageStr);
			 return;
		 }
		 //接受到准备
		 if(str.startsWith("<READY>")&&str.endsWith("<READY>")) {
			 myServer.map.get(otherId).out.println("<READY>");
		 }
	 }

}
