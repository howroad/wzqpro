/**
 * 
 */
package com.luhao.service.impl;

import com.luhao.bean.FivePO;
import com.luhao.bean.User;
import com.luhao.dao.IFiveDao;
import com.luhao.dao.IUserDao;
import com.luhao.dao.impl.FiveDaoImpl;
import com.luhao.dao.impl.UserDaoImpl;
import com.luhao.service.IFiveService;
import com.luhao.vo.FiveVO;

/**
 * @author howroad
 * @Date 2018年1月28日
 * @version 1.0
 */
public class FiveServiceImpl implements IFiveService {
	private IFiveDao ifd=new FiveDaoImpl();
	private IUserDao iud=new UserDaoImpl();
	@Override
	public FiveVO findById(int id) {
		FivePO fp=ifd.findbyUserId(id);
		User user=iud.findById(id);
		FiveVO fv=fp.toFiveVO();
		fv.setUserNickName(user.getNickname());
		fv.init();
		return fv;
	}

	@Override
	public void win(int userId) {
		ifd.win(userId);
		
	}

	@Override
	public void lost(int userId) {
		ifd.lost(userId);
		
	}

	@Override
	public void ping(int userId) {
		ifd.ping(userId);
		
	}

	@Override
	public void taopao(int userId) {
		ifd.taopao(userId);
		
	}

}
