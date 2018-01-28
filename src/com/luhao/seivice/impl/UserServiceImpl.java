/**
 * 
 */
package com.luhao.seivice.impl;

import com.luhao.bean.User;
import com.luhao.dao.IFiveDao;
import com.luhao.dao.IUserDao;
import com.luhao.dao.impl.FiveDaoImpl;
import com.luhao.dao.impl.UserDaoImpl;
import com.luhao.service.IUserService;
import com.luhao.util.DBResult;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class UserServiceImpl implements IUserService {
	private IUserDao iud=new UserDaoImpl();
	private IFiveDao ifd=new FiveDaoImpl();
	@Override
	public boolean isexsit(String username) {
		return iud.isexsit(username);
	}

	@Override
	public int add(User user) {
		DBResult dbr=iud.add(user);
		if(dbr!=null) {
			ifd.addUser(dbr);
		}
		return dbr.getGeneratedKey();
	}

	@Override
	public User login(User user) {
		return iud.checkUser(user);
	}

	@Override
	public User getById(int id) {
		return iud.findById(id);
	}

}
