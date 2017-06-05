package org.itheima.edu.bean;

import java.util.List;

/**
 * Created by Poplar on 2016/12/27.
 */
public class HomeBean {

    private float version;
    private String content;
    private float lastModified;
    private List<Integer> progress;

    public HomeBean() {
    }

    public HomeBean(float version) {
        this.version = version;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Integer> getProgress() {
        return progress;
    }

    public void setProgress(List<Integer> progress) {
        this.progress = progress;
    }

    public float getLastModified() {
        return lastModified;
    }

    public void setLastModified(float lastModified) {
        this.lastModified = lastModified;
    }
}
