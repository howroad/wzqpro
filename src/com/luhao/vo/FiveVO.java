/**
 * 
 */
package com.luhao.vo;

/**
 * @author howroad
 * @Date 2018年1月28日
 * @version 1.0
 */
public class FiveVO {
	private int id;
	private int userId;
	private String userNickName;
	private int win;
	private int lost;
	private int ping;
	private int taopao;
	private int sum;
	private int socore;
	
	
	private double shenglv;
	private double duanxianlv;
	private String duanwei;
	
	public void init() {
		this.sum=win+lost+ping+taopao;
		this.shenglv=((double)win)/sum;
		this.duanxianlv=((double)taopao)/sum;
		this.socore=win-lost-10*taopao;
		if(socore>1000) {
			duanwei="8段";
		}
		else if(socore>750) {
			duanwei="7段";
		}
		else if(socore>500) {
			duanwei="6段";
		}
		else if(socore>200) {
			duanwei="5段";
		}
		else if(socore>100) {
			duanwei="4段";
		}
		else if(socore>50) {
			duanwei="3段";
		}
		else if(socore>10) {
			duanwei="2段";
		}
		else if(socore>0) {
			duanwei="1段";
		}
		else if(socore<=0) {
			duanwei="菜鸟";
		}
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserNickName() {
		return userNickName;
	}
	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
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
	public double getShenglv() {
		return shenglv;
	}
	public void setShenglv(double shenglv) {
		this.shenglv = shenglv;
	}
	public double getDuanxianlv() {
		return duanxianlv;
	}
	public void setDuanxianlv(double duanxianlv) {
		this.duanxianlv = duanxianlv;
	}
	public String getDuanwei() {
		return duanwei;
	}
	public void setDuanwei(String duanwei) {
		this.duanwei = duanwei;
	}
	public int getPing() {
		return ping;
	}
	public void setPing(int ping) {
		this.ping = ping;
	}
	public int getTaopao() {
		return taopao;
	}
	public void setTaopao(int taopao) {
		this.taopao = taopao;
	}
}
