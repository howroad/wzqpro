/**
 * 
 */
package com.luhao.frame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.luhao.bean.User;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 * 应该有一个socket做属性,thread不需要了,应该关闭login的out
 */
public class ChatFrame extends JFrame{
	
	private User user;
	
	private JPanel contentPanel=new JPanel(new BorderLayout());
	
	private JPanel leftPanel=new JPanel(new BorderLayout());
	private JPanel rightPanel=new JPanel(null);
	
	private JPanel showPanel=new JPanel(new BorderLayout());
	private JPanel sendPanel=new JPanel(new BorderLayout());
	private JPanel btnPanel=new JPanel(new BorderLayout());
	
	private JLabel leftLabel=new JLabel("用户：");
	private JList<User> nameList=new JList<User>();
	private DefaultListModel<User> model=new DefaultListModel<User>();
	private JScrollPane jspLeft=new JScrollPane(nameList);
	
	private JTextArea showText=new JTextArea();
	private JScrollPane jspRight=new JScrollPane(showText);
	
	private JTextArea sendText=new JTextArea();
	private JScrollPane jspsend=new JScrollPane(sendText);
	
	private JButton sendBtn=new JButton("发送");
	
	public ChatFrame(User user) {
		this.user=user;
		this.setLayout(null);
		this.setBounds(0, 0, 510, 410);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.leftPanel.add(leftLabel,BorderLayout.NORTH);
		this.leftPanel.add(jspLeft,BorderLayout.CENTER);
		
		this.showPanel.add(jspRight);
		this.sendPanel.add(jspsend);
		this.btnPanel.add(sendBtn);
		
		this.rightPanel.add(showPanel,BorderLayout.NORTH);
		this.rightPanel.add(sendPanel,BorderLayout.CENTER);
		this.rightPanel.add(btnPanel,BorderLayout.SOUTH);
		
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
		model.addElement(user);
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
