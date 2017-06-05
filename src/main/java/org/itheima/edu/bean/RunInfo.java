package org.itheima.edu.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 每次编译运行的信息
 */
public class RunInfo implements Serializable {

	private static final long serialVersionUID = 2680065770841965067L;

	// 运行时提交的信息
	private CommitInfo commit;

	// 编译信息
	private CompileInfo compile;

	// 测试信息
	private List<TestInfo> tests;

	// 当前运行得分
	private int score;

	// 运行起始时间
	private long startTime;

	// 运行结束时间
	private long endTime;

	// 是否已经提交代码
	private boolean isSubmit;
	
	public CommitInfo getCommit() {
		return commit;
	}

	public void setCommit(CommitInfo commit) {
		this.commit = commit;
	}

	public CompileInfo getCompile() {
		return compile;
	}

	public void setCompile(CompileInfo compile) {
		this.compile = compile;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public List<TestInfo> getTests() {
		return tests;
	}

	public void setTests(List<TestInfo> tests) {
		this.tests = tests;
	}

	public boolean isSubmit() {
		return isSubmit;
	}

	public void setSubmit(boolean isSubmit) {
		this.isSubmit = isSubmit;
	}

}
