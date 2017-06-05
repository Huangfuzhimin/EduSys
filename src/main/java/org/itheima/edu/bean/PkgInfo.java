package org.itheima.edu.bean;

import java.io.Serializable;

/**
 * 用来描述应用程序信息
 *
 */
public class PkgInfo implements Serializable {

	private static final long serialVersionUID = 3624519774309024025L;

	// 应用安装目录
	private String rootDir;

	// 应用题目相关信息
	private AppInfo app;

	private RunInfo result;

	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	public AppInfo getApp() {
		return app;
	}

	public void setApp(AppInfo app) {
		this.app = app;
	}

	public RunInfo getResult() {
		return result;
	}

	public void setResult(RunInfo result) {
		this.result = result;
	}

}
