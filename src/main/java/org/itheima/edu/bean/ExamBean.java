package org.itheima.edu.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Poplar on 2016/12/12.
 */
public class ExamBean {

    String examName;

    Map<String, List<SourceItem>> maps = new HashMap<>();

    public ExamBean(String examName) {
        this.examName = examName;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }


    public Map<String, List<SourceItem>> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, List<SourceItem>> maps) {
        this.maps = maps;
    }

    public List<SourceItem> getItems(String type){
        List<SourceItem> sourceItems = maps.get(type);
        if(sourceItems == null){
            sourceItems = new ArrayList<>();
            maps.put(type, sourceItems);
        }
        return sourceItems;
    }

    @Override
    public String toString() {
        return "ExamBean{" +
                "maps=" + maps +
                '}';
    }

    public static class SourceItem {
        public int index;
        public String file;
        public String fileName;
        public String alias;

        @Override
        public String toString() {
            return "SourceItem{" +
                    "index=" + index +
                    ", file=" + file +
                    ", fileName='" + fileName + '\'' +
                    ", alias='" + alias + '\'' +
                    "}\n";
        }
    }

}
