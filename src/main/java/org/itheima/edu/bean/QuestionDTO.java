package org.itheima.edu.bean;

import java.io.Serializable;

/**
 * Created by Poplar on 2017/6/5.
 */
public class QuestionDTO implements Serializable{

    String chapter;
    String title;
    String desc;

    public QuestionDTO() {
    }

    public QuestionDTO(String chapter, String title, String desc) {
        this.chapter = chapter;
        this.title = title;
        this.desc = desc;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
