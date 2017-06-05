package org.itheima.edu.bean;

import java.io.Serializable;

/**
 * 用来描述代码信息的，对应一个代码文件
 */
public class CodeInfo implements Serializable {
	private static final long serialVersionUID = -7358445793867546608L;

	/**
	 * 模版的文件名
	 */
	private String name;

	/**
	 * 模版的编译名称
	 */
	private String compileName;

	/**
	 * 模版的内容
	 */
	private String content;

	/**
	 * 模版显示顺序
	 */
	private int order;

	/**
	 * 模版是否可编辑
	 */
	private boolean readOnly;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompileName() {
		return compileName;
	}

	public void setCompileName(String compileName) {
		this.compileName = compileName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

}
