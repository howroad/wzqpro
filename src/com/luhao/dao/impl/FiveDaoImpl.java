/**
 * 
 */
package com.luhao.dao.impl;

import java.sql.SQLException;

import com.luhao.bean.FivePO;
import com.luhao.dao.IFiveDao;
import com.luhao.util.DBResult;
import com.luhao.util.DBSessionFactory;

/**
 * @author howroad
 * @Date 2018年1月28日
 * @version 1.0
 */
public class FiveDaoImpl extends BaseDaoAdapterImpl<FivePO,Integer> implements IFiveDao {

	@Override
	public DBResult addUser(DBResult dbr) {
		int userId=dbr.getGeneratedKey();
		DBResult dbrFive=null;
		try {
			dbrFive=DBSessionFactory.openSession().excuteUpdate("insert into tb_wzq values(null,?,0,0,0,0)", userId);
			DBSessionFactory.closeSession();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBSessionFactory.closeSession();
		}
		return dbrFive;
	}

	/* (non-Javadoc)
	 * @see com.luhao.dao.IFiveDao#findbyId(int)
	 */
	@Override
	public FivePO findbyUserId(int userId) {
		FivePO fp=null;
		try {
			fp=this.fetchSingleEntity(DBSessionFactory.openSession().excuteQuery("select * from tb_wzq where user_id=?", userId));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBSessionFactory.closeSession();
		}
		if(fp.getId()<1) {return null;}
		return fp;
	}

}
