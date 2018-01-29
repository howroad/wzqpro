/**
 * 
 */
package com.luhao.service;

/**
 * @author howroad
 * @Date 2018年1月24日
 * @version 1.0
 */
public interface IAnswerService {
	public int[] bestAnswer(byte[][] qi, int x);
	public boolean isWin(byte[][] qi, int color, int x, int y);
}
