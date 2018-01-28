/**
 * 
 */
package com.luhao.dao;

import com.luhao.bean.FivePO;
import com.luhao.util.DBResult;

/**
 * @author howroad
 * @Date 2018年1月28日
 * @version 1.0
 */
public interface IFiveDao extends IBaseDao<FivePO,Integer>{
	public DBResult addUser(DBResult dbr);
	public FivePO findbyUserId(int id);
}
