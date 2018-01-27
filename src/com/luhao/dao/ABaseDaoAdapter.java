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
public abstract class ABaseDaoAdapter<E, K extends Serializable> implements IBaseDao<E, K> {

	@Override
	public K save(E entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K delete(E entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K deleteById(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K update(E entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E findById(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<E> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
