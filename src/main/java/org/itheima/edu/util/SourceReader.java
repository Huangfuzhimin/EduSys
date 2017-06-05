package org.itheima.edu.util;

import org.itheima.edu.bean.ExamConfigBean;
import org.itheima.edu.bean.ExamBean;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Poplar on 2016/12/20.
 */
public class SourceReader {


    /**
     * 读取指定目录的所有题库数据
     * @param rootFolder
     */
    public static List<ExamBean> readSource(File rootFolder) {

        FileFilter folderFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };
        File[] files = rootFolder.listFiles(folderFilter);

        // 获取所有进阶目录的集合
        List<ExamBean> examBeanList = new ArrayList<>();

        for (File folder : files) {
            String folderName = folder.getName();
            System.out.println("file: " + folderName);

            File file = new File(folder, "config.xml");
            boolean configExist = file.exists() && file.isFile() /*&& file.length() > 0*/;
            if(configExist) {

                String examName = folderName;
                if(folderName.contains("_")){
                    examName = folderName.substring(folderName.indexOf("_") + 1);
                }
                ExamBean examBean = new ExamBean(examName);
                examBeanList.add(examBean);

                ExamConfigBean bean = XmlUtils.parseData(file, ExamConfigBean.class);

                if(bean == null) continue;
                // 包含每个进阶的 课堂知识、课下练习、视频学习三个分类目录

                List<ExamConfigBean.Dir> dirs = bean.dirs;
                int dirSize = dirs.size();
                for (int i = 0; i < dirSize; i++) {
                    // 每个分类目录
                    ExamConfigBean.Dir dir = dirs.get(i);
                    List<ExamConfigBean.Dir.Item> items = dir.items;
                    System.out.println("dir.name: " + dir.type);

                    List<ExamBean.SourceItem> sourceItems = examBean.getItems(dir.type);

                    ExamBean.SourceItem source;
                    if(items != null){
                        for (int k = 0; k < items.size(); k++) {
                            ExamConfigBean.Dir.Item item = items.get(k);

                            source = new ExamBean.SourceItem();
                            source.index = k;
                            source.fileName = item.name;
                            source.alias = item.alias;
                            source.file = "/" + folder.getName() + "/" + dir.name + "/" + item.name;
                            sourceItems.add(source);
                        }
                    }
                }

                if(examBean.getMaps().size() > 0){
//                    System.out.println(folderName + " ==> " + examBean);
                }
            }


        }
        return examBeanList;
    }


    static Map<String, String> categoryMaps;
    static {
        categoryMaps = new HashMap<String, String>(){
            {
                put("LessonSource", "lesson");
                put("ExerciseSource", "exercise");
                put("Video", "video");
            }
        };
    }

    /**
     * 向指定目录写出数据配置
     * @param rootFolder
     */
    public static void writeConfig(File rootFolder) {

        FileFilter folderFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };
        File[] files = rootFolder.listFiles(folderFilter);

        for (int i = 0; i < files.length; i++) {
            ExamConfigBean examConfigBean = new ExamConfigBean();
            File categoryFolder = files[i];
            System.out.println(categoryFolder.getName());

            for (Map.Entry<String, String> entry : categoryMaps.entrySet()) {
                File folder = new File(categoryFolder, entry.getKey());


                if(!folder.exists() || !folder.isDirectory()){
                    continue;
                }

                ExamConfigBean.Dir dir = new ExamConfigBean.Dir(entry.getKey(), entry.getValue());
                examConfigBean.add(dir);


                File[] itemFiles = folder.listFiles();
                for (File itemFile : itemFiles) {
                    String name = itemFile.getName();
                    dir.add(new ExamConfigBean.Dir.Item(name, getAlias(name)));
                }

            }

            String s = XmlUtils.newInstance().toXML(examConfigBean);
//            System.out.println(s);

            if(examConfigBean.dirs.size() == 0) {
                continue;
            }
            FileWriter fileWriter = null;
            try {
                File configFile = new File(categoryFolder, "config.xml");
                fileWriter = new FileWriter(configFile);
                fileWriter.write(s);
                fileWriter.flush();
                System.out.println("写出完毕!" + configFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(fileWriter != null) fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getAlias(String name) {
        int pointIndex = name.indexOf(".");
        if(pointIndex != -1)
            name = name.substring(0, pointIndex);

        int i = name.indexOf("_");
        if( i != -1)
            name = name.substring(i + 1);

        return name;
    }


}
