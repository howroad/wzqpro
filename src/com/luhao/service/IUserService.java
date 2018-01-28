/**
 * 
 */
package com.luhao.service;

import com.luhao.bean.User;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public interface IUserService {
	public boolean isexsit(String username);
	public int add(User user);
	public User login(User user);
	public User getById(int id);
}
