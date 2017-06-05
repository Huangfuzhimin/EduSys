package org.itheima.edu.bean;

import java.io.Serializable;

/**
 * 编译信息
 * 
 */
public class CompileInfo implements Serializable {

	private static final long serialVersionUID = 1271281697114123115L;

	// 是否通过编译
	private boolean pass;

	// 没有通过编译时的异常错误信息
	private String exception;

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}
