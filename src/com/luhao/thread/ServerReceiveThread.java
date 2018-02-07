/**
 * 
 */
package com.luhao.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.luhao.bean.User;
import com.luhao.frame.ServerFrame;
import com.luhao.service.IFiveService;
import com.luhao.service.IUserService;
import com.luhao.service.impl.FiveServiceImpl;
import com.luhao.service.impl.UserServiceImpl;
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
	private ServerFrame serverFrame;
	public ServerReceiveThread(Socket socket,ServerFrame serverFrame) {
		this.socket=socket;
		this.serverFrame=serverFrame;
		try {
			br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			this.out=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			br=new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			String str="";
			while((str=br.readLine())!=null) {
				receiveData(str);
			}
		} catch (IOException e) {
			if(user==null) {
				return;
			}
			if(serverFrame.fightSet.size()>0&&serverFrame.fightSet.contains(user.getId())) {
				serverFrame.showMessage(user+"游戏中断线!");
				ifs.taopao(user.getId());
				serverFrame.fightSet.remove(user.getId());//从对战人中删除
				serverFrame.map.get(otherId).out.println("<TAOPAO>");//给对方发送逃跑消息
				return;
			}
			serverFrame.map.remove(user.getId());
			serverFrame.showMessage(user+"客户端在大厅退出");
			MessageUtil.sendMessageToAll(serverFrame, user+"下线了!");
			MessageUtil.sendUserListToAll(serverFrame);
		}
		
	}
	//判断并执行操作
	 private void receiveData(String data) {
		 String str=data.trim();
		 serverFrame.showMessage("接受的是:"+str);
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
			 if(serverFrame.map.keySet().contains(user.getId())) {
				 out.println(MessageUtil.USER_ALLREADY_ONLINE);
				 return;
			 }
			 //成功登陆
			 out.println("<LOGIN>"+user.toStreamString()+"<LOGIN>");
			 serverFrame.map.put(user.getId(), this);
			 MessageUtil.sendMessageToAll(serverFrame, user+"上线了!");
			 MessageUtil.sendUserListToAll(serverFrame);
			 return;
		 }
		 //消息
		 if(str.startsWith("<MESSAGE>")&&str.endsWith("<MESSAGE>")) {
			 String messageStr=str.trim().replace("<MESSAGE>", "");
			 MessageUtil.sendMessageToAll(serverFrame, user+":"+messageStr);
			 return;
		 }
		 //申请对战
		 if(str.startsWith("<FIGHT>")&&str.endsWith("<FIGHT>")) {
			 String fightStr=str.trim().replace("<FIGHT>", "");
			 otherId=Integer.parseInt(fightStr);
			 //玩家已经在对战中
			 if(serverFrame.fightSet.contains(otherId)) {
				 out.println("<FIGHT>ONFIGHT<FIGHT>");
				 return;
			 }
			 //申请对战
			 this.serverFrame.map.get(otherId).out.println("<FIGHT>"+user.toStreamString()+"<FIGHT>");
		 }
		 //打开窗口
		 if(str.startsWith("<STARTFIVE>")&&str.endsWith("<STARTFIVE>")) {
			 String messageStr=str.trim().replace("<STARTFIVE>", "");
			 //发送打开窗口信息,并把这两个玩家添加到set
			 otherId=Integer.parseInt(messageStr);
			 MessageUtil.fightWithUser(serverFrame, otherId, user.getId());
			 return;
		 }
		 //接受下棋坐标
		 if(str.startsWith("<ANSWER>")&&str.endsWith("<ANSWER>")) {
			 String messageStr=str.trim().replace("<ANSWER>", "");
			 //给对手发送坐标
			 MessageUtil.downQi(serverFrame, messageStr);
			 return;
		 }
		 //逃跑
		 if(str.startsWith("<TAOPAO>")&&str.endsWith("<TAOPAO>")) {
			 ifs.taopao(user.getId());//记录逃跑
			 ifs.win(otherId);//记录对方胜利
			 serverFrame.fightSet.remove(user.getId());//从对战人中删除
			 serverFrame.map.get(otherId).out.println("<TAOPAO>");//给对方发送逃跑消息
			 return;
		 }
		 //关闭五子棋
		 if(str.startsWith("<EXIT_FIVE_GAME>")&&str.endsWith("<EXIT_FIVE_GAME>")) {
			 serverFrame.fightSet.remove(user.getId());//从对战人中删除
			 if(serverFrame.fightSet.contains(otherId)) {
				 serverFrame.map.get(otherId).out.println("<EXIT_FIVE_GAME>");//给对方发送退出消息
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
			 MessageUtil.sendMessageToFight(user.getId(),otherId,serverFrame, "[对战]"+user+":"+messageStr);
			 return;
		 }
		 //接受到准备
		 if(str.startsWith("<READY>")&&str.endsWith("<READY>")) {
			 serverFrame.map.get(otherId).out.println("<READY>");
		 }
	 }

}
