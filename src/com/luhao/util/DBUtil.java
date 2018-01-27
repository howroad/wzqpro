/**
 * 
 */
package com.luhao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author howroad
 * @Date 2018年1月25日
 * @version 1.0
 */
public class DBUtil {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/db_lugame?useUnicode=true&characterEncoding=UTF-8";
	private static final String USER = "root";
	private static final String PASSWORD = "luhao123";

	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void open() {
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet excuteQuery(String sql, Object... parames) throws SQLException {
		ps = conn.prepareStatement(sql);
		for (int i = 0; i < parames.length; i++) {
			ps.setObject(i + 1, parames[i]);
		}
		return ps.executeQuery();
	}

	public DBResult excuteUpdate(String sql, Object... params) throws SQLException {
		int affectRows = -1;
		int generatedKey = -1;
		
		ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
		for (int i = 0; i < params.length; i++) {
			ps.setObject((i + 1), params[i]);
		}
		
		affectRows = ps.executeUpdate();
		
		rs = ps.getGeneratedKeys();
		if (rs.next()) {
			generatedKey = rs.getInt(1);
		}
		
		return new DBResult(affectRows, generatedKey);
	}

}
