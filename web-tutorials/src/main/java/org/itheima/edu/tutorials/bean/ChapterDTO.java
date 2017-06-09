package org.itheima.edu.tutorials.bean;

/**
 * Created by Poplar on 2017/6/6.
 */
public class ChapterDTO {

    int total;          // 总体量      以100为准
    int current;        // 当前已完成
    String desc;        // 章节描述
    String name;        // 章节名称
    boolean isCompleted;// 是否完成

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        updateCompleteState();
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
        updateCompleteState();
    }

    public void updateCompleteState() {
        this.isCompleted = this.total == this.current && this.total != 0;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
