package com.luhao.frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 五子棋棋盘 
 * 20180108
 * @author howroad
 */
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.luhao.bean.User;
import com.luhao.service.IAnswerService;
import com.luhao.service.IUserService;
import com.luhao.service.impl.AnswerServiceImpl3;
import com.luhao.service.impl.UserServiceImpl;


public class FiveMap extends JFrame{

	private static final long serialVersionUID = 1L;
	/**
	 * 以下两个值按照自己的需求更改
	 */
	private final static byte LENGTH = 19;// 棋盘大小(需要奇数,自适应可以直接修改)
	private final static int MYWIDTH = 700;// 棋盘高度(可以直接修改更改棋盘)

	private final static int LINEWIDTH = MYWIDTH / (LENGTH - 1);// 棋子行间距
	private final static int QIWIDTH = LINEWIDTH - 2;// (需要偶数)
	private byte[][] qi = new byte[LENGTH][LENGTH];// 棋盘大小

	private JPanel content = new JPanel(null);
	private JPanel left = new JPanel();
	private JPanel right = new JPanel(null);
	
	private JButton readyBtn = new JButton("准备");
	private JButton startBtn = new JButton("开始");
	private JButton heqiBtn =new JButton("求和");
	
	private JTextArea showText=new JTextArea();
	private JTextArea sendText=new JTextArea();
	private JButton sendBtn=new JButton("发送信息");
	
	private byte color;// 棋子颜色 0代表无子 1 代表黑棋 2代表白棋
	private byte otherColor;
	private int answerX = (LENGTH - 1) / 2;// 当前的下子
	private int answerY = (LENGTH - 1) / 2;// 当前下子
	
	private IAnswerService ias=new AnswerServiceImpl3();
	private IUserService ius=new UserServiceImpl();
	private User otherUser;
	private PrintWriter out;
	private boolean myTurn;
	private boolean isStart;
	public boolean isReady;
	private ChatFrame chatFrame;
	

	public FiveMap(Socket socket,ChatFrame chatFrame,int otherUserId,byte color) {
		this.otherUser=ius.getById(otherUserId);
		this.chatFrame=chatFrame;
		try {
			this.out=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"),true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.right.setBackground(Color.MAGENTA);
		readyBtn.setBounds(5, 5, 100, 30);
		startBtn.setBounds(5, 40, 100, 30);
		heqiBtn.setBounds(5, 75, 100, 30);
		showText.setBounds(5, 110, 180, MYWIDTH-250);
		sendText.setBounds(5, MYWIDTH-125, 180, 60);
		sendBtn.setBounds(5, MYWIDTH-60, 100, 30);
		right.add(readyBtn);
		right.add(startBtn);
		right.add(heqiBtn);
		right.add(showText);
		right.add(sendText);
		right.add(sendBtn);
		// 设置棋盘的布局
		left.setBackground(new Color(90, 225, 90));
		left.setBounds(0, 0, MYWIDTH + 100, MYWIDTH + 100);
		right.setBounds(MYWIDTH + 100, 0, 200, MYWIDTH+100);
		content.add(left);
		content.add(right);
		this.add(content);
		this.setBounds(0, 0, MYWIDTH + 310, MYWIDTH + 80);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addMouseListener(new MyMouseListener());
		//准备
		readyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isReady=true;
				out.println("<READY>");
				setShowTxt("我方已准备");
			}
		});
		//开始
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}

		});
		//求和
		heqiBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
			
		});
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				int answer=JOptionPane.showConfirmDialog(null, "你确定要关闭吗?");
				if(answer==0) {
					if(isStart) {
						System.out.println("游戏逃跑");
						out.println("<TAOPAO>");
					}
					exit();
				}
			}
			
		});
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
		
		this.setTitle("五子棋 by howroad V2.0 [正在对战: "+otherUser+"]");
		
		
		initAndStart(color);
	}

	// 初始化并开始
	public void initAndStart(byte mycolor) {
		this.readyBtn.setEnabled(false);
		for (int i = 0; i < LENGTH; i++) {
			for (int j = 0; j < LENGTH; j++) {
				qi[i][j] = 0;
			}
		}
		this.color=mycolor;
		this.otherColor=(byte) (this.color==1?2:1);
		answerX = (LENGTH - 1) / 2;
		answerY = (LENGTH - 1) / 2;
		isStart=true;
		if(color==1) {
			myTurn=true;
		}
		repaint();
	}

	// 绘制棋盘
	public void paint(Graphics g) {
		super.paint(g);
		// 画棋盘
		for (int i = 0; i < LENGTH; i++) {
			g.drawLine(50, 50 + LINEWIDTH * i, 50 + LINEWIDTH * (LENGTH - 1), 50 + LINEWIDTH * i);
		}
		for (int i = 0; i < LENGTH; i++) {
			g.drawLine(50 + LINEWIDTH * i, 50, 50 + LINEWIDTH * i, 50 + LINEWIDTH * (LENGTH - 1));
		}
		int center = 50 + (LENGTH - 1) / 2 * LINEWIDTH - 4;
		g.fillOval(center, center, 8, 8);
		// 画棋
		for (int i = 0; i < qi.length; i++) {
			for (int j = 0; j < qi[i].length; j++) {
				if (qi[i][j] == 1) {// 画黑棋
					g.setColor(Color.BLACK);
					g.fillOval(50 + i * LINEWIDTH - QIWIDTH / 2, 50 + j * LINEWIDTH - QIWIDTH / 2, QIWIDTH, QIWIDTH);
				}
				if (qi[i][j] == 2) {// 画白棋
					g.setColor(Color.WHITE);
					g.fillOval(50 + i * LINEWIDTH - QIWIDTH / 2, 50 + j * LINEWIDTH - QIWIDTH / 2, QIWIDTH, QIWIDTH);
					g.setColor(Color.BLACK);
					g.drawOval(50 + i * LINEWIDTH - QIWIDTH / 2, 50 + j * LINEWIDTH - QIWIDTH / 2, QIWIDTH, QIWIDTH);
				}
			}
		}
		// 画小绿点
		g.setColor(Color.CYAN);
		g.fillOval(50 + answerX * LINEWIDTH - 3, 50 + answerY * LINEWIDTH - 3, 6, 6);
		g.setColor(Color.BLACK);
	}

	
	public void sendAnswer() {
		String str=otherUser.getId()+"&"+answerX+"&"+answerY;
		out.println("<ANSWER>"+str+"<ANSWER>");
		myDown();
	}
	
	//下棋
	public void myDown() {
		qi[answerX][answerY]=color;
		myTurn=false;
		repaint();
		if (ias.isWin(qi, color, answerX, answerY)) {
			out.println("<WIN>");
			JOptionPane.showMessageDialog(null, color == 1 ? "黑棋获胜！" : "白棋获胜！");
			this.readyBtn.setEnabled(true);
			isStart=false;
			myTurn = false;
			isReady=false;
			return;
		}
	}
	//对手下棋
	public void otherDown(int x,int y) {
		answerX=x;
		answerY=y;
		qi[x][y]=otherColor;
		myTurn=true;
		repaint();
		if (ias.isWin(qi, otherColor, answerX, answerY)) {
			JOptionPane.showMessageDialog(null, otherColor == 1 ? "黑棋获胜！" : "白棋获胜！");
			out.println("<LOST>");
			this.readyBtn.setEnabled(true);
			isStart=false;
			myTurn = false;
			isReady=false;
			return;
		}
	}
	//开始游戏
	public void start() {
		this.readyBtn.setEnabled(false);
		out.println("<STARTFIVE>"+otherUser.getId()+"<STARTFIVE>");
	}
	//显示信息
	public void setShowTxt(String msg) {
		this.showText.setText(showText.getText()+msg+"\n");
	}
	//关闭
	public void exit() {
		out.println("<EXIT_FIVE_GAME>");
		FiveMap.this.dispose();//关闭棋盘
		chatFrame.initBtn();
		chatFrame.setVisible(true);//打开聊天大厅
	}
	//对战中聊天
	public void sendMessage() {
		out.println("<FIGHTMESSAGE>"+sendText.getText().trim()+"<FIGHTMESSAGE>");
		this.sendText.setText(null);
	}
	
	// 内部类,用于监听鼠标动作
	public class MyMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (!myTurn) {
				return;
			}
			// 将鼠标坐标转换为棋盘坐标
			answerX = Math.round((e.getX() - 50.0f) / LINEWIDTH);
			answerY = Math.round((e.getY() - 50.0f) / LINEWIDTH);
			// 判断是否再棋盘范围
			if (answerX < LENGTH && answerY < LENGTH) {
				// 判断此处是否有棋子
				if (qi[answerX][answerY] != 0) {
					return;
				}
				sendAnswer();
			} else {
				return;
			}
		}

	}
	
}
