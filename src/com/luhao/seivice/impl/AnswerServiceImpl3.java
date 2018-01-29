package com.luhao.seivice.impl;

/**
 * @version 3.0
 */
import java.util.Arrays;
/**
 * 还有一种情况没有考虑到  一条横线构成44连这种情况(01_1_1_10)概率较小,暂时忽略
 * 活四和53重复时候有时候会出错,可能改好了
 * 有时候判断的分数并不是该子落下时能额外获得的分数
 */
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import com.luhao.service.IAnswerService;

public class AnswerServiceImpl3 implements IAnswerService{
	/**
	 * 用来判断求解范围,不至于全盘遍历浪费时间,判断范围为下子的矩形向外拓宽两格
	 * 
	 * @param qi 棋子的数组,下同
	 * @return XY坐标分别的最大值最小值
	 */
	private   int[] getRange(byte[][] qi) {
		int length = qi.length;
		int[] range = { -1, -1, -1, -1 };
		int minX = length;
		int minY = length;
		int maxX = -1;
		int maxY = -1;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (qi[i][j] == 0) {
					continue;
				}
				minX = minX < i ? minX : i;
				minY = minY < j ? minY : j;
				maxX = maxX > i ? maxX : i;
				maxY = maxY > j ? maxY : j;
			}
		}
		// 如果棋盘没有棋子
		if (maxX == -1) {
			return range;
		}
		// 判断求解范围(上下左右各向外2格)
		range[0] = minX < 2 ? 0 : minX - 2;
		range[1] = minY < 2 ? 0 : minY - 2;
		range[2] = maxX > length - 3 ? length - 1 : maxX + 2;
		range[3] = maxY > length - 3 ? length - 1 : maxY + 2;
		return range;
	}

	/**
	 * 求解最优解
	 * 
	 * @param qi 棋子的数组,下同
	 * @param color 颜色
	 * @return 棋子的坐标的数组
	 */
	public int[] bestAnswer(byte[][] qi, int color) {
		int[] result = { -1, -1 };// 用来保存结果
		int length = qi.length;
		int[] range = getRange(qi);
		int minX = range[0];
		int minY = range[1];
		int maxX = range[2];
		int maxY = range[3];
		int[] maxScore = { -1, -1, -1 };// 用来保存分数

		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				if (qi[i][j] != 0) {
					continue;
				}
				// 获得该位置棋子的得分
				int nowScore = getScore(qi, color, i, j);
				if (maxScore[0] < nowScore) {
					maxScore[0] = nowScore;
					maxScore[1] = i;
					maxScore[2] = j;
				}
			}

		}
		if (maxScore[0] <= 2) {
			// System.out.println("随机下棋");
			if (qi[length / 2][length / 2] == 0) {
				result[0] = result[1] = length / 2;
				return result;
			}
			int loopnum = 0;
			do {
				result[0] = (int) (Math.random() * (maxX - minX) + minX);
				result[1] = (int) (Math.random() * (maxY - minY) + minY);
				if (++loopnum >= 5000) {
					JOptionPane.showMessageDialog(null, "你把电脑下崩了!");
					System.exit(0);
				}
			} while (qi[result[0]][result[1]] != 0);
			return result;
		}
		System.out.println("最高分数" + maxScore[0]);
		result[0] = maxScore[1];
		result[1] = maxScore[2];
		return result;
	}

	/**
	 * 获得该点的得分
	 * 
	 * @param qi
	 * @param color
	 * @param x
	 * @param y
	 * @return
	 */
	public   int getScore(byte[][] qi, int color, int x, int y) {
		int score = 0;
		int otherColor = color == 1 ? 2 : 1;
		// 直接胜利
		if (isWin(qi, color, x, y)) {
			return 100000;
		}
		if (isWin(qi, otherColor, x, y)) {
			return 90000;
		}

		Set<Integer> h4 = getTwoAliveFour(qi, color, x, y);// 活4
		Set<Integer> h3 = getTwoAliveThree(qi, color, x, y);// 活3
		Set<Integer> h2 = getTwoAliveTwo(qi, color, x, y);// 活二,差一步活三(包括01010不包括010010)
		Set<Integer> s4 = need1ToWin(qi, color, x, y);// 死四
		Set<Integer> s3 = need2ToWin(qi, color, x, y);// 死三,差两步5子
		boolean lh3 = lian3Alive(qi, color, x, y).size() > 0;// 连活3
		boolean ls4 = lian4Death(qi, color, x, y);// 连死4
		Set<Integer> otherh4 = getTwoAliveFour(qi, otherColor, x, y);// 活4
		Set<Integer> otherh3 = getTwoAliveThree(qi, otherColor, x, y);// 活3
		Set<Integer> otherh2 = getTwoAliveTwo(qi, otherColor, x, y);
		Set<Integer> others4 = need1ToWin(qi, otherColor, x, y);// 死四
		Set<Integer> others3 = need2ToWin(qi, otherColor, x, y);
		boolean otherlh3 = lian3Alive(qi, otherColor, x, y).size() > 0;// 连活3
		boolean otherls4 = lian4Death(qi, otherColor, x, y);// 连死4

		// 构成自己活四,双死四,死四活三(必胜),优先下活四
		if (h4.size() > 0 || s4.size() > 1
				|| s4.size() > 0 && h3.size() > 0 && (!s4.containsAll(h3) || !h3.containsAll(s4))) {
			score += 10000;
			if (h4.size() > 0) {
				score += 500;
			}
		}
		// 构成敌人活四,双死四,死四活三,如果自己有死四进攻棋就优先下,否则优先堵对方活四
		if (otherh4.size() > 0 || others4.size() > 1 || others4.size() > 0 && otherh3.size() > 0
				&& (!others4.containsAll(otherh3) || !otherh3.containsAll(others4))) {
			if (s4.size() > 0) {
				score += 500;
			}
			if (otherh4.size() > 0) {
				score += 200;
			}
			score += 9000;
		}

		// 自己双活三
		if (h3.size() > 1) {
			score += 100;
		}
		// 敌人双活三,如果自己有四个的可以试一下
		if (otherh3.size() > 1) {
			score += 90;
		}
		// 为了保证每个点的分数尽可能的不一样,多判断一些
		// 根据我的思路还应该满足h3+h2<lh3,s4+s3<ls4
		// ls4 > s4 > lh3 > h3 > otherlh3 > otherh3 > otherls4 > others4 > h2*2 >otherh2*2 > s3 > others3 > h2 > otherh2
		// 58	 50   46	40      35        30         25         20      10     8          7     6       5       4
		//这个分数的顺序可能不是很好，比如说死四和活三的分数 ，死三和活二的分数谁大谁小，见仁见智。
		// 连死四或者普通死四
		if (s4.size() > 0) {
			if (ls4) {
				score += 8;
			}
			score += 50;
			//score += 9200;//凶狠打法
		}
		// 连活三或普通活三
		if (s3.size() > 0) {
			if (lh3) {
				score += 39;
			} else if (h3.size() > 0) {
				score += 33;
			}
			score += 7 * s3.size();
		}

		// 敌人的连死四或者死四
		if (others4.size() > 0) {
			if (otherls4) {
				score += 5;
			}
			score += 20;
		}
		// 敌人的连活三或者活三
		if (others3.size() > 0) {
			if (otherlh3) {
				score += 29;
			} else if (otherh3.size() > 0) {
				score += 24;
			}
			score += 6 * others3.size();

		}
		// 剩余情况计算得分

		score += 5 * h2.size() + 4 * otherh2.size();
		return score;
	}

	/**
	 * 连死四,四子相连,且至少有一头为空
	 * 
	 * @param qi
	 * @param color
	 * @param x
	 * @param y
	 * @return 没有四子的情况返回null,有的话返回方向
	 */
	public   boolean lian4Death(byte[][] qi, int color, int x, int y) {
		int[] r1 = checkPoint1(qi, color, x, y);
		int[] r2 = checkPoint2(qi, color, x, y);
		int[] r3 = checkPoint3(qi, color, x, y);
		int[] r4 = checkPoint4(qi, color, x, y);
		// Set<Integer> result = new HashSet<Integer>();
		if (r1[0] == 4 && (r1[2] == 0 || r1[3] == 0)) {
			return true;
		}
		if (r2[0] == 4 && (r2[2] == 0 || r2[3] == 0)) {
			return true;
		}
		if (r3[0] == 4 && (r3[2] == 0 || r3[3] == 0)) {
			return true;
		}
		if (r4[0] == 4 && (r4[2] == 0 || r4[3] == 0)) {
			return true;
		}
		return false;
	}

	/**
	 * 连活三:三子相连,两头为空
	 * 
	 * @param qi
	 * @param color
	 * @param x
	 * @param y
	 * @return 没有四子的情况返回null,有的话返回方向
	 */
	public   Set<Integer> lian3Alive(byte[][] qi, int color, int x, int y) {
		int[] r1 = checkPoint1(qi, color, x, y);
		int[] r2 = checkPoint2(qi, color, x, y);
		int[] r3 = checkPoint3(qi, color, x, y);
		int[] r4 = checkPoint4(qi, color, x, y);
		Set<Integer> result = new HashSet<Integer>();
		if (r1[0] == 3 && r1[2] == 0 && r1[3] == 0) {
			result.add(1);
		}
		if (r2[0] == 3 && r2[2] == 0 && r2[3] == 0) {
			result.add(2);
		}
		if (r3[0] == 3 && r3[2] == 0 && r3[3] == 0) {
			result.add(3);
		}
		if (r4[0] == 3 && r4[2] == 0 && r4[3] == 0) {
			result.add(4);
		}
		return result;
	}

	/**
	 * 差一个子构成活三,有可能是连活二,有可能是跳活二
	 * 
	 * @param qi
	 * @param color
	 * @param x
	 * @param y
	 * @return 返回哪个方向存在活二
	 */
	public   Set<Integer> getTwoAliveTwo(byte[][] qi, int color, int x, int y) {
		// 新建一个棋子的副本
		int length = qi.length;
		byte[][] temp = new byte[length][length];
		for (int i = 0; i < qi.length; i++) {
			for (int j = 0; j < qi[i].length; j++) {
				temp[i][j] = qi[i][j];
			}
		}
		Set<Integer> result = new HashSet<Integer>();
		temp[x][y] = (byte) color;
		int[] range = getRange(temp);
		int minX = range[0];
		int minY = range[1];
		int maxX = range[2];
		int maxY = range[3];
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				if (temp[i][j] != 0) {
					continue;
				}
				result.addAll(lian3Alive(temp, color, i, j));
			}
		}
		return result;
	}

	/**
	 * 活3(包括连活三和跳活三),即差一个子构成连活四
	 * 
	 * @param qi
	 * @param color
	 * @param x
	 * @param y
	 * @return 返回哪个方向存在活三
	 */
	public   Set<Integer> getTwoAliveThree(byte[][] qi, int color, int x, int y) {
		// 新建一个棋子的副本
		int length = qi.length;
		byte[][] temp = new byte[length][length];
		for (int i = 0; i < qi.length; i++) {
			for (int j = 0; j < qi[i].length; j++) {
				temp[i][j] = qi[i][j];
			}
		}
		Set<Integer> result = new HashSet<Integer>();
		temp[x][y] = (byte) color;
		int[] range = getRange(temp);
		int minX = range[0];
		int minY = range[1];
		int maxX = range[2];
		int maxY = range[3];
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				if (temp[i][j] != 0) {
					continue;
				}
				result.addAll(getTwoAliveFour(temp, color, i, j));
			}
		}
		return result;
	}

	/**
	 * 活四:相连四个子,并且两头为空
	 * 
	 * @param qi
	 * @param color
	 * @param x
	 * @param y
	 * @return 返回哪个方向存在活四
	 */
	public   Set<Integer> getTwoAliveFour(byte[][] qi, int color, int x, int y) {
		int[] r1 = checkPoint1(qi, color, x, y);
		int[] r2 = checkPoint2(qi, color, x, y);
		int[] r3 = checkPoint3(qi, color, x, y);
		int[] r4 = checkPoint4(qi, color, x, y);
		Set<Integer> result = new HashSet<Integer>();
		if (Arrays.equals(r1, new int[] { 4, 1, 0, 0 })) {
			result.add(1);
		}
		if (Arrays.equals(r2, new int[] { 4, 2, 0, 0 })) {
			result.add(2);
		}
		if (Arrays.equals(r3, new int[] { 4, 3, 0, 0 })) {
			result.add(3);
		}
		if (Arrays.equals(r4, new int[] { 4, 4, 0, 0 })) {
			result.add(4);
		}
		return result;

	}

	/**
	 * 三子的情况(包括死三活三跳3)
	 * 
	 * @param qi
	 * @param color
	 * @param x
	 * @param y
	 * @return 没有四子的情况返回null,有的话返回方向
	 */
	public   Set<Integer> need2ToWin(byte[][] qi, int color, int x, int y) {
		// 新建一个棋子的副本
		int length = qi.length;
		byte[][] temp = new byte[length][length];
		for (int i = 0; i < qi.length; i++) {
			for (int j = 0; j < qi[i].length; j++) {
				temp[i][j] = qi[i][j];
			}
		}
		Set<Integer> result = new HashSet<Integer>();
		temp[x][y] = (byte) color;
		int[] range = getRange(temp);
		int minX = range[0];
		int minY = range[1];
		int maxX = range[2];
		int maxY = range[3];
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				if (temp[i][j] != 0) {
					continue;
				}
				result.addAll(need1ToWin(temp, color, i, j));
			}
		}
		return result;
	}

	/**
	 * 差一个子构成五子,活长连,不包括被封死的死四
	 * 
	 * @param qi
	 * @param color
	 * @param x
	 * @param y
	 * @return 没有四子的情况返回null,有的话返回方向
	 */
	public   Set<Integer> need1ToWin(byte[][] qi, int color, int x, int y) {
		// 新建一个棋子的副本
		int length = qi.length;
		byte[][] temp = new byte[length][length];
		for (int i = 0; i < qi.length; i++) {
			for (int j = 0; j < qi[i].length; j++) {
				temp[i][j] = qi[i][j];
			}
		}
		Set<Integer> result = new HashSet<Integer>();
		temp[x][y] = (byte) color;
		int[] range = getRange(temp);
		int minX = range[0];
		int minY = range[1];
		int maxX = range[2];
		int maxY = range[3];
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j < maxY; j++) {
				if (temp[i][j] != 0) {
					continue;
				}
				if (checkPoint1(temp, color, i, j)[0] >= 5) {
					result.add(1);
				}
				if (checkPoint2(temp, color, i, j)[0] >= 5) {
					result.add(2);
				}
				if (checkPoint3(temp, color, i, j)[0] >= 5) {
					result.add(3);
				}
				if (checkPoint4(temp, color, i, j)[0] >= 5) {
					result.add(4);
				}
			}
		}
		return result;
	}

	/**
	 * 该子落下胜利
	 * 
	 * @param qi
	 * @param color
	 * @param x
	 * @param y
	 * @return
	 */
	public   boolean isWin(byte[][] qi, int color, int x, int y) {
		return checkPoint1(qi, color, x, y)[0] >= 5 || checkPoint2(qi, color, x, y)[0] >= 5
				|| checkPoint3(qi, color, x, y)[0] >= 5 || checkPoint4(qi, color, x, y)[0] >= 5;

	}

	/**
	 * 传入棋子数组/颜色/坐标,分析这个点(这是分析的第一步)(下同)
	 * 
	 * @param qi
	 *            传入的棋子数组
	 * @param color
	 *            需要分析的改点的颜色
	 * @param x
	 *            该点的横坐标
	 * @param y
	 *            该点的纵坐标
	 * @return result[1/2/3/4]分别表示该子落下后:相连的棋子个数/判断的方向/两边棋子的状态0表示无子,-1表示边界或者其他颜色的棋子
	 */
	public   int[] checkPoint1(byte[][] qi, int color, int x, int y) {
		int[] result = { -1, -1, -1, -1 };// 将结果放在这个数组里
		int length = qi.length;// 用来保存棋盘的长
		int flag = 1;// 用来计数
		int tempX = x;// 用来参与判断的坐标
		int tempY = y;
		while (true) {// 向左边依次判断
			tempX--;
			if (tempX < 0 || tempX > length - 1) {// 遇到棋盘边缘
				result[2] = -1;
				break;
			}
			if (qi[tempX][tempY] == 0) {// 遇到空位置
				result[2] = 0;
				break;
			}
			if (qi[tempX][tempY] == color) {// 遇到相同颜色
				flag++;
				continue;
			}
			result[2] = -1;// 遇到不同颜色
			break;
		}
		tempX = x;
		tempY = y;
		while (true) {// 向左边依次判断
			tempX++;
			if (tempX < 0 || tempX > length - 1) {// 遇到棋盘边缘
				result[3] = -1;
				break;
			}
			if (qi[tempX][tempY] == 0) {// 遇到空位置
				result[3] = 0;
				break;
			}
			if (qi[tempX][tempY] == color) {// 遇到相同颜色
				flag++;
				continue;
			}
			result[3] = -1;// 遇到不同颜色
			break;
		}
		result[0] = flag;
		result[1] = 1;
		// System.out.println(""+result[0]+result[1]+result[2]+result[3]);
		return result;
	}

	public   int[] checkPoint2(byte[][] qi, int color, int x, int y) {
		int[] result = { -1, -1, -1, -1 };// 将结果放在这个数组里
		int length = qi.length;// 用来保存棋盘的长
		int flag = 1;// 用来计数
		int tempX = x;// 用来参与判断的坐标
		int tempY = y;
		while (true) {// 向左边依次判断
			tempY--;
			if (tempY < 0 || tempY > length - 1) {// 遇到棋盘边缘
				result[2] = -1;
				break;
			}
			if (qi[tempX][tempY] == 0) {// 遇到空位置
				result[2] = 0;
				break;
			}
			if (qi[tempX][tempY] == color) {// 遇到相同颜色
				flag++;
				continue;
			}
			result[2] = -1;// 遇到不同颜色
			break;
		}
		tempX = x;
		tempY = y;
		while (true) {// 向左边依次判断
			tempY++;
			if (tempY < 0 || tempY > length - 1) {// 遇到棋盘边缘
				result[3] = -1;
				break;
			}
			if (qi[tempX][tempY] == 0) {// 遇到空位置
				result[3] = 0;
				break;
			}
			if (qi[tempX][tempY] == color) {// 遇到相同颜色
				flag++;
				continue;
			}
			result[3] = -1;// 遇到不同颜色
			break;
		}
		result[0] = flag;
		result[1] = 2;
		// System.out.println(""+result[0]+result[1]+result[2]+result[3]);
		return result;
	}

	public   int[] checkPoint3(byte[][] qi, int color, int x, int y) {
		int[] result = { -1, -1, -1, -1 };// 将结果放在这个数组里
		int length = qi.length;// 用来保存棋盘的长
		int flag = 1;// 用来计数
		int tempX = x;// 用来参与判断的坐标
		int tempY = y;
		while (true) {// 向左边依次判断
			tempX--;
			tempY--;
			if (tempX < 0 || tempX > length - 1 || tempY < 0 || tempY > length - 1) {// 遇到棋盘边缘
				result[2] = -1;
				break;
			}
			if (qi[tempX][tempY] == 0) {// 遇到空位置
				result[2] = 0;
				break;
			}
			if (qi[tempX][tempY] == color) {// 遇到相同颜色
				flag++;
				continue;
			}
			result[2] = -1;// 遇到不同颜色
			break;
		}
		tempX = x;
		tempY = y;
		while (true) {// 向左边依次判断
			tempX++;
			tempY++;
			if (tempX < 0 || tempX > length - 1 || tempY < 0 || tempY > length - 1) {// 遇到棋盘边缘
				result[3] = -1;
				break;
			}
			if (qi[tempX][tempY] == 0) {// 遇到空位置
				result[3] = 0;
				break;
			}
			if (qi[tempX][tempY] == color) {// 遇到相同颜色
				flag++;
				continue;
			}
			result[3] = -1;// 遇到不同颜色
			break;
		}
		result[0] = flag;
		result[1] = 3;
		// System.out.println(""+result[0]+result[1]+result[2]+result[3]);
		return result;
	}

	public int[] checkPoint4(byte[][] qi, int color, int x, int y) {
		int[] result = { -1, -1, -1, -1 };// 将结果放在这个数组里
		int length = qi.length;// 用来保存棋盘的长
		int flag = 1;// 用来计数
		int tempX = x;// 用来参与判断的坐标
		int tempY = y;
		while (true) {// 向左边依次判断
			tempX--;
			tempY++;
			if (tempX < 0 || tempX > length - 1 || tempY < 0 || tempY > length - 1) {// 遇到棋盘边缘
				result[2] = -1;
				break;
			}
			if (qi[tempX][tempY] == 0) {// 遇到空位置
				result[2] = 0;
				break;
			}
			if (qi[tempX][tempY] == color) {// 遇到相同颜色
				flag++;
				continue;
			}
			result[2] = -1;// 遇到不同颜色
			break;
		}
		tempX = x;
		tempY = y;
		while (true) {// 向左边依次判断
			tempX++;
			tempY--;
			if (tempX < 0 || tempX > length - 1 || tempY < 0 || tempY > length - 1) {// 遇到棋盘边缘
				result[3] = -1;
				break;
			}
			if (qi[tempX][tempY] == 0) {// 遇到空位置
				result[3] = 0;
				break;
			}
			if (qi[tempX][tempY] == color) {// 遇到相同颜色
				flag++;
				continue;
			}
			result[3] = -1;// 遇到不同颜色
			break;
		}
		result[0] = flag;
		result[1] = 4;
		// System.out.println(""+result[0]+result[1]+result[2]+result[3]);
		return result;
	}

}
