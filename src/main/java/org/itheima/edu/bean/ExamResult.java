package org.itheima.edu.bean;

import java.util.List;

public class ExamResult {

	private String account;
	private String name;
	private List<ExamItemResult> results;
	
	private int totalScore = 0; 		// 总分
	private long lastCommitTime = 0;	// 最后提交时间
	private int totalExecuteCount = 0;	// 总执行次数
	private int totalExecuteSuccessCount = 0;	// 总成功执行次数
	private int totalExecuteFailedCount = 0;	// 总失败执行次数
	private int totalExamCount = 0; 	// 总题目数量
	private int totalCompleteCount = 0; // 满分完成的题目数量
	private int totalTryCount = 0; 		// 尝试执行的题目数量

	private long lastModified = 0;		// 最后生成时间

	public void merge(ExamResult other) {
		if(other == null) {
			return;
		}

		if(results != null && other.results != null){
			results.addAll(other.results);
		}
		totalScore += other.totalScore;
		lastCommitTime = Math.max(lastCommitTime, other.lastCommitTime);
		totalExecuteCount += other.totalExecuteCount;
		totalExecuteSuccessCount += other.totalExecuteSuccessCount;
		totalExecuteFailedCount += other.totalExecuteFailedCount;
		
		totalExamCount += other.totalExamCount;
		totalCompleteCount += other.totalCompleteCount;
		totalTryCount += other.totalTryCount;

		lastModified = Math.max(lastModified, other.lastModified);
		
	}
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ExamItemResult> getResults() {
		return results;
	}

	public void setResults(List<ExamItemResult> results) {
		this.results = results;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public long getLastCommitTime() {
		return lastCommitTime;
	}

	public void setLastCommitTime(long lastCommitTime) {
		this.lastCommitTime = lastCommitTime;
	}

	public int getTotalExecuteCount() {
		return totalExecuteCount;
	}

	public void setTotalExecuteCount(int totalExecuteCount) {
		this.totalExecuteCount = totalExecuteCount;
	}

	public int getTotalExecuteSuccessCount() {
		return totalExecuteSuccessCount;
	}

	public void setTotalExecuteSuccessCount(int totalExecuteSuccessCount) {
		this.totalExecuteSuccessCount = totalExecuteSuccessCount;
	}

	public int getTotalExecuteFailedCount() {
		return totalExecuteFailedCount;
	}

	public void setTotalExecuteFailedCount(int totalExecuteFailedCount) {
		this.totalExecuteFailedCount = totalExecuteFailedCount;
	}
	public int getTotalExamCount() {
		return totalExamCount;
	}
	public void setTotalExamCount(int totalExamCount) {
		this.totalExamCount = totalExamCount;
	}
	public int getTotalCompleteCount() {
		return totalCompleteCount;
	}

	public void setTotalCompleteCount(int totalCompleteCount) {
		this.totalCompleteCount = totalCompleteCount;
	}

	public int getTotalTryCount() {
		return totalTryCount;
	}

	public void setTotalTryCount(int totalTryCount) {
		this.totalTryCount = totalTryCount;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		return "ExamResult [account=" + account + ", name=" + name + ", results=" + results.size() + ", totalScore="
				+ totalScore + ", lastCommitTime=" + lastCommitTime + ", totalExecuteCount=" + totalExecuteCount
				+ ", totalCompleteCount=" + totalCompleteCount + ", totalTryCount=" + totalTryCount + "]";
	}

	
}
