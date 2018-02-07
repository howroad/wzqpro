/**
 * 
 */
package com.luhao.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.luhao.bean.User;
import com.luhao.service.IUserService;
import com.luhao.service.impl.UserServiceImpl;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class ChatFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IUserService ius=new UserServiceImpl();
	private Socket socket ;
	private PrintWriter out;
	private User user;
	
	private JPanel contentPanel=new JPanel(new BorderLayout());
	
	private JPanel leftPanel=new JPanel(new BorderLayout());
	private JPanel rightPanel=new JPanel(null);
	
	private JPanel showPanel=new JPanel(new BorderLayout());
	private JPanel sendPanel=new JPanel(new BorderLayout());
	private JPanel btnPanel=new JPanel(new GridLayout(1,3,5,5));
	
	private JLabel leftLabel=new JLabel("用户：");
	private JList<User> nameList=new JList<User>();
	private DefaultListModel<User> model=new DefaultListModel<User>();
	private JScrollPane jspLeft=new JScrollPane(nameList);
	
	private JTextArea showText=new JTextArea();
	private JScrollPane jspRight=new JScrollPane(showText);
	
	private JTextArea sendText=new JTextArea();
	private JScrollPane jspsend=new JScrollPane(sendText);
	
	private JButton sendBtn=new JButton("发送");
	private JButton checkUserBtn=new JButton("查看信息");
	private JButton fightBtn=new JButton("对战");
	
	public ChatFrame(User user,Socket socket) {
		this.user=user;
		this.socket=socket;
		this.setLayout(null);
		this.setBounds(0, 0, 510, 410);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.leftPanel.add(leftLabel,BorderLayout.NORTH);
		this.leftPanel.add(jspLeft,BorderLayout.CENTER);
		
		this.showPanel.add(jspRight);
		this.sendPanel.add(jspsend);
		this.btnPanel.add(checkUserBtn);
		this.btnPanel.add(fightBtn);
		this.btnPanel.add(sendBtn);
		
		this.rightPanel.add(showPanel);
		this.rightPanel.add(sendPanel);
		this.rightPanel.add(btnPanel);
		
		this.contentPanel.add(leftPanel,BorderLayout.WEST);
		this.contentPanel.add(rightPanel,BorderLayout.CENTER);
		this.setTitle("Chat V0.1");
		this.setContentPane(contentPanel);
		//pack();
		
		//设置大小
		this.leftPanel.setBackground(Color.BLUE);
		this.rightPanel.setBackground(Color.RED);
		this.leftPanel.setSize(150, 360);
		this.rightPanel.setSize(350,360);
		
		this.showPanel.setBounds(5, 5, 360, 260);
		this.sendPanel.setBounds(5,270,360,70);
		this.btnPanel.setBounds(5,345,360,25);
		
		
		nameList.setFixedCellWidth(120);
		nameList.setModel(model);
		
		try {
			//应该可以把LoginFrame的out传过来,但是不知道dispose了之后流还在不
			this.out=new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(),"UTF-8"),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendMessage();
			}
		});
		this.sendText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});
		this.checkUserBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(ChatFrame.this.nameList.getSelectedValue()==null) {
					JOptionPane.showMessageDialog(null, "请选择一个玩家!");
					return;
				}
				new UserInfoDelDialog(ChatFrame.this.nameList.getSelectedValue().getId());
			}
		});
		this.fightBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(ChatFrame.this.nameList.getSelectedValue()==null) {
					JOptionPane.showMessageDialog(null, "请选择一个玩家!");
					return;
				}
				int fightId=ChatFrame.this.nameList.getSelectedValue().getId();
				if(fightId==user.getId()) {
					JOptionPane.showMessageDialog(null, "你是周伯通吗?");
					return;
				}
				fightToUser(fightId);
			}
			
		});
		
	}
	
	public void setShowTxt(String msg) {
		this.showText.setText(showText.getText()+msg+"\n");
	}
	public void addList(int userId) {
		User u=ius.getById(userId);
		model.addElement(u);
	}
	public void updateList(int[] userIds) {
		model.removeAllElements();
		model.addElement(user);
		for(int i=0;i<userIds.length;i++) {
			if(this.user.getId()==userIds[i]) {
				continue;
			}
			User u=ius.getById(userIds[i]);
			model.addElement(u);
		}
	}
	public void sendMessage() {
		out.println("<MESSAGE>"+sendText.getText().trim()+"<MESSAGE>");
		this.sendText.setText(null);
	}
	
	public void fightToUser(int userId) {
		out.println("<FIGHT>"+userId+"<FIGHT>");
		this.fightBtn.setText("已申请");
		this.fightBtn.setEnabled(false);
	}
	
	public void receiveFight(User otherUser) {
		int answer=JOptionPane.showConfirmDialog(null, otherUser.getNickname()+"申请与你对战,是否接受?");
		if(answer==0) {
			out.println("<STARTFIVE>"+otherUser.getId()+"<STARTFIVE>");
		}
	}
	public void initBtn() {
		this.fightBtn.setText("申请对战");
		this.fightBtn.setEnabled(true);
	}
	
	
}
