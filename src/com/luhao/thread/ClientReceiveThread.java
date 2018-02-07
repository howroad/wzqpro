/**
 * 
 */
package com.luhao.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.luhao.bean.User;
import com.luhao.frame.ChatFrame;
import com.luhao.frame.FiveMap;
import com.luhao.frame.LoginFrame;
import com.luhao.util.MessageUtil;
import com.luhao.util.ReceiveUtil;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class ClientReceiveThread implements Runnable {
	private BufferedReader br;
	private Socket socket;
	private User user;
	private LoginFrame loginFrame;
	private ChatFrame chatFrame;
	private FiveMap fiveMap;

	public ClientReceiveThread(Socket socket, LoginFrame loginFrame) {
		this.socket = socket;
		this.loginFrame = loginFrame;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			String str = "";
			while ((str = br.readLine()) != null) {
				receiveData(str);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "与服务器链接断开,游戏退出!");
			System.exit(0);
			//e.printStackTrace();
		}

	}

	// 判断并执行操作
	private void receiveData(String data) {
		String str = data.trim();
		if (str.startsWith("<LOGIN>") && str.endsWith("<LOGIN>")) {
			String loginStr = str.trim().replace("<LOGIN>", "");
			this.user = ReceiveUtil.toUser(loginStr);
			enterYard();
			return;
		}
		if (str.equals(MessageUtil.USER_OR_PASSWORD_FAILED)) {
			JOptionPane.showMessageDialog(null, "用户名或密码错误!");
			return;
		}
		if (str.equals(MessageUtil.USER_ALLREADY_ONLINE)) {
			JOptionPane.showMessageDialog(null, "用户已登陆!");
			return;
		}
		if (str.startsWith("<MESSAGE>") && str.endsWith("<MESSAGE>")) {
			String message = str.trim().replace("<MESSAGE>", "");
			this.chatFrame.setShowTxt(message);
			if(this.fiveMap!=null) {
				this.fiveMap.setShowTxt(message);
			}
			return;
		}
		if (str.startsWith("<USERLIST>") && str.endsWith("<USERLIST>")) {
			String message = str.trim().replace("<USERLIST>", "");
			int[] userIds = MessageUtil.StringToUserIds(message);
			this.chatFrame.updateList(userIds);
			return;
		}
		if (str.startsWith("<FIGHT>") && str.endsWith("<FIGHT>")) {
			String message = str.trim().replace("<FIGHT>", "");
			if (message.equals("ONFIGHT")) {
				JOptionPane.showMessageDialog(null, "对方已经开战!");
			} else {
				User otherUser=MessageUtil.toUser(message);
				chatFrame.receiveFight(otherUser);
			}
			return;
		}
		if (str.startsWith("<OPENFIVE>") && str.endsWith("<OPENFIVE>")) {
			String message = str.trim().replace("<OPENFIVE>", "");
			int[] temp=MessageUtil.StringToOtherUserAndColor(message);
			startFive(temp[0],temp[1]);
			return;
		}
		if (str.startsWith("<ANSWER>") && str.endsWith("<ANSWER>")) {
			String message = str.trim().replace("<ANSWER>", "");
			int[] temp=MessageUtil.StringToAnswer(message);
			fiveMap.otherDown(temp[0], temp[1]);
			return;
		}
		if (str.startsWith("<TAOPAO>") && str.endsWith("<TAOPAO>")) {
			JOptionPane.showMessageDialog(null, "对方逃跑,胜利!");
			fiveMap.exit();
			return;
		}
		if (str.startsWith("<EXIT_FIVE_GAME>") && str.endsWith("<EXIT_FIVE_GAME>")) {
			JOptionPane.showMessageDialog(null, "对方已离开!");
			fiveMap.exit();
			return;
		}
		if (str.startsWith("<READY>") && str.endsWith("<READY>")) {
				this.fiveMap.setShowTxt("对方已准备!");
				if(this.fiveMap.isReady) {
					this.fiveMap.start();
				}
			return;
		}
	}

	private void enterYard() {
		this.chatFrame = new ChatFrame(user, socket);
		chatFrame.setVisible(true);
		loginFrame.dispose();
	}
	private void startFive(int otherUserId,int myColor) {
		if(fiveMap==null) {
			fiveMap = new FiveMap(socket,chatFrame,otherUserId,(byte)myColor);
			fiveMap.setVisible(true);
			chatFrame.setVisible(false);
		}else {
			fiveMap.initAndStart((byte)myColor);
		}

	}
}
