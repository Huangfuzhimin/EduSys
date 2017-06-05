package org.itheima.edu.controller;

import org.itheima.edu.bean.ExamBean;
import org.itheima.edu.bean.ExamConfigBean;
import org.itheima.edu.util.SourceReader;
import org.itheima.edu.util.XmlUtils;
import org.junit.Before;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Poplar on 2016/12/9.
 */
public class TestControllerTest {

    @Before
    public void setUp() throws Exception {
        System.out.println("-------------------setUp");

    }

    public static void main(String[] args){

//        File rootFolder = new File("E:\\cms\\练习题\\source");
        File rootFolder = new File("C:\\Users\\Poplar\\Desktop\\source");
//        File rootFolder = new File("cms/source");
        // 生成config文件
        SourceReader.writeConfig(rootFolder);
        // 从config读取配置
//        readSource(rootFolder);



//        writeXmlTest();

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

//    private static void writeConfig(File rootFolder) {
//
//        FileFilter folderFilter = new FileFilter() {
//            @Override
//            public boolean accept(File pathname) {
//                return pathname.isDirectory();
//            }
//        };
//        File[] files = rootFolder.listFiles(folderFilter);
//
//        for (int i = 0; i < files.length; i++) {
//            ExamConfigBean examConfigBean = new ExamConfigBean();
//            File categoryFolder = files[i];
//            System.out.println(categoryFolder.getName());
//
//            for (Map.Entry<String, String> entry : categoryMaps.entrySet()) {
//                File folder = new File(categoryFolder, entry.getKey());
//
//
//                if(!folder.exists() || !folder.isDirectory()){
//                    continue;
//                }
//
//                ExamConfigBean.Dir dir = new ExamConfigBean.Dir(entry.getKey(), entry.getValue());
//                examConfigBean.add(dir);
//
//
//                File[] itemFiles = folder.listFiles();
//                for (File itemFile : itemFiles) {
//                    String name = itemFile.getName();
//                    dir.add(new ExamConfigBean.Dir.Item(name, getAlias(name)));
//                }
//
//            }
//
//            String s = XmlUtils.newInstance().toXML(examConfigBean);
//            System.out.println(s);
//
//            if(examConfigBean.dirs.size() == 0) {
//                continue;
//            }
//            FileWriter fileWriter = null;
//            try {
//                File configFile = new File(categoryFolder, "config.xml");
//                fileWriter = new FileWriter(configFile);
//                fileWriter.write(s);
//                fileWriter.flush();
//                System.out.println("写出完毕!" + configFile.getAbsolutePath());
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    if(fileWriter != null) fileWriter.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//
//
//    }

    private static String getAlias(String name) {
        int pointIndex = name.indexOf(".");
        if(pointIndex != -1)
            name = name.substring(0, pointIndex);

        int i = name.indexOf("_");
        if( i != -1)
            name = name.substring(i + 1);

        return name;
    }

    private static void readSource(File rootFolder) {

        FileFilter folderFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };
        File[] files = rootFolder.listFiles(folderFilter);

        // 获取所有进阶目录的集合
        List<ExamBean> examsResult = new ArrayList<>();

        for (File folder : files) {
            System.out.println("file: " + folder.getName());

            ExamBean examBean = new ExamBean(folder.getName());
            File file = new File(folder, "config.xml");
            if(file.exists() && file.isFile()) {
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
                examsResult.add(examBean);
            }

            if(examBean.getMaps().size() > 0){
                System.out.println(folder.getName() + " ==> " + examBean);
            }

        }
    }


    private static void writeXmlTest() {
        ExamConfigBean examConfigBean = new ExamConfigBean();

        ExamConfigBean.Dir dir = new ExamConfigBean.Dir("LessonSource", "lesson");
        dir.add(new ExamConfigBean.Dir.Item("1_01_Welcome", "Welcome"));
        dir.add(new ExamConfigBean.Dir.Item("1_02_Computer_think", "Computer_think"));
        dir.add(new ExamConfigBean.Dir.Item("1_03_Instructions", "Instructions"));

        examConfigBean.add(dir);

        ExamConfigBean.Dir dir1 = new ExamConfigBean.Dir("ExerciseSource", "exercise");
        dir1.add(new ExamConfigBean.Dir.Item("alarmClock", "AlarmClock"));
        dir1.add(new ExamConfigBean.Dir.Item("blackjack", "BlackJack"));
        dir1.add(new ExamConfigBean.Dir.Item("blueTicket", "BlueTicket"));

        examConfigBean.add(dir1);


        String s = XmlUtils.newInstance().toXML(examConfigBean);
//        System.out.println(s);
    }

    String a = "<section class=\"content\"><div class=\"row\"><div id=\"lesson\" class=\"col-md-3 col-sm-6 col-xs-12\" style=\"cursor: pointer;\"><div class=\"info-box bg-aqua\"><span class=\"info-box-icon\"><i class=\"fa\"><b id=\"index\">01</b></i></span><div class=\"info-box-content\"><span class=\"info-box-number-bg\" id=\"name\">Java编程入门</span><div class=\"progress\"><div class=\"progress-bar\" style=\"width: 70%\"></div></div><span class=\"progress-description\">进度已完成70%，请继续加油！</span></div></div></div><div class=\"col-md-3 col-sm-6 col-xs-12\" style=\"cursor: pointer;\"><div class=\"info-box bg-maroon\"><span class=\"info-box-icon\"><i class=\"fa\"><b>02</b></i></span><div class=\"info-box-content\"><span class=\"info-box-number-bg\">String</span><div class=\"progress\"><div class=\"progress-bar\" style=\"width: 70%\"></div></div><span class=\"progress-description\">进度已完成70%，请继续加油！</span></div></div></div></div></section>";

}