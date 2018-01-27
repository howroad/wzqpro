/**
 * 
 */
package com.luhao.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author howroad
 * @Date 2018年1月25日
 * @version 1.0
 */
public interface IBaseDao<E,K extends Serializable> {
	
	public K save(E entity);
	
	public K delete(E entity);
	
	public K deleteById(K key);
	
	public K update(E entity);
	
	public E findById(K key);
	
	public List<E> findAll();

}
