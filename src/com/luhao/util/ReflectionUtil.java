/**
 * 
 */
package com.luhao.util;

import java.lang.reflect.Field;

/**
 * @author howroad
 * @Date 2018年1月25日
 * @version 1.0
 */
public class ReflectionUtil {
	//通过对象获得属性名数组
	public static String[] getFieldNames(Object obj) {
		Field [] fields=obj.getClass().getDeclaredFields();
		String[] fieldNames=new String[fields.length];
		for(int i=0;i<fields.length;i++) {
			String[] temp=fields[i].getName().split("\\.");
			fieldNames[i]=temp[temp.length-1];
		}
		
		return fieldNames;
	}
	//给对象的某个字段赋值
	public static void setFieldValue(Object obj,String fieldName,Object value) {
		Class<?> clazz=obj.getClass();
		String[] temps=fieldName.split("\\.");
		try {
			Field field=clazz.getDeclaredField(temps[temps.length-1]);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
