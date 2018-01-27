/**
 * 
 */
package com.luhao.frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.luhao.bean.User;
import com.luhao.seivice.impl.UserServiceImpl;
import com.luhao.service.IUserService;
import com.luhao.thread.ClientReceiveThread;
import com.luhao.util.CheckUtil;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class LoginFrame extends JFrame {
	private IUserService ius = new UserServiceImpl();
	private Thread receiveThread;
	private PrintWriter out;
	private Socket socket;

	private JPanel contentPanel = new JPanel(new GridLayout(3, 1, 5, 5));

	private JPanel userNamePanel = new JPanel();
	private JPanel passwordPanel = new JPanel();
	private JPanel btnPanel = new JPanel();

	private JLabel userNameLabel = new JLabel("用户名:");
	private JTextField userNameTxt = new JTextField(10);

	private JLabel passwordLabel = new JLabel("密　 码:");
	private JPasswordField passwordTxt = new JPasswordField(10);

	private JButton loginBtn = new JButton("登陆");
	private JButton registBtn = new JButton("注册");

	public LoginFrame() {
		this.setBounds(0, 0, 300, 200);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.userNamePanel.add(userNameLabel);
		this.userNamePanel.add(userNameTxt);
		this.contentPanel.add(userNamePanel);

		this.passwordPanel.add(passwordLabel);
		this.passwordPanel.add(passwordTxt);
		this.contentPanel.add(passwordPanel);

		this.btnPanel.add(loginBtn);
		this.btnPanel.add(registBtn);
		this.contentPanel.add(btnPanel);

		this.setTitle("在线GameV0.1");
		this.setContentPane(contentPanel);
		pack();

		init();
	}

	private void init() {
		userNameTxt.setText("admin");
		passwordTxt.setText("admin");
		try {
			socket = new Socket("127.0.0.1", 60702);
			receiveThread = new Thread(new ClientReceiveThread(socket, this));
			out = new PrintWriter(socket.getOutputStream(), true);
			receiveThread.start();

		} catch (UnknownHostException e2) {
			JOptionPane.showMessageDialog(null, "链接失败!");
			// e2.printStackTrace();
		} catch (IOException e2) {
			JOptionPane.showMessageDialog(null, "链接失败!");
			// e2.printStackTrace();
		}

		this.loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = userNameTxt.getText();
				String password = new String(passwordTxt.getPassword());
				String msg = "<LOGIN>" + "0&" + userName + "&" + password + "&default<LOGIN>";
				out.println(msg);
			}
		});

		this.registBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = userNameTxt.getText();
				String password = new String(passwordTxt.getPassword());
				if (!CheckUtil.checkExsit(userName) || !CheckUtil.checkExsit(password)) {
					JOptionPane.showMessageDialog(null, "请输入合法值！");
					return;
				}
				User u = new User();
				u.setUsername(userName);
				u.setPassword(password);
				if (ius.isexsit(userName)) {
					JOptionPane.showMessageDialog(null, "该用户名已经存在！");
					return;
				}
				int n = ius.add(u);
				if (n < 1) {
					JOptionPane.showMessageDialog(null, "注册失败！");
					return;
				}
				JOptionPane.showMessageDialog(null, "注册成功！");
			}
		});
	}

}
