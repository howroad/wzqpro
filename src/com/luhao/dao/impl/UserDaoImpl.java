/**
 * 
 */
package com.luhao.dao.impl;

import java.sql.SQLException;

import com.luhao.bean.User;
import com.luhao.dao.IUserDao;
import com.luhao.util.DBResult;
import com.luhao.util.DBSessionFactory;

/**
 * @author howroad
 * @Date 2018年1月26日
 * @version 1.0
 */
public class UserDaoImpl extends BaseDaoAdapterImpl<User,Integer> implements IUserDao{

	@Override
	public User checkUser(User user) {
		String username=user.getUsername();
		String password=user.getPassword();
		User u=null;
		try {
			u=this.fetchSingleEntity(DBSessionFactory.openSession().excuteQuery("select * from tb_user where username=? and password=?", username,password));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(u.getId()<1) {
			return null;
		}
		return u;
	}

	/* (non-Javadoc)
	 * @see com.luhao.dao.IUserDao#isexsit(com.luhao.bean.User)
	 */
	@Override
	public boolean isexsit(String username) {
		boolean b=false;
		try {
			//System.out.println(this.fetchSingleEntity(DBSessionFactory.openSession().excuteQuery("select * from tb_user where username=?", username)));
			b=this.fetchSingleEntity(DBSessionFactory.openSession().excuteQuery("select * from tb_user where username=?", username)).getId()>0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBSessionFactory.closeSession();
		return b;
	}

	/* (non-Javadoc)
	 * @see com.luhao.dao.IUserDao#add(com.luhao.bean.User)
	 */
	@Override
	public DBResult add(User user) {
		user.setNickname(user.getUsername());
		//System.out.println(user);
		DBResult dbr=null;
		try {
			dbr=DBSessionFactory.openSession().excuteUpdate("insert into tb_user(id,username,password,nickname,registtime) values(null,?,?,?,now())", user.getUsername(),user.getPassword(),user.getNickname());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbr;
	}
	
}
