/**
 * 
 */
package com.luhao.bean;

import com.luhao.vo.FiveVO;

/**
 * @author howroad
 * @Date 2018年1月28日
 * @version 1.0
 */
public class FivePO {
	private int id;
	private int userId;
	private int win;
	private int lost;
	private int sum;
	private int socore;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWin() {
		return win;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public int getLost() {
		return lost;
	}
	public void setLost(int lost) {
		this.lost = lost;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public int getSocore() {
		return socore;
	}
	public void setSocore(int socore) {
		this.socore = socore;
	}

	public FiveVO toFiveVO() {
		FiveVO fv=new FiveVO();
		fv.setId(id);
		fv.setUserId(userId);
		fv.setWin(win);
		fv.setLost(lost);
		fv.setSum(sum);
		fv.setSocore(socore);
		return fv;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userid) {
		this.userId = userid;
	}
}
