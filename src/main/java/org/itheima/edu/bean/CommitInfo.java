package org.itheima.edu.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * 运行时，提交的信息
 *
 */
public class CommitInfo implements Serializable {

	private static final long serialVersionUID = -6940096226969251268L;

	// 提交的代码
	private Map<String, String> codes;

	public Map<String, String> getCodes() {
		return codes;
	}

	public void setCodes(Map<String, String> codes) {
		this.codes = codes;
	}
}
