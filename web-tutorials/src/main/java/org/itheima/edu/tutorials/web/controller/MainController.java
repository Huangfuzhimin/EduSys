package org.itheima.edu.tutorials.web.controller;

import org.itheima.edu.tutorials.bean.ChapterDTO;
import org.itheima.edu.tutorials.bean.QuestionDTO;
import org.itheima.edu.tutorials.utils.FileUtils;
import org.itheima.edu.tutorials.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Poplar on 2017/6/5.
 */
@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class MainController {


    // 数据源目录
    @Value("${config.dir.source}")
    String sourceFolder;

    /**
     * 获取章节目录列表
     *
     * @param request
     * @param type    0:java 1:kotlin 2:python
     * @return
     */
    // 获取章节目录
    @RequestMapping("/chapters")
    @ResponseBody
    public String chapters(HttpServletRequest request,
                           @RequestParam(required = false, defaultValue = "0") int type) {

        // TODO: 获取当前这个用户的进度

        File folder = new File(sourceFolder);
        System.out.println("chapters--> " + folder.getAbsolutePath());

        Random random = new Random();
        List<ChapterDTO> dtoList = new ArrayList<ChapterDTO>();
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

        return JsonUtils.toWrapperJson(0, dtoList);
    }

    /**
     * 获取指定章节所有题目
     *
     * @param request
     * @param chapter 章节名称
     * @return
     */
    @RequestMapping(value = "/chapter")
    @ResponseBody
    public String list(HttpServletRequest request, String chapter) {
//        logic-1\blueTicket\question.description
//        logic-1\blueTicket\question.title
        List<QuestionDTO> dtos = new ArrayList<QuestionDTO>();
        try {
            request.setCharacterEncoding("UTF-8");
            chapter = new String(chapter.getBytes("iso-8859-1"), "utf-8");

            // TODO: 获取对应题目是否已完成

            dtos = readChapter(chapter);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String s = JsonUtils.toWrapperJson(dtos);
//        printEncoding(s);
        return s;
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
                File titleFile = new File(file, "question.title");
                String title = FileUtils.readFileToString(titleFile);
                String desc = FileUtils.readFileToString(descFile);
                dtos.add(new QuestionDTO(chapterName, file.getName(), title, desc));

            }
        }
        return dtos;
    }


    @RequestMapping(value = "/desc")
    public void getDesc(HttpServletRequest request, HttpServletResponse response) {
//        LogUtils.printRequest(request);
        String chapter = request.getParameter("chapter");
        String questionid = request.getParameter("questionid");
        try {
            chapter = new String(chapter.getBytes("iso-8859-1"), "utf-8");
            questionid = new String(questionid.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            if (questionid != null) {
                File rootDir = new File(sourceFolder);
                File descFile = new File(rootDir, chapter + File.separator + questionid + "/question.description");

                System.out.println(descFile.getAbsolutePath());
                String s = FileUtils.readFileToString(descFile);

                response.getOutputStream().write(s.getBytes("utf-8"));
            } else {
                response.getOutputStream().write("error".getBytes("utf-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.getOutputStream().write("error".getBytes("utf-8"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @RequestMapping("/chapter/list")
    public String getChapterList(String name, String type) {

        return "chapter/list";
    }


}
