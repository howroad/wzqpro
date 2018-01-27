/**
 * 
 */
package com.luhao.util;

/**
 * @author howroad
 * @Date 2018年1月25日
 * @version 1.0
 */
public class DBResult {
	public DBResult() {}
	
	public DBResult(int affectRows, int generatedKey) {
		super();
		this.affectRows = affectRows;
		this.generatedKey = generatedKey;
	}

	public int getAffectRows() {
		return affectRows;
	}
	public void setAffectRows(int affectRows) {
		this.affectRows = affectRows;
	}
	public int getGeneratedKey() {
		return generatedKey;
	}
	public void setGeneratedKey(int generatedKey) {
		this.generatedKey = generatedKey;
	}
	private int affectRows;
	private int generatedKey;
	
}
