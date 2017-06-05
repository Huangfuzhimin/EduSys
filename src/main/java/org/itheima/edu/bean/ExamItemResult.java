package org.itheima.edu.bean;

public class ExamItemResult {

	private String name;
	private int score;
	private int executeCount;
	private int compileSuccessCount;
	private int compileFailedCount;
	private long commitTime;
	private int topScore;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getExecuteCount() {
		return executeCount;
	}

	public void setExecuteCount(int executeCount) {
		this.executeCount = executeCount;
	}

	public int getCompileSuccessCount() {
		return compileSuccessCount;
	}

	public void setCompileSuccessCount(int compileSuccessCount) {
		this.compileSuccessCount = compileSuccessCount;
	}

	public int getCompileFailedCount() {
		return compileFailedCount;
	}

	public void setCompileFailedCount(int compileFailedCount) {
		this.compileFailedCount = compileFailedCount;
	}

	@Override
	public String toString() {
		return "ExamItemResult [name=" + name + ", score=" + score
				+ ", executeCount=" + executeCount + ", compileSuccessCount="
				+ compileSuccessCount + ", compileFailedCount="
				+ compileFailedCount + "]";
	}

	public long getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(long commitTime) {
		this.commitTime = commitTime;
	}

	public int getTopScore() {
		return topScore;
	}

	public void setTopScore(int topScore) {
		this.topScore = topScore;
	}

}
