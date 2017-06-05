package org.itheima.edu.bean;

import java.util.List;

public class ListWrapper<T> {

	private int code;
	private String msg;
	/**
	 * 数据
	 */
	private List<T> data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ListWrapper [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}

}