package org.itheima.edu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 题目信息描述
 */
public class AppInfo implements Serializable {

	private static final long serialVersionUID = 2294855864636198846L;

	/**
	 * 每道题目的title
	 */
	private String title;

	/**
	 * 题目描述
	 */
	private String question;

	/**
	 * 代码信息
	 */
	private List<CodeInfo> codes;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<CodeInfo> getCodes() {
		return codes;
	}

	public void setCodes(List<CodeInfo> codes) {
		this.codes = codes;
	}

}
