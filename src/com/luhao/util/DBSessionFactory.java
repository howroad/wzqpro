/**
 * 
 */
package com.luhao.util;

/**
 * @author howroad
 * @Date 2018年1月25日
 * @version 1.0
 */
public class DBSessionFactory {
	private static final ThreadLocal<DBUtil> THREADLOCAL=new ThreadLocal<DBUtil>();
	
	public static DBUtil openSession() {
		DBUtil session =THREADLOCAL.get();
			if(session==null) {
				session=new DBUtil();
				THREADLOCAL.set(session);
			}
			session.open();
			return session;
	}

	public static void closeSession() {
		DBUtil session=THREADLOCAL.get();
		THREADLOCAL.set(null);
		if(session!=null) {
			session.close();
		}
	}
}
