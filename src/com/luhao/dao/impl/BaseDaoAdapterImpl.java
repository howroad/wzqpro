/**
 * 
 */
package com.luhao.dao.impl;


import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.luhao.dao.ABaseDaoAdapter;
import com.luhao.util.ReflectionUtil;

/**
 * @author howroad
 * @Date 2018年1月25日
 * @version 1.0
 */
public class BaseDaoAdapterImpl<E, K extends Serializable> extends ABaseDaoAdapter<E, K> {
	private Class<?> entityClass;

	public BaseDaoAdapterImpl() {
		// 获得父类中E的类型的映射
		Type superClass = this.getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) superClass;
		Type[] tps = pt.getActualTypeArguments();
		entityClass = (Class<?>) tps[0];
	}

	@SuppressWarnings("unchecked")
	public E fetchSingleEntity(ResultSet rs) {
		E entity = null;
		try {
			entity = (E) entityClass.getConstructor().newInstance();
			ResultSetMetaData rsmd = rs.getMetaData();
			if (rs.next()) {
				String[] fields = ReflectionUtil.getFieldNames(entity);
				for (int i = 0; i < fields.length && i < rsmd.getColumnCount(); i++) {
					ReflectionUtil.setFieldValue(entity, fields[i], rs.getObject(i + 1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}

}
