package com.luhao.frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
/**
 * 五子棋棋盘 
 * 20180108
 * @author howroad
 */
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.luhao.service.IAnswerService;
import com.luhao.service.impl.AnswerServiceImpl3;

public class FiveMapD extends JFrame{
	/**
	 * 棋盘界面
	 * 
	 * @author howroad
	 * @version v1.4_20180110
	 *  未解决的问题: 悔棋之后小绿点不会智能的跳到该棋,悔棋有问题所以屏蔽了
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 以下两个值按照自己的需求更改
	 */
	private final static byte LENGTH = 19;// 棋盘大小(需要奇数,自适应可以直接修改)
	private final static int MYWIDTH = 700;// 棋盘高度(可以直接修改更改棋盘)

	private final static int LINEWIDTH = MYWIDTH / (LENGTH - 1);// 棋子行间距
	private final static int QIWIDTH = LINEWIDTH - 2;// (需要偶数)
	private byte[][] qi = new byte[LENGTH][LENGTH];// 棋盘大小
	
	private IAnswerService ias=new AnswerServiceImpl3();

	private JPanel content = new JPanel(null);
	private JPanel left = new JPanel();
	private JPanel right = new JPanel();
	private JRadioButton[] rdb = new JRadioButton[4];// 单选按钮
	private ButtonGroup buttonGroup = new ButtonGroup();
	private String[] btnName = { "双人对战", "初级对战", "中级对战", "高级对战"};
	private JButton button1 = new JButton("开始");
	private JButton button2 = new JButton("悔棋");
	private JTextField txt=new JTextField(8);
	private byte selected = 0;// 储存单选按钮的选择状态 1代表第一个按钮被选中
	private byte color = 1;// 棋子颜色 0代表无子 1 代表黑棋 2代表白棋
	private int answerX = (LENGTH - 1) / 2;// 当前的下子
	private int answerY = (LENGTH - 1) / 2;// 当前下子
	private List<byte[][]> qis = new ArrayList<byte[][]>();// 棋子数组的集合 用于悔棋
	public boolean isStart = false;// 标志,用于判断游戏是否已经开始


	/**
	 * 构造函数
	 */
	public FiveMapD() {
		// 添加单选按钮和按钮
		for (int i = 0; i < rdb.length; i++) {
			rdb[i] = new JRadioButton(btnName[i]);
			buttonGroup.add(rdb[i]);
			right.add(rdb[i]);
		}
		rdb[0].setSelected(true);
		right.add(button1);
		right.add(button2);
		right.add(txt);
		txt.setEnabled(false);
		// 设置棋盘的布局
		left.setBackground(new Color(90, 225, 90));
		left.setBounds(0, 0, MYWIDTH + 100, MYWIDTH + 100);
		right.setBounds(MYWIDTH + 100, 20, 100, MYWIDTH);
		content.add(left);
		content.add(right);
		this.add(content);
		this.setBounds(0, 0, MYWIDTH + 220, MYWIDTH + 80);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addMouseListener(new MyMouseListener());
		// 添加开始按钮的监听器
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = rdb[0].isSelected() ? 1 : selected;
				selected = rdb[1].isSelected() ? 2 : selected;
				selected = rdb[2].isSelected() ? 3 : selected;
				selected = rdb[3].isSelected() ? 4 : selected;
				isStart = true;
				init();
				if (selected == 3 || selected == 4 || selected == 2) {
					qi[answerX][answerY] = 1;
					color = 2;
				}
			}
		});
		// 添加悔棋按钮的监听器
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (qis.size() >= 1) {
					qis.remove(qis.size() - 1);
					if (qis.size() == 0) {
						init();
					} else {
						qi = qis.get(qis.size() - 1);
					}
					answerX = -1;
				}
				repaint();
			}

		});
		//暂时关闭悔棋
		button2.setEnabled(false);
		this.setTitle("五子棋 by howroad V1.5");
	}

	// 初始化棋子
	private void init() {
		for (int i = 0; i < LENGTH; i++) {
			for (int j = 0; j < LENGTH; j++) {
				qi[i][j] = 0;
			}
		}
		color = 1;
		qis.clear();
		answerX = (LENGTH - 1) / 2;
		answerY = (LENGTH - 1) / 2;
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

	// 判断单选按钮选择的是哪一个并执行相应的操作
	public void choice(int choice) {
		// 双人对战
		if (choice == 1) {

		}
		// 初级对战
		if (choice == 2) {
			int[] result = ias.bestAnswer(qi, color);
			answerX = result[0];
			answerY = result[1];
			qi[answerX][answerY] = color;
			repaint();
			if (ias.isWin(qi, color, answerX, answerY)) {
				JOptionPane.showMessageDialog(null, color == 1 ? "黑棋获胜！" : "白棋获胜！");
				isStart = false;
				return;
			}
			color = (byte) (color == 1 ? 2 : 1);
		}
		// 中级对战
		if (choice == 3) {
			int[] result = ias.bestAnswer(qi, color);
			answerX = result[0];
			answerY = result[1];
			qi[answerX][answerY] = color;
			repaint();
			if (ias.isWin(qi, color, answerX, answerY)) {
				JOptionPane.showMessageDialog(null, color == 1 ? "黑棋获胜！" : "白棋获胜！");
				isStart = false;
				return;
			}
			color = (byte) (color == 1 ? 2 : 1);
		}

		// 高级对战
		if (choice == 4) {
			int[] result = ias.bestAnswer(qi, color);
			answerX = result[0];
			answerY = result[1];
			qi[answerX][answerY] = color;
			repaint();
			if (ias.isWin(qi, color, answerX, answerY)) {
				JOptionPane.showMessageDialog(null, color == 1 ? "黑棋获胜！" : "白棋获胜！");
				isStart = false;
				return;
			}
			color = (byte) (color == 1 ? 2 : 1);
		}
	}
	
	//下棋
	public void myDown(int x,int y) {
		answerX=x;
		answerY=y;
		qi[x][y]=color;
		repaint();
		if (ias.isWin(qi, color, answerX, answerY)) {
			JOptionPane.showMessageDialog(null, color == 1 ? "黑棋获胜！" : "白棋获胜！");
			isStart = false;
			return;
		}
		color = (byte) (color == 1 ? 2 : 1);
	}
	//对手下棋
	public void otherDown(int x,int y) {
		answerX=x;
		answerY=y;
		qi[x][y]=color;
		repaint();
		if (ias.isWin(qi, color, answerX, answerY)) {
			JOptionPane.showMessageDialog(null, color == 1 ? "黑棋获胜！" : "白棋获胜！");
			isStart = false;
			return;
		}
		color = (byte) (color == 1 ? 2 : 1);
	}
	// 内部类,用于监听鼠标动作
	public class MyMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (!isStart) {
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
				myDown(answerX,answerY);
				choice(selected);
			} else {
				return;
			}
		}

	}
}
