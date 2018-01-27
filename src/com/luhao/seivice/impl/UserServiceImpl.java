/**
 * 
 */
package com.luhao.seivice.impl;

import com.luhao.bean.User;
import com.luhao.dao.IUserDao;
import com.luhao.dao.impl.UserDaoImpl;
import com.luhao.service.IUserService;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class UserServiceImpl implements IUserService {
	private IUserDao iud=new UserDaoImpl();
	@Override
	public boolean isexsit(String username) {
		return iud.isexsit(username);
	}

	@Override
	public int add(User user) {
		return iud.add(user).getGeneratedKey();
	}

	@Override
	public User login(User user) {
		return iud.checkUser(user);
	}

}
