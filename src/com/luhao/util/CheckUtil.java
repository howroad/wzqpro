/**
 * 
 */
package com.luhao.util;

/**
 * @author howroad
 * @Date 2018年1月16日
 * @version 1.0
 */

public class CheckUtil {

	// 检查是否为空
	public static boolean checkExsit(String str) {
		if (str == null) {
			return false;
		}
		if (str.equals("")) {
			return false;
		}
		return true;

	}

	// 检查是否为时间
	public static boolean checkDate(String str) {
		if (!checkExsit(str)) {
			return false;
		}
		return str.matches("([1][9]\\d{2}|[2][01234]\\d{2})-([0]\\d|[1][012])-([012]\\d|[3][01])");
	}

	// 检查是否为电话号码
	public static boolean checkTel(String str) {
		if (!checkExsit(str)) {
			return false;
		}
		return str.matches("([+-]|\\d){8,15}");
	}

	// 检查是否为数字
	public static boolean checkNum(String str) {
		if (!checkExsit(str)) {
			return false;
		}
		return str.matches("\\d{1,8}");

	}
	// 检查是否有特殊符号
	public static boolean checkFH(String str) {
		if (!checkExsit(str)) {
			return false;
		}
		return !str.matches("[<>]+");

	}
}
