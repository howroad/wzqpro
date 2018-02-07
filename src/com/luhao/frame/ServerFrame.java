/**
 * 
 */
package com.luhao.frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.luhao.thread.ServerReceiveThread;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class ServerFrame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ServerSocket ss=null;
	public Map<Integer,ServerReceiveThread> map=new HashMap<Integer,ServerReceiveThread>();
	public Set<Integer> fightSet=new HashSet<Integer>();
	
	private JPanel content=new JPanel(new BorderLayout());
	private JPanel btnPanel=new JPanel();
	private JTextArea jta=new JTextArea();
	private JScrollPane showScr=new JScrollPane(jta);
	private JButton startBtn=new JButton("START");
	private JButton closeBtn=new JButton("CLOSE");
	private Thread thd1=null;
	
	public ServerFrame() {
		this.btnPanel.add(startBtn);
		this.btnPanel.add(closeBtn);
		this.content.add(showScr,BorderLayout.CENTER);
		this.content.add(btnPanel,BorderLayout.SOUTH);
		this.setTitle("五子棋 V4  服务端");
		this.setContentPane(content);
		this.setBounds(300, 300, 300, 500);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.closeBtn.setEnabled(false);
		this.startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startBtn.setEnabled(false);		
				thd1=new Thread(new Thread1());
				thd1.start();
			}
			
		});
		this.closeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
			
		});
	}
	public void showMessage(String message) {
		jta.setText(jta.getText()+message+"\n");
	}
	private class Thread1 implements Runnable{
		@Override
		public void run() {
			try {
				ss=new ServerSocket(60702);
				for(int i=0;i<100;i++) {
					Socket s=ss.accept();
					Thread t1=new Thread(new ServerReceiveThread(s,ServerFrame.this));
					t1.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
