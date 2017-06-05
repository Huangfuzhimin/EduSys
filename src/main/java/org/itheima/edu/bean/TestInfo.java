package org.itheima.edu.bean;

import java.io.Serializable;

/**
 * 用来描述记录测试用例中的单个测试方法结果信息
 *
 */
public class TestInfo implements Serializable {
	public final static int STATE_UNTEST = 0;
	public final static int STATE_TEST_GIVEUP = 1;
	public final static int STATE_TEST_FAILED = 2;
	public final static int STATE_TEST_SUCCESS = 3;

	private static final long serialVersionUID = -7208192101271531491L;

	// 测试用来状态
	// 0: 没有执行测试
	// 1: 依赖执行失败，放弃执行
	// 2: 执行了测试，但没通过
	// 3: 执行了测试，通过了
	private int state;

	// 测试的描述
	private String desc;

	// 此项测试的当前得分
	private int currentScore;

	// 此项测试的实际分值
	private int totalScore;

	// 测试错误时的结果提示
	private String error;

	private long startTime;

	private long endTime;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
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

}
