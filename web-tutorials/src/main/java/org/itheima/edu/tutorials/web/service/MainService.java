package org.itheima.edu.tutorials.web.service;

import org.itheima.edu.tutorials.bean.ChapterDTO;
import org.itheima.edu.tutorials.bean.QuestionDTO;
import org.itheima.edu.tutorials.utils.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Poplar on 2017/6/16.
 */
@Service
public class MainService {


    // 数据源目录
    @Value("${config.dir.source}")
    String sourceFolder;

    /**
     * 根据类型获取所有章节
     * @param type  类型  0:java 1:kotlin 2:python
     * @return
     */
    public List<ChapterDTO> getChaptersByType(int type) {
        // TODO: 获取当前这个用户的进度

        File folder = new File(sourceFolder);
        System.out.println("chapters--> " + folder.getAbsolutePath());

        Random random = new Random();
        List<ChapterDTO> dtoList = new ArrayList<>();
        if (folder.exists() && folder.isDirectory()) {

            File[] files = folder.listFiles();
            ChapterDTO dto = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                // TODO: 读取每个题对应的描述信息 (数据库? 配置文件?)

                dto = new ChapterDTO();
                dto.setName(file.getName());
                dto.setDesc("我是描述信息" + i);
                int total = random.nextInt(100 + 1);
                dto.setTotal(total);
                dto.setCurrent(random.nextInt(total + 1));
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    /**
     * 根据章节获取所有题目
     * @param chapter 章节名称
     * @return
     */
    public List<QuestionDTO> getQuestionsByChapter(String chapter) {
        // logic-1\blueTicket\question.description
        // logic-1\blueTicket\question.title
        List<QuestionDTO> dtos = new ArrayList<QuestionDTO>();
        try {
            // TODO: 获取对应题目完成状态
            dtos = readChapter(chapter);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return dtos;
    }


    public List<QuestionDTO> readChapter(String chapter) throws UnsupportedEncodingException {
        List<QuestionDTO> dtos = new ArrayList<QuestionDTO>();

        File folder = new File(sourceFolder, chapter);
        System.out.println("list--> " + folder.getAbsolutePath());
        if (folder.exists() && folder.isDirectory()) {
            String chapterName = folder.getName();

            File[] files = folder.listFiles();
            for (File file : files) {
                File descFile = new File(file, "question.description");
                File titleFile = new File(file,  "question.title");
                String title = FileUtils.readFileToString(titleFile);
                String desc = FileUtils.readFileToString(descFile);
                dtos.add(new QuestionDTO(chapterName, file.getName(), title, desc));

            }
        }
        return dtos;
    }


    /**
     * 根据章节名, 问题id获取题目描述
     * @param chapter       章节名称
     * @param questionid    问题id
     * @return  题目描述
     */
    public String getQuestionDesc(String chapter, String questionid) {
        String result;
        if (questionid != null) {
            File rootDir = new File(sourceFolder);
            File descFile = new File(rootDir, chapter + File.separator + questionid + "/question.description");

            System.out.println(descFile.getAbsolutePath());
            result = FileUtils.readFileToString(descFile);
        } else {
            result = "error";
        }
        return result;
    }

    public String getQuestionTree(String chapter,String questionid) {
        if (!StringUtils.isEmpty(questionid)) {
            File rootDir = new File(sourceFolder);
            File manifestFile = new File(rootDir, chapter + File.separator + questionid + "/manifest.json");

            System.out.println(manifestFile.getAbsolutePath());
            return FileUtils.readFileToString(manifestFile);
        } else {
            return "error";
        }
    }
}
