/**
 * 
 */
package com.luhao.service;

import com.luhao.vo.FiveVO;

/**
 * @author howroad
 * @Date 2018年1月28日
 * @version 1.0
 */
public interface IFiveService {
	public FiveVO findById(int id);
	public void win(int userId);
	public void lost(int userId);
	public void ping(int userId);
	public void taopao(int userId);
}
