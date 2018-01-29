package com.luhao.seivice.impl;

/**
 * 小学生版本
 * 这个版本只能使用15*15的棋盘
 */
import java.util.Arrays;

import com.luhao.service.IAnswerService;

public class AnswerServiceImpl1 implements IAnswerService {
	/**
	 * 获得颜色为x的最优解
	 * 
	 * @param qi 棋子数组
	 * @param x 棋子颜色
	 * @return 最优解数组
	 */
	public int[] bestAnswer(byte[][] qi, int x) {

		int y = x == 1 ? 2 : 1;
		int[] result = new int[2];
		int minX = qi.length - 1;
		int minY = qi.length - 1;
		int maxX = -1;
		int maxY = -1;
		int maxScore = -1;

		for (int i = 0; i < qi.length; i++) {
			for (int j = 0; j < qi[i].length; j++) {
				if (qi[i][j] != 0) {
					minX = minX < i ? minX : i;
					minY = minY < j ? minY : j;
					maxX = maxX > i ? maxX : i;
					maxY = maxY > j ? maxY : j;
				}

			}
		}

		// 没找到x颜色的棋子
		if (maxX == -1) {
			// do {
			// result[0] = (int) (Math.random() * 15);
			// result[1] = (int) (Math.random() * 15);
			// } while (qi[result[0]][result[1]] != 0);
			result[0] = 7;
			result[1] = 7;
			return result;
		}
		// 判断求解范围
		minX = minX < 2 ? 0 : minX - 2;
		maxX = maxX > qi.length - 2 ? qi.length - 1 : maxX + 2;
		minY = minY < 2 ? 0 : minY - 2;
		maxY = maxY > qi.length - 2 ? qi.length - 1 : maxY + 2;
		int[] temp = { -1, -1, -1 };
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				if (qi[i][j] != 0) {
					continue;
				}

				int[] temp1 = Arrays.copyOf(getanswer(qi, x, i, j), 3);// 防守
				int[] temp2 = Arrays.copyOf(getanswer(qi, y, i, j), 3);// 进攻

				// 进攻分数

				int attackScore = temp2[0] == -1 ? -1 : temp2[0] + 1;
				// 防守分数
				int defenseScore = temp1[0];

				// 该位置最高得分
				int nowMax = attackScore > defenseScore ? attackScore : defenseScore;
				// System.out.println(nowMax);
				temp = attackScore > defenseScore ? Arrays.copyOf(temp2, 3) : Arrays.copyOf(temp1, 3);

				// System.out.println(temp[0]);
				if (nowMax > maxScore) {
					maxScore = nowMax;
					result[0] = temp[1];
					result[1] = temp[2];
				}
			}
		}
//		System.out.println("最高分数为" + maxScore);
		if (maxScore != -1) {
			// System.out.println("坐标为:" + result[0] + "," + result[1]);
			qi[result[0]][result[1]] = (byte) x;
			return result;
		}
//		System.out.println("看不出来,我随便下一个吧");
		do {
			result[0] = (int) (Math.random() * (maxX - minX) + minX);
			result[1] = (int) (Math.random() * (maxY - minY) + minY);
		} while (qi[result[0]][result[1]] != 0);
		return result;

	}

	/**
	 * 判断是否胜利
	 * 
	 * @param qi 棋子数组
	 * @param x 棋子颜色
	 * @param i 刚下子的坐标（为了减少运算）
	 * @param j 刚下子的坐标（为了减少运算）
	 * @return 是否胜利
	 */
	public boolean isWin(byte[][] qi, int x, int i, int j) {
		return checkWin1(qi, x, i, j)[0] >= 5 || checkWin2(qi, x, i, j)[0] >= 5 || checkWin3(qi, x, i, j)[0] >= 5
				|| checkWin4(qi, x, i, j)[0] >= 5;

	}

	/**
	 * 判断这个位置下x颜色的棋子的得分
	 * 
	 * @param qi 棋子数组
	 * @param color 颜色
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @return int[0]分数 int[1]横坐标（i） int[j]纵坐标（j）
	 */
	public int[] getanswer(byte[][] qi, int color, int x, int y) {
		int[] result = new int[3];

		int[] r1 = Arrays.copyOf(checkWin1(qi, color, x, y), 5);
		int[] r2 = Arrays.copyOf(checkWin2(qi, color, x, y), 5);
		int[] r3 = Arrays.copyOf(checkWin3(qi, color, x, y), 5);
		int[] r4 = Arrays.copyOf(checkWin4(qi, color, x, y), 5);

		// 1.判断为直接胜利 得分10
		if (r1[0] >= 5 || r2[0] >= 5 || r3[0] >= 5 || r4[0] >= 5) {
			result[0] = 10;
			result[1] = x;
			result[2] = y;
			return result;
		}
		// 2.判断为活4 得分 8
		if (r1[0] == 4 && r1[2] != -1 && r1[4] != -1 || r2[0] == 4 && r2[2] != -1 && r2[4] != -1
				|| r3[0] == 4 && r3[2] != -1 && r3[4] != -1 || r4[0] == 4 && r4[2] != -1 && r4[4] != -1) {
			result[0] = 8;
			result[1] = x;
			result[2] = y;
			return result;
		}
		// 3.判断为 双死四 得分8
		if (r1[0] == 4 && r2[0] == 4 && (r1[2] + r1[4] != -2 && r2[2] + r2[4] != -2)
				|| r1[0] == 4 && r3[0] == 4 && (r1[2] + r1[4] != -2 && r3[2] + r3[4] != -2)
				|| r1[0] == 4 && r4[0] == 4 && (r1[2] + r1[4] != -2 && r4[2] + r4[4] != -2)
				|| r2[0] == 4 && r3[0] == 4 && (r2[2] + r2[4] != -2 && r3[2] + r3[4] != -2)
				|| r2[0] == 4 && r4[0] == 4 && (r2[2] + r2[4] != -2 && r4[2] + r4[4] != -2)
				|| r3[0] == 4 && r4[0] == 4 && (r3[2] + r3[4] != -2 && r4[2] + r4[4] != -2)) {
			result[0] = 8;
			result[1] = x;
			result[2] = y;
			return result;
		}
		// 4.判断为不连双死四 得分(不会写)

		// 5.判断为 死四+活三 得分8
		if (r1[0] == 4 && r1[2] + r1[4] != -2 && r2[0] == 3 && r2[2] != -1 && r2[4] != -1
				|| r1[0] == 4 && r1[2] + r1[4] != -2 && r3[0] == 3 && r3[2] != -1 && r3[4] != -1
				|| r1[0] == 4 && r1[2] + r1[4] != -2 && r4[0] == 3 && r4[2] != -1 && r4[4] != -1
				|| r2[0] == 4 && r2[2] + r2[4] != -2 && r3[0] == 3 && r3[2] != -1 && r3[4] != -1
				|| r2[0] == 4 && r2[2] + r2[4] != -2 && r4[0] == 3 && r4[2] != -1 && r4[4] != -1
				|| r3[0] == 4 && r3[2] + r3[4] != -2 && r4[0] == 3 && r4[2] != -1 && r4[4] != -1) {
			result[0] = 8;
			result[1] = x;
			result[2] = y;
			return result;
		}
		// 6.判断为 双活3 得分+6
		if (r1[0] == 3 && r1[2] != -1 && r1[4] != -1 && r2[0] == 3 && r2[2] != -1 && r2[4] != -1
				|| r1[0] == 3 && r1[2] != -1 && r1[4] != -1 && r3[0] == 3 && r3[2] != -1 && r3[4] != -1
				|| r1[0] == 3 && r1[2] != -1 && r1[4] != -1 && r4[0] == 3 && r4[2] != -1 && r4[4] != -1
				|| r2[0] == 3 && r2[2] != -1 && r2[4] != -1 && r3[0] == 3 && r3[2] != -1 && r3[4] != -1
				|| r2[0] == 3 && r2[2] != -1 && r2[4] != -1 && r4[0] == 3 && r4[2] != -1 && r4[4] != -1
				|| r3[0] == 3 && r3[2] != -1 && r3[4] != -1 && r4[0] == 3 && r4[2] != -1 && r4[4] != -1) {
			result[0] = 6;
			result[1] = x;
			result[2] = y;
			return result;
		}
		// 单死4，得分＋4
		if (r1[0] == 4 && (r1[2] != -1 || r1[4] != -1) || r2[0] == 4 && (r2[2] != -1 || r2[4] != -1)
				|| r3[0] == 4 && (r3[2] != -1 || r3[4] != -1) || r4[0] == 4 && (r4[2] != -1 || r4[4] != -1)) {
			result[0] = 4;
			result[1] = x;
			result[2] = y;
//			System.out.println("这里出现情况！！");
//			System.out.println("r1" + r1[0] + r1[1] + r1[2] + r1[3] + r1[4]);
//			System.out.println("r2" + r2[0] + r2[1] + r2[2] + r2[3] + r2[4]);
//			System.out.println("r3" + r3[0] + r3[1] + r3[2] + r3[3] + r3[4]);
//			System.out.println("r4" + r4[0] + r4[1] + r4[2] + r4[3] + r4[4]);
			return result;
		}

		// 判断单活3 得分＋4
		if (r1[0] == 3 && r1[2] != -1 && r1[4] != -1 || r2[0] == 3 && r2[2] != -1 && r2[4] != -1
				|| r3[0] == 3 && r3[2] != -1 && r3[4] != -1 || r4[0] == 3 && r4[2] != -1 && r4[4] != -1) {
			result[0] = 4;
			result[1] = x;
			result[2] = y;
			return result;
		}
		result[0] = -1;
		result[1] = -1;
		result[2] = -1;
		return result;
	}

	public int[] checkWin1(byte[][] qi, int x, int i, int j) {
		int[] result = new int[5];
		int x1 = i;
		int y1 = j;
		int flag = -1;
		while (true) {
			flag++;
			x1++;
			if (x1 < 0 || x1 > qi.length - 1) {
				result[1] = -1;
				result[2] = -1;
				break;
			}
			if (qi[x1][y1] == 0) {
				result[1] = x1;
				result[2] = y1;
				break;
			} else if (qi[x1][y1] == x) {
				continue;
			} else {
				result[1] = -1;
				result[2] = -1;
				break;
			}
		}
		x1 = i;
		y1 = j;
		while (true) {
			flag++;
			x1--;
			if (x1 < 0 || x1 > qi.length - 1) {
				result[3] = -1;
				result[4] = -1;
				break;
			}
			if (qi[x1][y1] == 0) {
				result[3] = x1;
				result[4] = y1;
				break;
			} else if (qi[x1][y1] == x) {
				continue;
			} else {
				result[3] = -1;
				result[4] = -1;
				break;
			}
		}
		result[0] = flag;
		return result;
	}

	public int[] checkWin2(byte[][] qi, int x, int i, int j) {
		int[] result = new int[5];
		int x1 = i;
		int y1 = j;
		int flag = -1;
		while (true) {
			flag++;
			y1++;
			if (y1 < 0 || y1 > qi.length - 1) {
				result[1] = -1;
				result[2] = -1;
				break;
			}
			if (qi[x1][y1] == 0) {
				result[1] = x1;
				result[2] = y1;
				break;
			} else if (qi[x1][y1] == x) {
				continue;
			} else {
				result[1] = -1;
				result[2] = -1;
				break;
			}
		}
		x1 = i;
		y1 = j;
		while (true) {
			flag++;
			y1--;
			if (y1 < 0 || y1 > qi.length - 1) {
				result[3] = -1;
				result[4] = -1;
				break;
			}
			if (qi[x1][y1] == 0) {
				result[3] = x1;
				result[4] = y1;
				break;
			} else if (qi[x1][y1] == x) {
				continue;
			} else {
				result[3] = -1;
				result[4] = -1;
				break;
			}
		}
		result[0] = flag;
		return result;
	}

	public int[] checkWin3(byte[][] qi, int x, int i, int j) {
		int[] result = new int[5];
		int x1 = i;
		int y1 = j;
		int flag = -1;
		while (true) {
			flag++;
			x1++;
			y1++;
			if (x1 < 0 || x1 > qi.length - 1 || y1 < 0 || y1 > qi.length - 1) {
				result[1] = -1;
				result[2] = -1;
				break;
			}
			if (qi[x1][y1] == 0) {
				result[1] = x1;
				result[2] = y1;
				break;
			} else if (qi[x1][y1] == x) {
				continue;
			} else {
				result[1] = -1;
				result[2] = -1;
				break;
			}
		}
		x1 = i;
		y1 = j;
		while (true) {
			flag++;
			x1--;
			y1--;
			if (x1 < 0 || x1 > qi.length - 1 || y1 < 0 || y1 > qi.length - 1) {
				result[3] = -1;
				result[4] = -1;
				break;
			}
			if (qi[x1][y1] == 0) {
				result[3] = x1;
				result[4] = y1;
				break;
			} else if (qi[x1][y1] == x) {
				continue;
			} else {
				result[3] = -1;
				result[4] = -1;
				break;
			}
		}
		result[0] = flag;
		return result;
	}

	public int[] checkWin4(byte[][] qi, int x, int i, int j) {
		int[] result = new int[5];
		int x1 = i;
		int y1 = j;
		int flag = -1;
		while (true) {
			flag++;
			x1++;
			y1--;
			if (x1 < 0 || x1 > qi.length - 1 || y1 < 0 || y1 > qi.length - 1) {
				result[1] = -1;
				result[2] = -1;
				break;
			}
			if (qi[x1][y1] == 0) {
				result[1] = x1;
				result[2] = y1;
				break;
			} else if (qi[x1][y1] == x) {
				continue;
			} else {
				result[1] = -1;
				result[2] = -1;
				break;
			}
		}
		x1 = i;
		y1 = j;
		while (true) {
			flag++;
			x1--;
			y1++;
			if (x1 < 0 || x1 > qi.length - 1 || y1 < 0 || y1 > qi.length - 1) {
				result[3] = -1;
				result[4] = -1;
				break;
			}
			if (qi[x1][y1] == 0) {
				result[3] = x1;
				result[4] = y1;
				break;
			} else if (qi[x1][y1] == x) {
				continue;
			} else {
				result[3] = -1;
				result[4] = -1;
				break;
			}
		}
		result[0] = flag;
		return result;
	}


}
