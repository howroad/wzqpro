/**
 * 
 */
package com.luhao.dao;

import com.luhao.bean.User;
import com.luhao.util.DBResult;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public interface IUserDao extends IBaseDao<User,Integer>{
	public User checkUser(User user);
	public boolean isexsit(String username);
	public DBResult add(User user);
	public User findById(int id);
}
