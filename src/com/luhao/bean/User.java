/**
 * 
 */
package com.luhao.bean;

import java.util.Date;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class User {
	private int id;
	private String username;
	private String password;
	private String nickname;
	private Date registtime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Date getRegisttime() {
		return registtime;
	}
	public void setRegisttime(Date registtime) {
		this.registtime = registtime;
	}
	@Override
	public String toString() {
		return nickname;
	}
	public String toStreamString() {
		id=id==0?-1:id;
		username=username==null?"default":username;
		password=password==null?"default":password;
		nickname=nickname==null?"default":nickname;
		return id+"&"+username+"&"+password+"&"+nickname;
	}

}
